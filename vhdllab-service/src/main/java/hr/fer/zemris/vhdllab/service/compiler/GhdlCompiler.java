package hr.fer.zemris.vhdllab.service.compiler;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.results.CompilationMessage;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.MessageType;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.service.Compiler;
import hr.fer.zemris.vhdllab.service.FileManager;
import hr.fer.zemris.vhdllab.service.LibraryFileManager;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.ServiceManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * A compiler wrapper for GHDL.
 *
 * @author marcupic
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class GhdlCompiler implements Compiler {

    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(GhdlCompiler.class);

    /**
     * Property name for path to GHDL executable file.
     */
    private static final String EXECUTABLE_PROPERTY = "compiler.executable";
    /**
     * Property name for path to temporary root compilation directory.
     */
    private static final String TMPDIR_PROPERTY = "compiler.tmpDir";

    /**
     * Path to GHDL executable.
     */
    private String executable;
    /**
     * Temporary root compilation directory.
     */
    private java.io.File tmpRootDir;

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.service.Functionality#configure(java.util.Properties)
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
        String dir = properties.getProperty(TMPDIR_PROPERTY);
        if (dir == null) {
            throw new IllegalArgumentException(
                    "No temporary root compilation directory defined!");
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
     * @see hr.fer.zemris.vhdllab.service.Compiler#execute(hr.fer.zemris.vhdllab.entities.File)
     */
    @Override
    public CompilationResult execute(File file) throws ServiceException {
        ServiceManager man = ServiceContainer.instance().getServiceManager();
        Hierarchy hierarchy = man.extractHierarchy(file.getProject());
        List<String> names = new ArrayList<String>();
        orderFiles(hierarchy, names, file.getName());
        return compile(names, file);
    }

    private void orderFiles(Hierarchy hierarchy, List<String> names, String n) {
        Set<String> dependencies = hierarchy.getDependenciesForFile(n);
        if (!dependencies.isEmpty()) {
            for (String s : dependencies) {
                orderFiles(hierarchy, names, s);
            }
        }
        if (!names.contains(n)) {
            names.add(n);
        }
    }

    private CompilationResult compile(List<String> names, File compileFile) {
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
            Long projectId = compileFile.getProject().getId();
            for (String n : names) {
                copyFile(n, tmpDir, projectId);
            }
            // STEP 3: prepare compiler call
            // -----------------------------------------------------------
            List<String> cmdList = new ArrayList<String>(names.size() + 2);
            cmdList.add(executable);
            cmdList.add("-a");
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
                return new CompilationResult(Integer.valueOf(retVal), false,
                        listToCompilationMessages(errors));
            }
            return new CompilationResult(Integer.valueOf(retVal), true,
                    listToCompilationMessages(errors));
        } catch (Throwable tr) {
            tr.printStackTrace();
        } finally {
            if (tmpDir != null) {
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
        return new CompilationResult(Integer.valueOf(-1), false,
                messageToCompilationMessage("Error running compiler."));
    }

    private List<CompilationMessage> listToCompilationMessages(
            List<String> errors) {
        List<CompilationMessage> list = new ArrayList<CompilationMessage>(
                errors.size());
        for (String e : errors) {
            String[] line = parseGHDLErrorMessage(e);
            if (line.length == 4) {
                list.add(new CompilationMessage(MessageType.ERROR, line[0],
                        line[3], Integer.parseInt(line[1]), Integer
                                .parseInt(line[2])));
            } else {
                list.add(new CompilationMessage(MessageType.ERROR, "", line[1],
                        1, 1));
            }
        }
        return list;
    }

    private List<CompilationMessage> messageToCompilationMessage(String error) {
        List<CompilationMessage> list = new ArrayList<CompilationMessage>(1);
        list.add(new CompilationMessage(MessageType.ERROR, error, 1, 1));
        return list;
    }

    private String[] parseGHDLErrorMessage(String msg) {
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

    private void copyFile(String name, java.io.File destDir, Long projectId)
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
                Long libraryId = container.getLibraryManager()
                        .getPredefinedLibrary().getId();
                LibraryFileManager libFileMan = container
                        .getLibraryFileManager();
                LibraryFile src = libFileMan.findByName(libraryId, name);
                content = src.getContent();
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
