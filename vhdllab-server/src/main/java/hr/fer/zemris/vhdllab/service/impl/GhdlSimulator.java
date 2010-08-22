package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.TimeScale;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchParserException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.Testbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.TestbenchParser;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.service.Simulator;
import hr.fer.zemris.vhdllab.service.WorkspaceService;
import hr.fer.zemris.vhdllab.service.exception.CompilationException;
import hr.fer.zemris.vhdllab.service.exception.NoAvailableProcessException;
import hr.fer.zemris.vhdllab.service.exception.SimulationException;
import hr.fer.zemris.vhdllab.service.exception.SimulatorTimeoutException;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.hierarchy.HierarchyNode;
import hr.fer.zemris.vhdllab.service.result.CompilationMessage;
import hr.fer.zemris.vhdllab.service.result.Result;
import hr.fer.zemris.vhdllab.service.util.SecurityUtils;
import hr.fer.zemris.vhdllab.util.IOUtil;
import hr.fer.zemris.vhdllab.util.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.UnhandledException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A wrapper for GHDL (http://ghdl.free.fr/).
 * 
 * @author marcupic
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class GhdlSimulator extends ServiceSupport implements Simulator {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(GhdlSimulator.class);

    private static final int PROCESS_TIMEOUT = 2000;
    private static final int MAX_SIMULTANEOUS_PROCESSES = 20;
    private static final int ACQUIRE_TIMEOUT = 1;
    private static final TimeUnit ACQUIRE_TIME_UNIT = TimeUnit.SECONDS;

    private static final String PROP_COMPILATION_CMD = "compilation.cmd";
    private static final String PROP_SIMULATION_CMD = "simulation.cmd";

    private final Semaphore semaphore = new Semaphore(MAX_SIMULTANEOUS_PROCESSES);

    private boolean leaveSimulationResults = true;

    private Properties properties;

    @Autowired
    private WorkspaceService workspaceService;

    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("ghdl.properties");
            try {
                properties.load(is);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            IOUtils.closeQuietly(is);
        }

        return properties;
    }

    @Override
    public List<CompilationMessage> compile(Integer fileId) throws CompilationException {
        try {
            if (!semaphore.tryAcquire(ACQUIRE_TIMEOUT, ACQUIRE_TIME_UNIT)) {
                throw new NoAvailableProcessException(MAX_SIMULTANEOUS_PROCESSES);
            }
        } catch (InterruptedException e) {
            throw new NoAvailableProcessException(e);
        }
        List<CompilationMessage> result = compileImpl(fileId);
        semaphore.release();
        return result;
    }

    @Override
    public Result simulate(Integer fileId) throws SimulationException {
        try {
            if (!semaphore.tryAcquire(ACQUIRE_TIMEOUT, ACQUIRE_TIME_UNIT)) {
                throw new NoAvailableProcessException(MAX_SIMULTANEOUS_PROCESSES);
            }
        } catch (InterruptedException e) {
            throw new NoAvailableProcessException(e);
        }
        Result result = simulateImpl(fileId);
        semaphore.release();
        return result;
    }

    private List<CompilationMessage> compileImpl(Integer fileId) {
        SimulationContext context = createContext(fileId);
        context.compileOnly = true;

        try {
            doCompile(context);
        } catch (IOException e) {
            throw new CompilationException(e);
        }

        return listToCompilationMessages(context.result);
    }

    private Result simulateImpl(Integer fileId) {
        SimulationContext context = createContext(fileId);

        String waveform;
        try {
            doCompile(context);

            prepairSimulationCommandLine(context);
            context.result = executeProcess(context.commandLine, context.tempDirectory);
            waveform = extractWaveform(context);
        } catch (IOException e) {
            throw new CompilationException(e);
        } finally {
            if (!leaveSimulationResults) {
                FileUtils.deleteQuietly(context.tempDirectory);
            }
        }

        return new Result(waveform, context.result);
    }

    @SuppressWarnings("synthetic-access")
    private SimulationContext createContext(Integer fileId) {
        SimulationContext context = new SimulationContext();
        context.targetFile = loadFile(fileId);
        return context;
    }

    private void doCompile(SimulationContext context) throws IOException {
        try {
            context.tempDirectory = createTempDirectory(context.targetFile.getId());
            context.dependencies = orderFileNames(context.targetFile);

            copyFiles(context.dependencies, context.targetFile.getProject().getId(), context.tempDirectory);

            context.commandLine = prepairCompilationCommandLine(context.dependencies);
            context.result = executeProcess(context.commandLine, context.tempDirectory);
        } finally {
            if (context.compileOnly) {
                FileUtils.deleteQuietly(context.tempDirectory);
            }
        }
    }

    private List<String> orderFileNames(File file) {
        Hierarchy hierarchy = workspaceService.extractHierarchy(file.getProject().getId());
        List<File> ordered = new ArrayList<File>();
        orderFileNames(ordered, hierarchy, hierarchy.getNode(file));
        List<String> names = new ArrayList<String>(ordered.size());
        for (File f : ordered) {
            names.add(f.getName());
        }
        return names;
    }

    private void orderFileNames(List<File> ordered, Hierarchy hierarchy, HierarchyNode node) {
        Set<HierarchyNode> dependencies = hierarchy.getDependenciesFor(node);
        for (HierarchyNode n : dependencies) {
            orderFileNames(ordered, hierarchy, n);
        }
        if (!ordered.contains(node.getFile())) {
            ordered.add(node.getFile());
        }
    }

    private java.io.File createTempDirectory(Integer fileId) throws IOException {
        String suffix = "_by_user-" + SecurityUtils.getUser() + "__file_id-" + fileId;
        java.io.File tempFile = java.io.File.createTempFile("ghd", suffix, null);
        java.io.File tempDir = new java.io.File(tempFile.getParentFile(), "DIR" + tempFile.getName());
        FileUtils.forceMkdir(tempDir);
        FileUtils.deleteQuietly(tempFile);
        LOG.debug("Created temp directory: " + tempDir.getPath());
        return tempDir;
    }

    private void copyFiles(List<String> dependencies, Integer projectId, java.io.File tempDirectory) throws IOException {
        for (String dep : dependencies) {
            File depFile = findProjectOrPredefinedFile(projectId, dep);
            String data = depFile.getData();
            if (!depFile.getType().equals(FileType.PREDEFINED)) {
                data = metadataExtractor.generateVhdl(depFile).getData();
            }
            java.io.File fileOnDisk = new java.io.File(tempDirectory, dep + ".vhdl");
            FileUtils.writeStringToFile(fileOnDisk, data, IOUtil.DEFAULT_ENCODING);
        }
    }

    private CommandLine createCommandLine(String cmd) {
        return CommandLine.parse(cmd);
    }

    private CommandLine prepairCompilationCommandLine(List<String> dependencies) {
        String cmd = getProperties().getProperty(PROP_COMPILATION_CMD);

        String[] files = new String[dependencies.size()];
        for (int i = 0; i < dependencies.size(); i++) {
            files[i] = dependencies.get(i) + ".vhdl";
        }
        cmd = cmd.replace("{files}", StringUtils.join(files, ' '));

        return createCommandLine(cmd);
    }

    private void prepairSimulationCommandLine(SimulationContext context) {
        String cmd = getProperties().getProperty(PROP_SIMULATION_CMD);
        cmd = cmd.replace("{files}", context.targetFile.getName());

        CommandLine cl = createCommandLine(cmd);
        cl.addArgument("--vcd=simout.vcd");

        Testbench tb = null;
        try {
            tb = TestbenchParser.parseXml(context.targetFile.getData());
        } catch (UniformTestbenchParserException e) {
            throw new SimulationException(e);
        }
        long simulationLength = tb.getSimulationLength();
        if (simulationLength <= 0) {
            simulationLength = 100;
        } else {
            simulationLength = simulationLength / TimeScale.getMultiplier(tb.getTimeScale());
            simulationLength = (long) (simulationLength * 1.1);
        }
        cl.addArgument("--stop-time=" + simulationLength + tb.getTimeScale().toString().toLowerCase());

        context.commandLine = cl;
    }

    private List<String> executeProcess(CommandLine cl, java.io.File tempDirectory) throws ExecuteException,
            IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Executing process: " + cl.toString());
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ExecuteStreamHandler handler = new PumpStreamHandler(bos);
        ExecuteWatchdog watchdog = new ExecuteWatchdog(PROCESS_TIMEOUT);
        Executor executor = new DefaultExecutor();
        executor.setWorkingDirectory(tempDirectory);
        executor.setWatchdog(watchdog);
        executor.setStreamHandler(handler);
        /*
         * It seems that when ExecuteWatchdog terminates process by invoking
         * destroy method, process terminates with exit code 143. And since we
         * manually ask watchdog if he killed the process, exit code 143 is
         * marked as successful (just so our code can be executed).
         * 
         * Exit code 1 in case of compilation error(s).
         */
        executor.setExitValues(new int[] { 0, 1, 143 });
        try {
            executor.execute(cl);
        } catch (ExecuteException e) {
            LOG.warn("Process output dump:\n" + bos.toString());
            throw e;
        }

        if (watchdog.killedProcess()) {
            throw new SimulatorTimeoutException(PROCESS_TIMEOUT);
        }
        String output;
        try {
            output = bos.toString(IOUtil.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new UnhandledException(e);
        }
        if (StringUtils.isBlank(output)) {
            return Collections.emptyList();
        }
        return Arrays.asList(StringUtil.splitToNewLines(output));
    }

    private List<CompilationMessage> listToCompilationMessages(List<String> errors) {
        List<CompilationMessage> list = new ArrayList<CompilationMessage>(errors.size());
        for (String e : errors) {
            String[] line = parseGHDLErrorMessage(e);
            if (line.length == 4) {
                list
                        .add(new CompilationMessage(line[0], Integer.parseInt(line[1]), Integer.parseInt(line[2]),
                                line[3]));
            } else {
                list.add(new CompilationMessage(line[1]));
            }
        }
        return list;
    }

    private String[] parseGHDLErrorMessage(String m) {
        String msg = m;
        String[] res = new String[] { msg, null, null, null };
        for (int i = 0; i < 3; i++) {
            int pos = msg.indexOf(':');
            if (pos != -1) {
                res[i] = msg.substring(0, pos);
                msg = msg.substring(pos + 1);
                res[i + 1] = msg;
            } else {
                return new String[] {
                        res[0],
                        (res[1] == null ? "" : res[1]) + (res[2] == null ? "" : ":" + res[2])
                                + (res[3] == null ? "" : ":" + res[3]) };
            }
        }
        if (res[0].toUpperCase().endsWith(".VHDL")) {
            res[0] = res[0].substring(0, res[0].length() - 5);
        } else if (res[0].toUpperCase().endsWith(".VHD")) {
            res[0] = res[0].substring(0, res[0].length() - 4);
        }
        return res;
    }

    private String extractWaveform(SimulationContext context) throws IOException {
        String vcd = FileUtils.readFileToString(new java.io.File(context.tempDirectory, "simout.vcd"),
                IOUtil.DEFAULT_ENCODING);
        VcdParser parser = new VcdParser(StringUtil.splitToNewLines(vcd));
        parser.parse();
        return parser.getResultInString();
    }

    private static class SimulationContext {
        /*
         * Since this is a private class used by simulator exclusively there is
         * no need for getters and setters.
         */
        public boolean compileOnly;
        public java.io.File tempDirectory;
        public File targetFile;
        public List<String> dependencies;
        public CommandLine commandLine;
        public List<String> result;
    }

}
