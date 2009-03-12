package hr.fer.zemris.vhdllab.service.filetype.testbench;

import hr.fer.zemris.vhdllab.api.results.VHDLGenerationMessage;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.api.util.StringFormat;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.TimeScale;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchParserException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.Testbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.TestbenchParser;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.Signal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.SignalChange;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.service.FileService;
import hr.fer.zemris.vhdllab.service.filetype.CircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.filetype.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.filetype.VhdlGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

public class TestbenchVhdlGenerator implements VhdlGenerator {

    @Autowired
    private FileService fileService;
    @Resource(name = "circuitInterfaceExtractionService")
    private CircuitInterfaceExtractor ciExtractor;

    @Override
    public VHDLGenerationResult generate(FileInfo file)
            throws VhdlGenerationException {
        if (file == null)
            throw new NullPointerException("File can not be null.");
        if (!file.getType().equals(FileType.TESTBENCH))
            throw new IllegalArgumentException("File type must be "
                    + FileType.TESTBENCH);

        String content = file.getData();

        Testbench tbInfo = null;
        try {
            tbInfo = TestbenchParser.parseXml(content);
        } catch (UniformTestbenchParserException e1) {
            throw new VhdlGenerationException(e1);
        }

        Caseless name = new Caseless(tbInfo.getSourceName());

        FileInfo source = fileService.findByName(file.getProjectId(), name);
        CircuitInterface ci = ciExtractor.extract(source);
        String vhdl = null;
        try {
            vhdl = generirajVHDL(file.getName().toString(), name.toString(),
                    ci, tbInfo);
        } catch (Exception e) {
            throw new VhdlGenerationException(e);
        }
        return new VHDLGenerationResult(true,
                new ArrayList<VHDLGenerationMessage>(0), vhdl);
    }

    /**
     * Create a testbench VHDL code from circuit interface and testbench.
     * 
     * @param testBenchFileName
     *            name of the created testbench for particular component
     * @param testedFileName
     *            name of the component for which we created testbench
     * @param ci
     *            a circuit interface
     * @param tbInfo
     *            information about testbench from inducement
     * @return a string representing testbench VHDL.
     * 
     * @throws NullPointerException
     *             if <code>ci</code> or <code>testedFileName</code> or
     *             <code>testBnechFileName</code> or <code>tbInfo</code> is
     *             <code>null</code>.
     * 
     */
    private String generirajVHDL(String testBenchFileName,
            String testedFileName, CircuitInterface ci, Testbench tbInfo) {
        if (ci == null)
            throw new NullPointerException("Circuit interface can not be null.");
        if (testedFileName == null)
            throw new NullPointerException("Tested filename can not be null.");
        if (testBenchFileName == null)
            throw new NullPointerException(
                    "TestBench filename can not be null.");
        if (tbInfo == null)
            throw new NullPointerException(
                    "TestBench filename can not be null.");
        long TestBenchLength = tbInfo.getTestBenchLength();
        int bufferSize = 0;
        if (tbInfo.getSimulationLength() != 0)
            TestBenchLength = tbInfo.getSimulationLength();
        for (Signal s : tbInfo.getSignals()) {
            bufferSize += s.getSignalChangeList(0, TestBenchLength).size() * 12;
        }
        StringBuilder vhdl = new StringBuilder(bufferSize);
        vhdl.append("library IEEE;\n").append("use IEEE.STD_LOGIC_1164.ALL;\n")
                .append("\n").append("entity ").append(testBenchFileName)
                .append(" is \n").append("end ").append(testBenchFileName)
                .append(";\n").append("\n").append(
                        "architecture structural of ")
                .append(testBenchFileName).append(" is \n");
        populateComponent(ci, vhdl);
        vhdl.append("end component;\n").append("\n");
        populateSignals(ci, vhdl);
        vhdl.append("begin\n");
        populatePortMap(ci, vhdl);
        vhdl.append("\n").append("\tprocess").append("\n").append("\tbegin")
                .append("\n");
        populateProcess(ci, tbInfo, vhdl);
        vhdl.append("\n").append("\tend process;\n").append("end structural;");
        return vhdl.toString();
    }

    /**
     * Populate a process in testbench VHDL.
     * 
     * @param ci
     *            a circuit interface from where to draw information.
     * @param tbInfo
     *            information about testbench from inducement.
     * @param vhdl
     *            a string builder where to store testbench VHDL.
     */
    private void populateProcess(CircuitInterface ci, Testbench tbInfo,
            StringBuilder vhdl) {
        List<ChangesInMoment> list = createProcessList(ci, tbInfo);
        tbInfo.getTimeScale();
        long time = -1;
        for (ChangesInMoment slot : list) {
            if (time != -1) {
                vhdl.append("\t\twait for ").append(
                        slot.getTime()
                                / TimeScale
                                        .getMultiplier(tbInfo.getTimeScale())
                                - time
                                / TimeScale
                                        .getMultiplier(tbInfo.getTimeScale()))
                        .append(" ").append(tbInfo.getTimeScale().toString())
                        .append(";\n\n");
            }
            for (ChangeWithSignalName change : slot.getList()) {
                String state = change.getChange().getSignalValue();
                vhdl.append("\t\t").append(
                        getTestbenchSignal(change.getSignalName())).append(
                        " <= ");
                if (ci.getPort(change.getSignalName()).getType().getRange()
                        .isScalar())
                    vhdl.append("\'").append(state).append("\';\n");
                else
                    vhdl.append("\"").append(state).append("\";\n");
            }
            time = slot.getTime();
        }
        vhdl.append("\t\twait for ").append(
                tbInfo.getPeriodLength()
                        / TimeScale.getMultiplier(tbInfo.getTimeScale()))
                .append(" ").append(tbInfo.getTimeScale().toString()).append(
                        ";\n");
        vhdl.append("\t\twait;\n");

    }

    /**
     * Create a process list. This method will create a list of objects
     * ChangesInMoment from where method populateProcess(CircuitInterface,
     * Testbench, StringBuilder) will draw information regarding signal changes
     * written in Testbench.
     * 
     * @param ci
     *            a circuit interface from where to draw information.
     * @param tbInfo
     *            information about testbench from inducement.
     */
    private List<ChangesInMoment> createProcessList(CircuitInterface ci,
            Testbench tbInfo) {
        ChangesInMoment tmp = new ChangesInMoment(0);
        Map<ChangesInMoment, ChangesInMoment> table = new HashMap<ChangesInMoment, ChangesInMoment>();
        for (Signal s : tbInfo.getSignals()) {
            if (!ci.getPort(s.getName()).getDirection().isIN())
                continue;
            long TestBenchLength = tbInfo.getTestBenchLength();
            if (tbInfo.getSimulationLength() != 0)
                TestBenchLength = tbInfo.getSimulationLength();
            for (SignalChange i : s.getSignalChangeList(0, TestBenchLength)) {
                tmp.setTime(i.getTime());
                ChangesInMoment list = table.get(tmp);
                if (list == null) {
                    list = new ChangesInMoment(tmp.getTime());
                    table.put(list, list);
                }
                ChangeWithSignalName imp = new ChangeWithSignalName(
                        s.getName(), i);
                list.addChangeWithSignalName(imp);
            }
        }
        Collection<ChangesInMoment> c = table.values();
        List<ChangesInMoment> l = new ArrayList<ChangesInMoment>(c);
        Collections.sort(l);
        return l;

    }

    /**
     * This class contains information about each signal change in the
     * particular time moment. It contains time of change and list of
     * ChangeWithSignalName structure.
     * 
     * @author Ivan Kmetovic
     * 
     */
    private class ChangesInMoment implements Comparable<ChangesInMoment> {

        private long time;
        private List<ChangeWithSignalName> list = new ArrayList<ChangeWithSignalName>();
        private List<ChangeWithSignalName> list_ro = Collections
                .unmodifiableList(list);

        /**
         * Construct an instance of this class using a time. A time must be a
         * non-negative number.
         * 
         * @param time
         */
        public ChangesInMoment(long time) {
            setTime(time);
        }

        /**
         * Getter method for time of signal change.
         * 
         * @return a time of signal change.
         */
        public long getTime() {
            return time;
        }

        /**
         * Setter method for a time of signal change.
         * 
         * @param time
         *            a time of signal change.
         * @throws IllegalArgumentException
         *             if <code>time</code> is null.
         */
        public void setTime(long time) {
            if (time < 0)
                throw new IllegalArgumentException("Time can not be negative.");
            this.time = time;
        }

        /**
         * Getter method for a read-only list of ChangeWithSignalName.
         * 
         * @return a read-only list of ChangeWithSignalName.
         */
        public List<ChangeWithSignalName> getList() {
            return list_ro;
        }

        /**
         * Append an ChangeWithSignalName to the end of this list.
         * 
         * @param i
         *            an ChangeWithSignalName to add.
         * @return <code>true</code> if <code>i</code> has been added;
         *         <code>false</code> otherwise.
         * @throws NullPointerException
         *             if <code>i</code> is <code>null</code>.
         */
        public boolean addChangeWithSignalName(ChangeWithSignalName i) {
            if (i == null)
                throw new NullPointerException(
                        "ChangeWithSignalName can not be null.");
            if (getList().contains(i))
                return false;
            list.add(i);
            return true;
        }

        /**
         * Compares this object ChangesInMoment with the specified object for
         * order. These two objects are compared by time.
         * 
         * @param other
         *            <code>ChangesInMoment</code> to be compared.
         * @return the value 0 if the argument object ChangesInMoment is equal
         *         to this object; a value less than 0 if this object has time
         *         less than the object argument; and a value greater than 0 if
         *         this object has time greater than the object argument.
         * @throws ClassCastException
         *             if the specified object's type prevents it from being
         *             compared to this ChangesInMoment.
         */
        public int compareTo(ChangesInMoment other) {
            long res = this.time - other.getTime();
            if (res < 0)
                return -1;
            else if (res > 0)
                return 1;
            else
                return 0;
        }

        /**
         * Compares this object ChangesInMoment to the specified object. Returns
         * <code>true</code> if and only if the specified object is also a
         * <code>ChangesInMoment</code> and if times are the same.
         * 
         * @param o
         *            the object to compare this <code>ChangesInMoment</code>
         *            against.
         * @return <code>true</code> if <code>ChangesInMoment</code> are equal;
         *         <code>false</code> otherwise.
         */
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ChangesInMoment))
                return false;
            ChangesInMoment other = (ChangesInMoment) o;
            return this.time == other.getTime();
        }

        /**
         * Returns a hash code value for this <code>ChangesInMoment</code>
         * instance. The hash code of <code>ChangesInMoment</code> instance is
         * hash code of time.
         * <p>
         * This ensures that <code>s1.equals(s2)</code> implies that
         * <code>s1.hashCode() == s2.hashCode()</code> for any two
         * ChangesInMoment objects, <code>s1</code> and <code>s2</code>, as
         * required by the general contract of <code>Object.hashCode</code>.
         * 
         * @return a hash code value for this <code>ChangesInMoment</code>
         *         instance.
         * @see java.lang.String#hashCode()
         * @see java.util.List#hashCode()
         */
        @Override
        public int hashCode() {
            return Long.valueOf(time).hashCode();
        }

    }

    /**
     * This class contains information regarding a signal change in a certain
     * time. It contains of signal name and an class that describes signal
     * change.
     * 
     * @author Ivan Kmetovic
     * 
     */
    private class ChangeWithSignalName {

        private String signalName;
        private SignalChange change;

        /**
         * Construct an instance of this class using a signal name and an signal
         * change to explain an ChangeWithSignalName. A signal name must have
         * the following format:
         * <ul>
         * <li>it must contain only alpha (only letters of english alphabet),
         * numeric (digits 0 to 9) or underscore (_) characters
         * <li>it must not start with a non-alpha character
         * <li>it must not end with an underscore character
         * <li>it must not contain an underscore character after an underscore
         * character
         * <li>it must not be a reserved word (check at
         * hr.fer.zemris.vhdllab.utilities.NotValidVHDLNames.txt)
         * </ul>
         * 
         * @param name
         *            a name of a signal.
         * @param change
         *            information about signal change
         * @throws NullPointerException
         *             if <code>name</code> or an <code>impulse</code> is
         *             <code>null</code>.
         * @throws IllegalArgumentException
         *             if <code>name</code> is not of correct format.
         */
        public ChangeWithSignalName(String name, SignalChange change) {
            if (change == null)
                throw new NullPointerException("Change can not be null.");
            if (name == null)
                throw new NullPointerException("Signal name can not be null.");
            if (!StringFormat.isCorrectEntityName(name))
                throw new IllegalArgumentException(
                        "Signal name is not of correct format.");
            this.signalName = name;
            this.change = change;
        }

        /**
         * Getter method for an SignalChange.
         * 
         * @return a description of signal change.
         */
        public SignalChange getChange() {
            return change;
        }

        /**
         * Getter method for a name of a signal.
         * 
         * @return a name of a signal.
         */
        public String getSignalName() {
            return signalName;
        }

        /**
         * Compares this ChangeWithSignalName to the specified object. Returns
         * <code>true</code> if and only if the specified object is also a
         * <code>ChangeWithSignalName</code> and if signal names and described
         * signal changes are the same.
         * 
         * @param o
         *            the object to compare this
         *            <code>ChangeWithSignalName</code> against.
         * @return <code>true</code> if <code>ChangeWithSignalName</code> are
         *         equal; <code>false</code> otherwise.
         */
        @Override
        public boolean equals(Object o) {
            if (o == null)
                return false;
            if (!(o instanceof ChangeWithSignalName))
                return false;
            ChangeWithSignalName other = (ChangeWithSignalName) o;
            return other.getSignalName().equalsIgnoreCase(this.signalName)
                    && other.getChange().equals(this.change);
        }

    }

    /**
     * Populate a port map in testbench VHDL.
     * 
     * @param ci
     *            a circuit interface from where to draw information.
     * @param vhdl
     *            a string builder where to store testbench VHDL.
     */
    private void populatePortMap(CircuitInterface ci, StringBuilder vhdl) {
        vhdl.append("uut: ").append(ci.getName()).append(" port map ( ");
        for (Port p : ci.getPorts()) {
            vhdl.append(getTestbenchSignal(p.getName())).append(", ");
        }
        vhdl.deleteCharAt(vhdl.length() - 2); // deleting last ','
        vhdl.append(");\n");
    }

    /**
     * Populate a signal list in testbench VHDL.
     * 
     * @param ci
     *            a circuit interface from where to draw information.
     * @param vhdl
     *            a string builder where to store testbench VHDL.
     */
    private void populateSignals(CircuitInterface ci, StringBuilder vhdl) {
        for (Port p : ci.getPorts()) {
            vhdl.append("\tsignal ").append(getTestbenchSignal(p.getName()))
                    .append(" : ").append(p.getType().getTypeName());
            if (p.getType().getRange().isVector())
                vhdl.append("(").append(p.getType().getRange().getFrom())
                        .append(" ").append(
                                p.getType().getRange().getDirection()).append(
                                " ").append(p.getType().getRange().getTo())
                        .append(")");
            vhdl.append(";\n");
        }
    }

    /**
     * Create a testbench signal name from a given signal name. Testbench signal
     * name will have the following format: <blockquote> tb_'signalName'
     * </blockquote>
     * 
     * @param name
     *            a name of a signal.
     * @return a testbench signal name.
     */
    private String getTestbenchSignal(String name) {
        return "tb_" + name;
    }

    /**
     * Populate a component in testbench VHDL.
     * 
     * @param ci
     *            a circuit interface from where to draw information.
     * @param vhdl
     *            a string builder where to store testbench VHDL.
     */
    private void populateComponent(CircuitInterface ci, StringBuilder vhdl) {
        vhdl.append("component ").append(ci.getName()).append(" is port (\n");
        for (Port p : ci.getPorts()) {
            vhdl.append("\t").append(p.getName()).append(" : ").append(
                    p.getDirection().toString()).append(" ").append(
                    p.getType().getTypeName());
            if (p.getType().getRange().isVector())
                vhdl.append("(").append(p.getType().getRange().getFrom())
                        .append(" ").append(
                                p.getType().getRange().getDirection()).append(
                                " ").append(p.getType().getRange().getTo())
                        .append(")");
            vhdl.append(";\n");
        }
        vhdl.deleteCharAt(vhdl.length() - 2) // deleting last ';'
                .append(");\n");
    }

}
