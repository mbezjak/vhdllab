package hr.fer.zemris.vhdllab.platform.gui.dialog.run;

import hr.fer.zemris.vhdllab.platform.gui.dialog.AbstractDialog;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSource;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class RunDialog extends AbstractDialog<FileIdentifier> {

    private static final long serialVersionUID = 1L;

    private static final String CANCEL_MESSAGE = "dialog.cancel";
    private static final String TESTBENCH_TITLE = "dialog.run.testbench.title";
    private static final String TESTBENCH_DESCRIPTION = "dialog.run.testbench.description";
    private static final String TESTBENCH_OK_MESSAGE = "dialog.run.testbench.ok";
    private static final String COMPILATION_TITLE = "dialog.run.compilation.title";
    private static final String COMPILATION_DESCRIPTION = "dialog.run.compilation.description";
    private static final String COMPILATION_OK_MESSAGE = "dialog.run.compilation.ok";
    private static final String SIMULATION_TITLE = "dialog.run.simulation.title";
    private static final String SIMULATION_DESCRIPTION = "dialog.run.simulation.description";
    private static final String SIMULATION_OK_MESSAGE = "dialog.run.simulation.ok";

    /** Size of a border */
    private static final int BORDER = 10;
    /** Width of this dialog */
    private static final int DIALOG_WIDTH = 300;
    /** Height of this dialog */
    private static final int DIALOG_HEIGHT = 400;
    /** Width of all buttons */
    private static final int BUTTON_WIDTH = 100;
    /** Height of all buttons */
    private static final int BUTTON_HEIGHT = 24;

    /** A model for fileList */
    DefaultListModel listModel;

    public RunDialog(LocalizationSource source, RunContext context) {
        super(source);

        // setup file listModel
        listModel = new DefaultListModel();
        final JList fileList = new JList(listModel);
        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int selectedIndex = fileList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        RunItem item = (RunItem) listModel.get(selectedIndex);
                        setResult(item.asIdentifier());
                    }
                } else if (e.getClickCount() == 2) {
                    setVisible(false);
                }
            }
        });

        JPanel listPanel = new JPanel(new BorderLayout());
        TitledBorder listBorder = BorderFactory
                .createTitledBorder(getDescription(source, context));
        Border outsideBorder = BorderFactory.createEmptyBorder(BORDER, BORDER,
                BORDER, BORDER);
        listPanel.setBorder(BorderFactory.createCompoundBorder(outsideBorder,
                listBorder));
        JScrollPane scroll = new JScrollPane(fileList);
        listPanel.add(scroll, BorderLayout.CENTER);

        // setup ok and cancel buttons
        JButton ok = new JButton(getOkMessage(source, context));
        ok.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        JButton cancel = new JButton(source.getMessage(CANCEL_MESSAGE));
        cancel.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setResult(null);
                setVisible(false);
            }
        });

        Box actionBox = Box.createHorizontalBox();
        actionBox.add(ok);
        actionBox
                .add(Box.createRigidArea(new Dimension(BORDER, BUTTON_HEIGHT)));
        actionBox.add(cancel);
        actionBox.setBorder(BorderFactory.createEmptyBorder(BORDER, 0, BORDER,
                0));

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.add(actionBox, BorderLayout.EAST);

        this.setLayout(new BorderLayout());
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(listPanel, BorderLayout.CENTER);
        messagePanel.add(actionPanel, BorderLayout.SOUTH);
        this.getContentPane().add(messagePanel, BorderLayout.CENTER);
        this.getRootPane().setDefaultButton(ok);
        this.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        setTitle(getTitle(source, context));
    }

    private String getTitle(LocalizationSource source, RunContext context) {
        switch (context) {
        case TESTBENCH:
            return source.getMessage(TESTBENCH_TITLE);
        case COMPILATION:
            return source.getMessage(COMPILATION_TITLE);
        case SIMULATION:
            return source.getMessage(SIMULATION_TITLE);
        default:
            throw new IllegalStateException("Unknown run context: " + context);
        }
    }

    private String getDescription(LocalizationSource source, RunContext context) {
        switch (context) {
        case TESTBENCH:
            return source.getMessage(TESTBENCH_DESCRIPTION);
        case COMPILATION:
            return source.getMessage(COMPILATION_DESCRIPTION);
        case SIMULATION:
            return source.getMessage(SIMULATION_DESCRIPTION);
        default:
            throw new IllegalStateException("Unknown run context: " + context);
        }
    }

    private String getOkMessage(LocalizationSource source, RunContext context) {
        switch (context) {
        case TESTBENCH:
            return source.getMessage(TESTBENCH_OK_MESSAGE);
        case COMPILATION:
            return source.getMessage(COMPILATION_OK_MESSAGE);
        case SIMULATION:
            return source.getMessage(SIMULATION_OK_MESSAGE);
        default:
            throw new IllegalStateException("Unknown run context: " + context);
        }
    }

    public void setRunFiles(List<FileIdentifier> files) {
        for (FileIdentifier identifier : files) {
            listModel.addElement(new RunItem(identifier));
        }
    }

    private class RunItem {

        private String projectName;
        private String fileName;

        public RunItem(FileIdentifier identifier) {
            this.projectName = identifier.getProjectName();
            this.fileName = identifier.getFileName();
        }

        public String getFileName() {
            return fileName;
        }

        public String getProjectName() {
            return projectName;
        }

        public FileIdentifier asIdentifier() {
            return new FileIdentifier(projectName, fileName);
        }

        @Override
        public String toString() {
            return fileName + " [" + projectName + "]";
        }

    }

}
