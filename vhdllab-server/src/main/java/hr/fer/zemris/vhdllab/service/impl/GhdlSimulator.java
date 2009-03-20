package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.TimeScale;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchParserException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.Testbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.TestbenchParser;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.service.FileService;
import hr.fer.zemris.vhdllab.service.MetadataExtractionService;
import hr.fer.zemris.vhdllab.service.ProjectService;
import hr.fer.zemris.vhdllab.service.Simulator;
import hr.fer.zemris.vhdllab.service.exception.CompilationException;
import hr.fer.zemris.vhdllab.service.exception.SimulationException;
import hr.fer.zemris.vhdllab.service.exception.SimulatorTimeoutException;
import hr.fer.zemris.vhdllab.service.ghdl.GhdlCompiler;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.hierarchy.HierarchyNode;
import hr.fer.zemris.vhdllab.service.result.CompilationMessage;
import hr.fer.zemris.vhdllab.service.result.Result;
import hr.fer.zemris.vhdllab.service.util.SecurityUtils;
import hr.fer.zemris.vhdllab.util.IOUtil;
import hr.fer.zemris.vhdllab.util.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.UnhandledException;
import org.apache.log4j.Level;
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
    private static final Logger LOG = Logger.getLogger(GhdlCompiler.class);

    private static final int PROCESS_TIMEOUT = 2000;

    private static final String EXECUTABLE_PROPERTY = "executable";
    private static final String COMPILER_PARAMETERS = "compiler.parameters";
    private static final String SIMULATOR_PARAMETERS = "simulator.parameters";
    private static final String TMPDIR_PROPERTY = "tmpDir";

    private String executable;
    private String[] compilerParameters;
    private String[] simulatorParameters;
    private java.io.File tmpRootDir;

    private boolean leaveSimulationResults = true;

    private Properties properties;

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Autowired
    private ProjectService projectService;
    @Autowired
    private FileService fileService;
    @Resource(name = "metadataExtractionService")
    private MetadataExtractionService metadataExtractionService;

    public void configure() {
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
            throw new IllegalArgumentException(
                    "GHDL simulator parameters not defined!");
        }
        simulatorParameters = allParameters.split(" ");
        allParameters = properties.getProperty(COMPILER_PARAMETERS);
        if (allParameters == null) {
            throw new IllegalArgumentException(
                    "GHDL compiler parameters not defined!");
        }
        compilerParameters = allParameters.split(" ");
        String dir = properties.getProperty(TMPDIR_PROPERTY);
        if (dir == null) {
            throw new IllegalArgumentException(
                    "No temporary root simulation directory defined!");
        }
        tmpRootDir = new java.io.File(dir);
        if (!tmpRootDir.exists()) {
            if (LOG.isEnabledFor(Level.WARN)) {
                LOG.warn(tmpRootDir + " doesn't exist and will be created!");
            }
            tmpRootDir.mkdirs();
        }
    }

    @Override
    public List<CompilationMessage> compile(Integer fileId)
            throws CompilationException {
        SimulationContext context = createContext(fileId);
        context.compileOnly = true;

        try {
            doCompile(context);
        } catch (IOException e) {
            throw new CompilationException(e);
        }

        return listToCompilationMessages(context.compilationLines);
    }

    private class SimulationContext {
        /*
         * Since this is a private class used by simulator exclusively there is
         * no need for getters and setters.
         */
        public boolean compileOnly;
        public java.io.File tempDirectory;
        public File targetFile;
        public List<String> dependencies;
        public CommandLine compilerCommandLine;
        public CommandLine simulatorCommandLine;
        public List<String> compilationLines;
        public List<String> simulationLines;

        public void clean() {
            tempDirectory = null;
            targetFile = null;
            dependencies = null;
            compilerCommandLine = null;
            compilationLines = null;
        }
    }

    @SuppressWarnings("synthetic-access")
    private SimulationContext createContext(Integer fileId) {
        SimulationContext context = new SimulationContext();
        context.targetFile = loadFile(fileId);
        return context;
    }

    private List<String> orderFileNames(File file) {
        Hierarchy hierarchy = projectService.extractHierarchy(file.getProject()
                .getId());
        List<File> ordered = new ArrayList<File>();
        orderFileNames(ordered, hierarchy, hierarchy.getNode(file));
        List<String> names = new ArrayList<String>(ordered.size());
        for (File f : ordered) {
            names.add(f.getName());
        }
        return names;
    }

    private void orderFileNames(List<File> ordered, Hierarchy hierarchy,
            HierarchyNode node) {
        Set<HierarchyNode> dependencies = hierarchy.getDependenciesFor(node);
        for (HierarchyNode n : dependencies) {
            orderFileNames(ordered, hierarchy, n);
        }
        if (!ordered.contains(node.getFile())) {
            ordered.add(node.getFile());
        }
    }

    private java.io.File createTempDirectory(Integer fileId) throws IOException {
        String suffix = "_by_user-" + SecurityUtils.getUser() + "__file_id-"
                + fileId;
        java.io.File tempFile = java.io.File.createTempFile("ghd", suffix,
                tmpRootDir);
        java.io.File tempDir = new java.io.File(tmpRootDir, "DIR"
                + tempFile.getName());
        FileUtils.forceMkdir(tempDir);
        FileUtils.deleteQuietly(tempFile);
        return tempDir;
    }

    private void copyFiles(List<String> dependencies, Integer projectId,
            java.io.File tempDirectory) throws IOException {
        for (String dep : dependencies) {
            File depFile = fileService.findByName(projectId, dep);
            String data = depFile.getData();
            if (!depFile.getType().equals(FileType.PREDEFINED)) {
                data = metadataExtractionService.generateVhdl(depFile.getId())
                        .getData();
            }
            java.io.File fileOnDisk = new java.io.File(tempDirectory, dep
                    + ".vhdl");
            FileUtils.writeStringToFile(fileOnDisk, data,
                    IOUtil.DEFAULT_ENCODING);
        }
    }

    private CommandLine prepairCommandLine(String[] arguments,
            List<String> dependencies) {
        CommandLine cl = new CommandLine(executable);
        cl.addArguments(arguments);
        for (String name : dependencies) {
            cl.addArgument(name + ".vhdl");
        }
        return cl;
    }

    private List<String> executeProcess(CommandLine cl,
            java.io.File tempDirectory) throws ExecuteException, IOException {
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
        executor.execute(cl);

        if (watchdog.killedProcess()) {
            throw new SimulatorTimeoutException(
                    "Process exceeded max runtime: " + PROCESS_TIMEOUT);
        }
        String errors;
        try {
            errors = bos.toString(IOUtil.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new UnhandledException(e);
        }
        return Arrays.asList(StringUtil.splitToNewLines(errors));
    }

    protected void doCompile(SimulationContext context) throws IOException {
        try {
            context.tempDirectory = createTempDirectory(context.targetFile
                    .getId());

            context.dependencies = orderFileNames(context.targetFile);

            copyFiles(context.dependencies, context.targetFile.getProject()
                    .getId(), context.tempDirectory);

            context.compilerCommandLine = prepairCommandLine(
                    compilerParameters, context.dependencies);

            context.compilationLines = executeProcess(
                    context.compilerCommandLine, context.tempDirectory);
        } finally {
            if (context.compileOnly) {
                FileUtils.deleteQuietly(context.tempDirectory);
                context.clean();
            }
        }
    }

    private List<CompilationMessage> listToCompilationMessages(
            List<String> errors) {
        List<CompilationMessage> list = new ArrayList<CompilationMessage>(
                errors.size());
        for (String e : errors) {
            String[] line = parseGHDLErrorMessage(e);
            if (line.length == 4) {
                list
                        .add(new CompilationMessage(line[0], Integer
                                .parseInt(line[1]), Integer.parseInt(line[2]),
                                line[3]));
            } else {
                list.add(new CompilationMessage(null, 1, 1, line[1]));
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
                        (res[1] == null ? "" : res[1])
                                + (res[2] == null ? "" : ":" + res[2])
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

    @Override
    public Result simulate(Integer fileId) throws SimulationException {
        SimulationContext context = createContext(fileId);

        String waveform;
        try {
            doCompile(context);
            prepairSimulationCommandLine(context);
            context.simulationLines = executeProcess(
                    context.simulatorCommandLine, context.tempDirectory);
            waveform = extractWaveform(context);
        } catch (IOException e) {
            throw new CompilationException(e);
        } finally {
            if (!leaveSimulationResults) {
                FileUtils.deleteQuietly(context.tempDirectory);
            }
        }

        return new Result(waveform, context.simulationLines);
    }

    private String extractWaveform(SimulationContext context)
            throws IOException {
        String vcd = FileUtils.readFileToString(new java.io.File(
                context.tempDirectory, "simout.vcd"), IOUtil.DEFAULT_ENCODING);
        VcdParser parser = new VcdParser(StringUtil.splitToNewLines(vcd));
        parser.parse();
        return parser.getResultInString();
    }

    private void prepairSimulationCommandLine(SimulationContext context) {
        CommandLine cl = new CommandLine(executable);
        cl.addArguments(simulatorParameters);
        cl.addArgument(context.targetFile.getName());
        cl.addArgument("--vcd=simout.vcd");
        cl.addArgument("--stop-delta=1000");

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
            simulationLength = simulationLength
                    / TimeScale.getMultiplier(tb.getTimeScale());
            simulationLength = (long) (simulationLength * 1.1);
        }
        cl.addArgument("--stop-time=" + simulationLength
                + tb.getTimeScale().toString().toLowerCase());

        context.simulatorCommandLine = cl;
    }

}
