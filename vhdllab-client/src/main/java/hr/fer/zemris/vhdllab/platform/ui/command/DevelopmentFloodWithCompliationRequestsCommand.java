package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSource;
import hr.fer.zemris.vhdllab.service.Simulator;
import hr.fer.zemris.vhdllab.service.WorkspaceService;
import hr.fer.zemris.vhdllab.service.exception.NoAvailableProcessException;
import hr.fer.zemris.vhdllab.service.exception.SimulatorTimeoutException;
import hr.fer.zemris.vhdllab.service.result.CompilationMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import javax.annotation.Resource;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.apache.commons.lang.UnhandledException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class DevelopmentFloodWithCompliationRequestsCommand extends
        ActionCommand {

    public static final String ID = "floodWithCompliationRequestsCommand";

    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    protected Simulator simulator;
    @Resource(name = "standaloneLocalizationSource")
    protected LocalizationSource localizationSource;

    public DevelopmentFloodWithCompliationRequestsCommand() {
        super(ID);
        setDisplaysInputDialog(true);
    }

    @Override
    protected void doExecuteCommand() {
        logger
                .info("Http client should be configured with MultiThreadedHttpConnectionManager and parameters that should be set are defaultMaxConnectionsPerHost and maxTotalConnections!!!");
        Project project;
        String projectName = "development_compilation_flood_project";
        project = workspaceService.persist(projectName);
        File file = createFile(project);
        try {
            floodWithCompilationRequests(file.getId(), file.getName());
        } finally {
            // cleanup
            workspaceService.deleteProject(project.getId());
        }
    }

    private void floodWithCompilationRequests(final Integer fileId,
            final String name) {
        String input = JOptionPane.showInputDialog("How many?", "30");
        int floodCount = Integer.parseInt(input);

        final List<String> results = Collections
                .synchronizedList(new ArrayList<String>(floodCount));
        final CyclicBarrier barrier = new CyclicBarrier(floodCount);
        List<Thread> threads = new ArrayList<Thread>(floodCount);
        long start = System.currentTimeMillis();
        for (int i = 0; i < floodCount; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        barrier.await();
                    } catch (Exception e) {
                        throw new UnhandledException(e);
                    }
                    logger.info("sending at: " + System.currentTimeMillis());
                    List<CompilationMessage> messages;
                    try {
                        messages = simulator.compile(fileId);
                    } catch (SimulatorTimeoutException e) {
                        String message = localizationSource.getMessage(
                                "simulator.compile.timout",
                                new Object[] { name });
                        messages = Collections
                                .singletonList(new CompilationMessage(message));
                    } catch (NoAvailableProcessException e) {
                        String message = localizationSource.getMessage(
                                "simulator.compile.no.processes",
                                new Object[] { name });
                        messages = Collections
                                .singletonList(new CompilationMessage(message));
                    }

                    if (messages.isEmpty()) {
                        results.add("Successful");
                    } else {
                        results.add(messages.get(0).getText());
                    }
                }
            });
            thread.start();
            threads.add(thread);
        }

        logger.info("waiting for threads to complete...");
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new UnhandledException(e);
            }
        }
        long end = System.currentTimeMillis();
        logger.info("ended in " + (end - start) + " ms");
        showResults(results);
    }

    private void showResults(List<String> results) {
        logger.info(results);
        JList list = new JList(results.toArray());
        JOptionPane.showOptionDialog(null, new JScrollPane(list), "Results",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                null, null);
    }

    private File createFile(Project project) {
        File file = new File("development_file_name", FileType.SOURCE,
                createData());
        file.setProject(project);
        return workspaceService.createFile(project.getId(),
                "development_file_name", FileType.SOURCE, createData())
                .getFile();
    }

    private String createData() {
        StringBuilder sb = new StringBuilder(1000);
        sb.append("library IEEE;\n");
        sb.append("use IEEE.STD_LOGIC_1164.ALL;\n");
        sb.append("\n");
        sb.append("-- note: entity name and resource name must match\n");
        sb.append("ENTITY development_file_name IS PORT (\n");
        sb.append("a : IN STD_LOGIC;\n");
        sb.append("b : IN STD_LOGIC;\n");
        sb.append("f : OUT STD_LOGIC);\n");
        sb.append("end development_file_name;\n");
        sb.append("\n");
        sb.append("ARCHITECTURE arch OF development_file_name IS\n");
        sb.append("\n");
        sb.append("BEGIN\n");
        sb.append("\n");
        sb.append("END arch;\n");
        return sb.toString();
    }

}
