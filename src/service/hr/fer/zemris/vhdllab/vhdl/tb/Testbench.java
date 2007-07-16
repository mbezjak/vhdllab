package hr.fer.zemris.vhdllab.vhdl.tb;

import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.generator.IVHDLGenerator;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;
import hr.fer.zemris.vhdllab.vhdl.model.StringFormat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains various static method for Testbench VHDL
 * generation.
 * 
 * @author Miro Bezjak
 */
public class Testbench implements IVHDLGenerator {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.service.generator.IVHDLGenerator#generateVHDL(hr.fer.zemris.vhdllab.model.File, hr.fer.zemris.vhdllab.service.VHDLLabManager)
	 */
	public String generateVHDL(File f, VHDLLabManager labman) throws ServiceException {
		if(f == null) throw new NullPointerException("File can not be null.");
		if(labman == null) throw new NullPointerException("VHDLLabManager can not be null.");
		if(!f.getFileType().equals(FileTypes.FT_VHDL_TB)) throw new IllegalArgumentException("");

		String content = f.getContent();
		int start = content.indexOf("<file>") + "<file>".length();
		int end = content.indexOf("</file>", start);
		String name = content.substring(start, end);
		File source = labman.findFileByName(f.getProject().getId(), name);
		CircuitInterface ci = labman.extractCircuitInterface(source);
		String vhdl = null;
		try {
			Generator gen = new DefaultGenerator(f.getContent());
			vhdl = writeVHDL(ci, gen, f.getFileName());
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		return vhdl;
	}
	
	/**
	 * Create a testbench VHDL from circuit interface and generator.
	 * 
	 * @param ci a circuit interface.
	 * @param generator a generator representing inducement.
	 * @return a string representing testbench VHDL.
	 * @throws NullPointerException if <code>ci</code> or <code>generator</code> is <code>null</code>.
	 * @throws ParseException if inducement is not of correct format.
	 * @throws IncompatibleDataException if <code>ci</code> is not compatible with <code>generator</code>.
	 * @see CircuitInterface
	 * @see CircuitInterface#isCompatible(Generator)
	 * @see Generator
	 */
	private String writeVHDL(CircuitInterface ci, Generator generator, String sourceName) throws ParseException, IncompatibleDataException {
		if( ci == null ) throw new NullPointerException("Circuit interface can not be null.");
		if( generator == null ) throw new NullPointerException("Generator can not be null.");
		if( !generator.isCompatible(ci) ) throw new IncompatibleDataException("Circuit Interface's ports are not compatible with Generator's signals.");
		
		StringBuilder vhdl = new StringBuilder( generator.toString().length() );
		vhdl.append("library IEEE;\n")
			.append("use IEEE.STD_LOGIC_1164.ALL;\n")
			.append("\n")
			.append("entity ").append(sourceName).append(" is \n")
			.append("end ").append(sourceName).append(";\n")
			.append("\n")
			.append("architecture structural of ").append(sourceName).append(" is \n");
		populateComponent(ci, vhdl);
		vhdl.append("end component;\n")
			.append("\n");
		populateSignals(ci, vhdl);
		vhdl.append("begin\n");
		populatePortMap(ci, vhdl);
		vhdl.append("\n")
			.append("\tprocess")
			.append("\n")
			.append("\tbegin")
			.append("\n");
		populateProcess(ci, generator, vhdl);
		vhdl.append("\n")
			.append("\tend process;\n")
			.append("end structural;");
		return vhdl.toString();
	}

	/**
	 * Create a testbench signal name from a given signal name.
	 * Testbench signal name will have the following format:
	 * <blockquote>
	 * tb_'signalName'
	 * </blockquote>
	 * 
	 * @param name a name of a signal.
	 * @return a testbench signal name.
	 */
	private String getTestbenchSignal(String name) {
		return "tb_" + name;
	}
	
	/**
	 * Populate a component in testbench VHDL.
	 * 
	 * @param ci a circuit interface from where to draw information.
	 * @param vhdl a string builder where to store testbench VHDL.
	 */
	private void populateComponent(CircuitInterface ci, StringBuilder vhdl) {
		vhdl.append("component ").append(ci.getEntityName()).append(" is port (\n");
		for(Port p : ci.getPorts()) {
			vhdl.append("\t").append(p.getName()).append(" : ")
				.append(p.getDirection().toString()).append(" ")
				.append(p.getType().getTypeName());
			if( p.getType().isVector() )
				vhdl.append("(").append(p.getType().getRangeFrom())
					.append(" ").append(p.getType().getVectorDirection()).append(" ")
					.append(p.getType().getRangeTo()).append(")");
			vhdl.append(";\n");
		}
		vhdl.deleteCharAt(vhdl.length()-2) //deleting last ';'
			.append(");\n");
	}
	
	/**
	 * Populate a signal list in testbench VHDL.
	 * 
	 * @param ci a circuit interface from where to draw information.
	 * @param vhdl a string builder where to store testbench VHDL.
	 */
	private void populateSignals(CircuitInterface ci, StringBuilder vhdl) {
		for(Port p : ci.getPorts()) {
			vhdl.append("\tsignal ").append(getTestbenchSignal(p.getName())).append(" : ")
				.append(p.getType().getTypeName());
			if( p.getType().isVector() )
				vhdl.append("(").append(p.getType().getRangeFrom())
					.append(" ").append(p.getType().getVectorDirection()).append(" ")
					.append(p.getType().getRangeTo()).append(")");
			vhdl.append(";\n");
		}
	}
	
	/**
	 * Populate a port map in testbench VHDL.
	 * 
	 * @param ci a circuit interface from where to draw information.
	 * @param vhdl a string builder where to store testbench VHDL.
	 */
	private void populatePortMap(CircuitInterface ci, StringBuilder vhdl) {
		vhdl.append("uut: ").append(ci.getEntityName()).append(" port map ( ");
		for(Port p : ci.getPorts()) {
			vhdl.append(getTestbenchSignal(p.getName())).append(", ");
		}
		vhdl.deleteCharAt(vhdl.length()-2); //deleting last ','
		vhdl.append(");\n");
	}
	
	/**
	 * Populate a process in testbench VHDL.
	 * 
	 * @param ci a circuit interface from where to draw information.
	 * @param gen a generator from where to draw information.
	 * @param vhdl a string builder where to store testbench VHDL.
	 */
	private void populateProcess(CircuitInterface ci, Generator gen, StringBuilder vhdl) {
		List<Slot> list = createProcessList(ci, gen);
		long time = -1;
		for(Slot slot : list) {
			if( time != -1 ) {
				vhdl.append("\t\twait for ").append(slot.getTime() - time).append(" ")
					.append(gen.getMeasureUnit()).append(";\n\n");
			}
			for(ImpulseWithSignalName imp : slot.getList()) {
				String state = imp.getImpulse().getState();
				vhdl.append("\t\t").append(getTestbenchSignal(imp.getSignalName()))
					.append(" <= ");
				if( ci.getPort(imp.getSignalName()).getType().isScalar() )
					vhdl.append("\'").append(state).append("\';\n");
				else
					vhdl.append("\"").append(state).append("\";\n");
			}
			time = slot.getTime();
		}
		vhdl.append("\t\twait;\n");
	}
	
	/**
	 * Create a process list. This method will create a list of Slot from
	 * where method populateProcess(CircuitInterface, Generator, StringBuilder)
	 * will draw information regarding signal changes written in Generator.
	 * 
	 * @param ci a circuit interface from where to draw information.
	 * @param gen a generator from where to draw information.
	 */
	@SuppressWarnings("unchecked")
	private List<Slot> createProcessList(CircuitInterface ci, Generator gen) {
		Slot tmp = new Slot(0);
		Map<Slot, Slot> table = new HashMap<Slot, Slot>();
		for(Signal s : gen.getSignals()) {
			if( !ci.getPort(s.getName()).getDirection().isIN() ) continue;
			for(Impulse i : s.getExciter()) {
				tmp.setTime(i.getTime());
				Slot list = table.get(tmp);
				if( list == null ) {
					list = new Slot(tmp.getTime());
					table.put(list, list);
				}
				ImpulseWithSignalName imp = new ImpulseWithSignalName(s.getName(), i);
				list.addImpulseWithSignalName(imp);
			}
		}
		Collection<Slot> c = table.values();
		List<Slot> l = new ArrayList<Slot>(c);
		Collections.sort(l);
		return l;
	}
	
	/**
	 * This class contains information regarding each signal change in a cirtain
	 * time. It contains of time and a list of ImpulseWithSignalName structure.
	 * 
	 * <h3>Example</h3>
	 * 
	 * Here is an example of testbench inducement:
	 * <blockquote>
	 * &lt;measureUnit&gt;ns&lt;/measureUnit&gt;<br>
	 * &lt;duration>1000&lt;/duration&gt;<br>
	 * &lt;signal name="A" type="scalar"&gt;(0,0)(100,1)(150,0)(300,1)&lt;/signal&gt;<br>
	 * &lt;signal name="b" type="scalar"&gt;(0,0)(200,1)(300,z)(440,U)&lt;/signal&gt;<br>
	 * &lt;signal name="c" type="scalar" rangeFrom="0" rangeTo="0"&gt;(0,0)(50,1)(300,0)(400,1)&lt;/signal&gt;<br>
	 * &lt;signal name="d" type="vector" rangeFrom="0" rangeTo="0"&gt;(100,1)(200,0)(300,1)(400,z)&lt;/signal&gt;<br>
	 * </blockquote>
	 * In this example this class will contain information that in time 0
	 * signals 'A', 'b' and 'c' changed their state to '0'. This is true
	 * for every other signal change in this inducement.
	 * 
	 * <h3>Restrictions</h3>
	 * 
	 * Time will be non-negative number and a list of ImpulseWithSignalName
	 * will not contain two {@link Testbench.ImpulseWithSignalName#equals(Object)
	 * equal} ImpulseWithSignalName.
	 * 
	 * @author Miro Bezjak
	 * @see ImpulseWithSignalName
	 */
	private class Slot implements Comparable<Slot> {
		
		private long time;
		private List<ImpulseWithSignalName> list = new ArrayList<ImpulseWithSignalName>();
		private List<ImpulseWithSignalName> list_ro = Collections.unmodifiableList(list);
		
		/**
		 * Construct an instance of this class using a time to explain a Slot.
		 * A time must be a non-negative number.
		 * 
		 * @param time a time of signal change.
		 * @throws IllegalArgumentException if <code>time</code> is null.
		 */
		public Slot(long time) {
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
		 * @param time a time of signal change.
		 * @throws IllegalArgumentException if <code>time</code> is null.
		 */
		public void setTime(long time) {
			if( time < 0 ) throw new IllegalArgumentException("Time can not be negative.");
			this.time = time;
		}

		/**
		 * Getter method for a read-only list of ImpulseWithSignalName.
		 * 
		 * @return a read-only list of ImpulseWithSignalName.
		 */
		public List<ImpulseWithSignalName> getList() {
			return list_ro;
		}
		
		/**
		 * Append an ImpulseWithSignalName to the end of this list.
		 * 
		 * @param i an ImpulseWithSignalName to add.
		 * @return <code>true</code> if <code>i</code> has been added; <code>false</code> otherwise.
		 * @throws NullPointerException if <code>i</code> is <code>null</code>.
		 */
		public boolean addImpulseWithSignalName(ImpulseWithSignalName i) {
			if( i == null ) throw new NullPointerException("ImpulseWithSignalName can not be null.");
			if( getList().contains(i) ) return false;
			list.add(i);
			return true;
		}

		/**
		 * Compares this Slot with the specified object for order. Two slots
		 * are compared by time.
		 * 
		 * @param a <code>Slot</code> to be compared.
		 * @return the value 0 if the argument slot is equal to this slot;
		 *         a value less than 0 if this slot has time less than
		 *         the slot argument; and a value greater than 0 if this
		 *         slot has time greater than the slot argument.
		 * @throws ClassCastException if the specified object's type prevents
		 *         it from being compared to this Slot.
		 */
		public int compareTo(Slot other) {
			long res = this.time - other.getTime();
			if(res<0) return -1;
			else if(res>0) return 1;
			else return 0;
		}

		/**
		 * Compares this slot to the specified object. Returns
		 * <code>true</code> if and only if the specified object is also a
		 * <code>Slot</code> and if times are the same.
		 * 
		 * @param o the object to compare this <code>Slot</code> against.
		 * @return <code>true</code> if <code>Slot</code> are equal; <code>false</code> otherwise.
		 */
		@Override
		public boolean equals(Object o) {
			if( !(o instanceof Slot) ) return false;
			Slot other = (Slot) o;
			return this.time == other.getTime();
		}

		/**
		 * Returns a hash code value for this <code>Slot</code> instance.
		 * The hash code of <code>Slot</code> instance is hash code of
		 * time.
		 * <p>
		 * This ensures that <code>s1.equals(s2)</code> implies that 
		 * <code>s1.hashCode() == s2.hashCode()</code> for any two Slots, 
		 * <code>s1</code> and <code>s2</code>, as required by the general 
		 * contract of <code>Object.hashCode</code>.
		 * 
		 * @return a hash code value for this <code>Slot</code> instance.
		 * @see java.lang.String#hashCode()
		 * @see java.lang.List#hashCode()
		 */
		@Override
		public int hashCode() {
			return Long.valueOf(time).hashCode();
		}
	}
	
	/**
	 * This class contains information regarding a signal change in a cirtain
	 * time. It contains of signal name and an impulse.
	 * 
	 * <h3>Example</h3>
	 * 
	 * Here is an example of testbench inducement:
	 * <blockquote>
	 * &lt;measureUnit&gt;ns&lt;/measureUnit&gt;<br>
	 * &lt;duration>1000&lt;/duration&gt;<br>
	 * &lt;signal name="A" type="scalar"&gt;(0,0)(100,1)(150,0)(300,1)&lt;/signal&gt;<br>
	 * &lt;signal name="b" type="scalar"&gt;(0,0)(200,1)(300,z)(440,U)&lt;/signal&gt;<br>
	 * &lt;signal name="c" type="scalar" rangeFrom="0" rangeTo="0"&gt;(0,0)(50,1)(300,0)(400,1)&lt;/signal&gt;<br>
	 * &lt;signal name="d" type="vector" rangeFrom="0" rangeTo="0"&gt;(100,1)(200,0)(300,1)(400,z)&lt;/signal&gt;<br>
	 * </blockquote>
	 * In this example this class will contain information that signal A is
	 * changing its state to a cirtain impulse <i>(0,0)</i>. This is true
	 * for every other signal and impulse in this inducement.
	 * 
	 * <h3>Restrictions</h3>
	 * 
	 * A name must have the following format:
	 * <ul>
	 * <li>it must contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underscore (_) characters
	 * <li>it must not start with a non-alpha character
	 * <li>it must not end with an underscore character
	 * <li>it must not contain an underscore character after an underscore character
	 * </ul>
	 * 
	 * @author Miro Bezjak
	 */
	private class ImpulseWithSignalName {
		
		private String signalName;
		private Impulse impulse;
		
		/**
		 * Construct an instance of this class using a signal name and an impulse to
		 * explain an ImpulseWithSignalName. A signal name must have the following format:
		 * <ul>
		 * <li>it must contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underscore (_) characters
		 * <li>it must not start with a non-alpha character
		 * <li>it must not end with an underscore character
		 * <li>it must not contain an underscore character after an underscore character
		 * </ul>
		 * 
		 * @param name a name of a signal.
		 * @param impulse an impuse to which signal is changing.
		 * @throws NullPointerException if <code>name</code> or an <code>impulse</code> is <code>null</code>.
		 * @throws IllegalArgumentException if <code>name</code> is not of correct format.
		 */
		public ImpulseWithSignalName(String name, Impulse impulse) {
			if( impulse == null ) throw new NullPointerException("Impulse can not be null.");
			if( name == null ) throw new NullPointerException("Signal name can not be null.");
			if( !StringFormat.isCorrectEntityName(name) ) throw new IllegalArgumentException("Signal name is not of correct format.");
			this.signalName = name;
			this.impulse = impulse;
		}

		/**
		 * Getter method for an impulse.
		 * 
		 * @return an impulse.
		 */
		public Impulse getImpulse() {
			return impulse;
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
		 * Compares this ImpulseWithSignalName to the specified object. Returns
		 * <code>true</code> if and only if the specified object is also a
		 * <code>ImpulseWithSignalName</code> and if signal names and impuses
		 * are the same.
		 * 
		 * @param o the object to compare this <code>ImpulseWithSignalName</code> against.
		 * @return <code>true</code> if <code>ImpulseWithSignalName</code> are equal; <code>false</code> otherwise.
		 */
		@Override
		public boolean equals(Object o) {
			if( o == null ) return false;
			if( !(o instanceof ImpulseWithSignalName) ) return false;
			ImpulseWithSignalName other = (ImpulseWithSignalName) o;
			return other.getSignalName().equalsIgnoreCase(this.signalName)
				&& other.getImpulse().equals(this.impulse);
		}
	}

}