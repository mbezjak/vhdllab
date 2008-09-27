package hr.fer.zemris.vhdllab.service.simulator;

import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.results.MessageType;
import hr.fer.zemris.vhdllab.api.results.SimulationMessage;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.TimeScale;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchParserException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.Testbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.TestbenchParser;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.service.FileManager;
import hr.fer.zemris.vhdllab.service.LibraryFileManager;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.ServiceManager;
import hr.fer.zemris.vhdllab.service.Simulator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * A simulator wrapper for GHDL.
 * 
 * @author marcupic
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class GHDLSimulator implements Simulator {

    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(GHDLSimulator.class);

    /**
     * Property name for path to GHDL executable file.
     */
    private static final String EXECUTABLE_PROPERTY = "simulator.executable";
    /**
     * Parameters to a GHDL executable file used for compilation.
     */
    private static final String COMPILER_PARAMETERS = "compiler.parameters";
    /**
     * Parameters to a GHDL executable file used for simulation.
     */
    private static final String SIMULATOR_PARAMETERS = "simulator.parameters";
    /**
     * Property name for path to temporary root simulation directory.
     */
    private static final String TMPDIR_PROPERTY = "simulator.tmpDir";

    /**
     * Path to GHDL executable.
     */
    private String executable;
    /**
     * Parameters to GHDL executable used for compilation.
     */
    private String[] compilerParameters;
    /**
     * Parameters to GHDL executable used for simulation.
     */
    private String[] simulatorParameters;
    /**
     * Temporary root simulation directory.
     */
    private java.io.File tmpRootDir;

    private boolean leaveSimulationResults = true;

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.service.Functionality#configure(java.util.Properties
     * )
     */
    @Override
    public void configure(Properties properties) {
        executable = properties.getProperty(EXECUTABLE_PROPERTY);
        if (executable == null) {
            throw new IllegalArgumentException("GHDL executable not defined!");
        }
        if (!new java.io.File(executable).exists()) {
            throw new IllegalArgumentException(
                    "Specified GHDL executable doesn't exist: " + executable);
        }
        String allParameters = properties.getProperty(SIMULATOR_PARAMETERS);
        if (allParameters == null) {
            throw new IllegalArgumentException("GHDL simulator parameters not defined!");
        }
        simulatorParameters = allParameters.split(" ");
        allParameters = properties.getProperty(COMPILER_PARAMETERS);
        if (allParameters == null) {
            throw new IllegalArgumentException("GHDL compiler parameters not defined!");
        }
        compilerParameters = allParameters.split(" ");
        String dir = properties.getProperty(TMPDIR_PROPERTY);
        if (dir == null) {
            throw new IllegalArgumentException(
                    "No temporary root simulation directory defined!");
        }
        tmpRootDir = new java.io.File(dir);
        if (!tmpRootDir.exists()) {
            if (log.isEnabledFor(Level.WARN)) {
                log.warn(tmpRootDir + " doesn't exist and will be created!");
            }
            tmpRootDir.mkdirs();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.service.Simulator#execute(hr.fer.zemris.vhdllab
     * .entities.File)
     */
    @Override
    public SimulationResult execute(File file) throws ServiceException {
        ServiceManager man = ServiceContainer.instance().getServiceManager();
        Hierarchy hierarchy = man.extractHierarchy(file.getProject());
        List<Caseless> names = new ArrayList<Caseless>();
        orderFiles(hierarchy, names, file.getName());
        return simulate(names, file);
    }

    private void orderFiles(Hierarchy hierarchy, List<Caseless> names, Caseless n) {
        Set<Caseless> dependencies = hierarchy.getDependenciesForFile(n);
        if (!dependencies.isEmpty()) {
            for (Caseless s : dependencies) {
                orderFiles(hierarchy, names, s);
            }
        }
        if (!names.contains(n)) {
            names.add(n);
        }
    }

    public SimulationResult simulate(List<Caseless> names, File simFile) {
        java.io.File tmpFile = null;
        java.io.File tmpDir = null;
        try {
            // STEP 1: create temporary directory
            // -----------------------------------------------------------
            tmpFile = java.io.File.createTempFile("ghd", null, tmpRootDir);
            tmpDir = new java.io.File(tmpRootDir, "DIR" + tmpFile.getName());
            tmpDir.mkdirs();
            // STEP 2: copy all vhdl files there
            // -----------------------------------------------------------
            Integer projectId = simFile.getProject().getId();
            for (Caseless n : names) {
                copyFile(n, tmpDir, projectId);
            }
            // STEP 3: prepare simulator call
            // -----------------------------------------------------------
            List<String> cmdList = new ArrayList<String>(names.size() + 2);
            cmdList.add(executable);
            Collections.addAll(cmdList, compilerParameters);
            for (int i = 0; i < names.size(); i++) {
                cmdList.add(names.get(i) + ".vhdl");
            }
            String[] cmd = new String[cmdList.size()];
            cmdList.toArray(cmd);
            // STEP 4: execute the call
            // -----------------------------------------------------------
            System.out.println("Will execute: " + Arrays.toString(cmd));
            final List<String> errors = new LinkedList<String>();
            Process proc = Runtime.getRuntime().exec(cmd, null, tmpDir);
            InputConsumer consumer = new InputConsumer(proc.getInputStream(),
                    proc.getErrorStream(), errors, errors);
            consumer.waitForThreads();
            int retVal = proc.waitFor();
            if (retVal != 0) {
                return new SimulationResult(Integer.valueOf(retVal), false,
                        listToSimMessages(errors), null);
            }
            // OK, here we have added files into project. Now we have to
            // simulate it.
            // STEP 5: prepare simulator call
            // -----------------------------------------------------------
            
            Testbench tb = null;
            try {
                tb = TestbenchParser.parseXml(simFile.getData());
            } catch (UniformTestbenchParserException e) {
                throw new ServiceException(StatusCodes.SERVICE_CANT_SIMULATE, e);
            }
            long simulationLength = tb.getSimulationLength();
            if(simulationLength <= 0) {
                simulationLength = 100;
            } else {
                simulationLength = simulationLength / TimeScale.getMultiplier(tb.getTimeScale());
                simulationLength = (long) (simulationLength * 1.1); // increase by 10%
            }
            cmdList.clear();
            cmdList.add(executable);
            Collections.addAll(cmdList, simulatorParameters);
            cmdList.add(simFile.getName().toString());
            cmdList.add("--vcd=simout.vcd");
            cmdList.add("--stop-delta=1000");
            cmdList.add("--stop-time=" + simulationLength + tb.getTimeScale().toString().toLowerCase());
            cmd = new String[cmdList.size()];
            cmdList.toArray(cmd);
            // STEP 6: execute the call
            // -----------------------------------------------------------
            errors.clear();
            System.out.println("Will execute: " + Arrays.toString(cmd));
            proc = Runtime.getRuntime().exec(cmd, null, tmpDir);
            consumer = new InputConsumer(proc.getInputStream(), proc
                    .getErrorStream(), errors, errors);
            consumer.waitForThreads();
            retVal = proc.waitFor();
            String waveform = null;
            if (retVal == 0) {
                VcdParser vcd = new VcdParser(new java.io.File(tmpDir,
                        "simout.vcd").getAbsolutePath());
                vcd.parse();
                waveform = vcd.getResultInString();
            }
            return new SimulationResult(Integer.valueOf(retVal), retVal == 0,
                    listToSimMessages(errors), waveform == null ? "" : waveform);
        } catch (Throwable tr) {
            tr.printStackTrace();
        } finally {
            if (tmpDir != null && !leaveSimulationResults) {
                try {
                    recursiveDelete(tmpDir);
                } catch (Exception ignorable) {
                    ignorable.printStackTrace();
                }
            }
            if (tmpFile != null) {
                tmpFile.delete();
            }
        }

        return new SimulationResult(1, false,
                messageToSimMessages("Could not simulate due to exception."),
                "");
    }

    private List<SimulationMessage> listToSimMessages(List<String> errors) {
        List<SimulationMessage> list = new ArrayList<SimulationMessage>(errors
                .size());
        for (String e : errors) {
            list.add(new SimulationMessage(MessageType.ERROR, e));
        }
        return list;
    }

    private List<SimulationMessage> messageToSimMessages(String error) {
        List<SimulationMessage> list = new ArrayList<SimulationMessage>(1);
        list.add(new SimulationMessage(MessageType.ERROR, error));
        return list;
    }

    private void recursiveDelete(java.io.File tmpDir) {
        java.io.File[] children = tmpDir.listFiles();
        if (children != null) {
            for (java.io.File f : children) {
                if (f.isDirectory()) {
                    recursiveDelete(f);
                } else {
                    f.delete();
                }
            }
        }
        tmpDir.delete();
    }

    private void copyFile(Caseless name, java.io.File destDir, Integer projectId)
            throws IOException, ServiceException {
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new java.io.File(destDir, name
                            + ".vhdl")), "UTF-8"));
            ServiceContainer container = ServiceContainer.instance();
            ServiceManager man = container.getServiceManager();
            FileManager fileMan = container.getFileManager();
            String content;
            if (fileMan.exists(projectId, name)) {
                File src = fileMan.findByName(projectId, name);
                content = man.generateVHDL(src).getVHDL();
            } else {
                Integer libraryId = container.getLibraryManager()
                        .getPredefinedLibrary().getId();
                LibraryFileManager libFileMan = container
                        .getLibraryFileManager();
                LibraryFile src = libFileMan.findByName(libraryId, name);
                content = src.getData();
            }
            bw.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IOException("Could not copy file.");
        } finally {
            if (bw != null)
                try {
                    bw.close();
                } catch (Exception ignorable) {
                }
        }
    }

    private static class InputConsumer {
        int runningThreads = 0;

        public InputConsumer(InputStream procOutput, InputStream procError,
                List<String> outputs, List<String> errors) {
            try {
                Thread t1 = consume(procOutput, outputs);
                t1.start();
                Thread t2 = consume(procError, errors);
                t2.start();
            } catch (Exception ignorable) {
            }
        }

        private Thread consume(final InputStream procStream,
                final List<String> list) {
            final Thread t = new Thread(new Runnable() {
                public void run() {
                    if (procStream != null) {
                        try {
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(procStream));
                            while (true) {
                                String line = br.readLine();
                                if (line == null) {
                                    break;
                                }
                                if (line.equals("")) {
                                    continue;
                                }
                                synchronized (list) {
                                    list.add(line);
                                }
                            }
                        } catch (Exception ignorable) {
                        }
                    }
                    synchronized (InputConsumer.this) {
                        runningThreads--;
                        InputConsumer.this.notifyAll();
                    }
                }
            });
            runningThreads++;
            return t;
        }

        public void waitForThreads() {
            synchronized (this) {
                while (runningThreads > 0) {
                    try {
                        this.wait();
                    } catch (Exception ignorable) {
                    }
                }
            }
        }
    }

}
