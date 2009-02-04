package hr.fer.zemris.vhdllab.applets.editor.newtb;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.api.vhdl.PortDirection;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.ChangeStateEdge;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.Radix;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.TimeScale;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.VectorDirection;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchParserException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.help.HelpManager;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.CombinatorialTestbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.SingleClockTestbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.Testbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.TestbenchParser;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.EditableSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.ScalarSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.Signal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.VectorSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.InitTimingDialog;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.components2.JTestbench;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.gui.dialog.run.RunContext;
import hr.fer.zemris.vhdllab.platform.gui.dialog.run.RunDialog;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditor;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.ProjectIdentifier;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TestbenchEditor extends AbstractEditor implements IWizard {

    private static final long serialVersionUID = 1L;

    private Testbench testbench = null;
    private JTestbench jTestbench = null;
    private boolean GUICreated = false;

    private void initTestbench(String xml) {
        try {
            this.testbench = TestbenchParser.parseXml(xml);
        } catch (UniformTestbenchParserException e) {
            e.printStackTrace();
        }
    }

    private void createGUI() {
        this.GUICreated = true;

        this.jTestbench = new JTestbench(this.testbench);

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton zoomInButton = new JButton("Zoom in");
        JButton zoomOutButton = new JButton("Zoom out");
        JButton optimalZoom = new JButton("Optimal zoom");
        final JComboBox radixSelector = new JComboBox(new String[] { "Binary",
                "Decimal", "Hexadecimal" });
        radixSelector.setSelectedItem(this.jTestbench.getTestbenchRadix()
                .toString());
        topLeftPanel.add(zoomInButton);
        topLeftPanel.add(zoomOutButton);
        topLeftPanel.add(optimalZoom);
        topLeftPanel.add(radixSelector);

        JButton helpButton = new JButton("Help");
        topRightPanel.add(helpButton);

        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(topRightPanel, BorderLayout.EAST);

        this.setLayout(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);
        this.add(this.jTestbench, BorderLayout.CENTER);

        jTestbench.setChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setModified(true);
            }
        });

        zoomInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                jTestbench.ZoomIn();
            }
        });

        zoomOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTestbench.ZoomOut();
            }
        });

        optimalZoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTestbench.OptimalZoom();
            }
        });

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HelpManager.openHelpDialog(HelpManager
                        .getHelpCode(TestbenchEditor.class));
            }
        });

        radixSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTestbench.setTestbenchRadix(Radix.valueOf(radixSelector
                        .getSelectedItem().toString()));
            }
        });
    }

    @Override
    protected void doDispose() {
    }

    @Override
    public String getData() {
        if (this.testbench == null) {
            return "";
        }
        return this.testbench.toXml();
    }

    @Override
    public IWizard getWizard() {
        return this;
    }

    @Override
    protected void doInitWithoutData() {
        if (!this.GUICreated) {
            this.createGUI();
        }
    }

    @Override
    protected void doInitWithData(FileInfo f) {
        setModified(false);
        this.initTestbench(f.getData());
        if (!this.GUICreated) {
            this.createGUI();
        }
        this.jTestbench.setModel(this.testbench);
    }

    @Override
    public FileContent getInitialFileContent(Component parent,
            Caseless projectName) {
        ProjectInfo project = getContainer().getMapper().getProject(
                new ProjectIdentifier(projectName));
        List<FileInfo> files = getContainer().getWorkspaceManager()
                .getFilesForProject(project);
        List<FileIdentifier> identifiers = new ArrayList<FileIdentifier>(files
                .size());
        for (FileInfo file : files) {
            if (file.getType().isCompilable()) {
                identifiers
                        .add(new FileIdentifier(projectName, file.getName()));
            }
        }
        RunDialog dialog = new RunDialog(getContainer(), RunContext.TESTBENCH);
        dialog.setRunFiles(identifiers);
        dialog.startDialog();
        FileIdentifier file = dialog.getResult();
        if (file == null) {
            return null;
        }
        if (!projectName.equals(file.getProjectName())) {
            SystemLog.instance().addSystemMessage(
                    "Cant create testbench for file outside of '" + projectName
                            + "'", MessageType.INFORMATION);
            return null;
        }
        // Ovo gore ostaviti
        // Ovo dolje zamijeniti

        FileInfo fileInfo = container.getMapper().getFile(file);
        CircuitInterface ci = container.getCircuitInterfaceExtractor().extract(
                fileInfo);

        String testbench = file.getFileName() + "_tb";

        while (true) {
            testbench = JOptionPane.showInputDialog(parent,
                    "Enter a name of a testbench", testbench);
            if (testbench == null) {
                return null;
            }
            // Provjera dal postoje duplikati
            FileInfo info = getContainer().getMapper().getFile(
                    new FileIdentifier(projectName, new Caseless(testbench)));
            if (info != null) {
                JOptionPane.showMessageDialog(null,
                        "A file with the name you specified already exists.",
                        "Error saving testbench", JOptionPane.ERROR_MESSAGE);
            } else {
                break;
            }
        }

        while (true) {
            InitTimingDialog initTimingDialog = new InitTimingDialog(parent,
                    true, ci, testbench, projectName.toString());
            initTimingDialog.startDialog();
            if (initTimingDialog.getOption() != InitTimingDialog.OK_OPTION)
                return null;

            Testbench tb = null;

            try {
                tb = this.getInitialTestbench(initTimingDialog, file
                        .getFileName().toString());
                this.addSignals(tb, ci);
                return new FileContent(projectName, new Caseless(testbench), tb
                        .toXml());
            } catch (UniformTestbenchException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(),
                        "Error creating testbench", JOptionPane.ERROR_MESSAGE);
                continue;
                // return null;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    private void addSignals(Testbench tb, CircuitInterface ci) {
        Signal s = null;
        for (Port p : ci.getPorts()) {
            try {
                if (p.getDirection().equals(PortDirection.IN)) {
                    if (p.getType().getRange().isScalar()) {
                        s = new ScalarSignal(p.getName());
                    } else {
                        short d = (short) (1 + Math.abs(p.getType().getRange()
                                .getFrom()
                                - p.getType().getRange().getTo()));
                        s = new VectorSignal(p.getName(), d, VectorDirection
                                .valueOf(p.getType().getRange().getDirection()
                                        .toString().toLowerCase()));
                    }
                }
                tb.addSignal((EditableSignal) s);
            } catch (UniformSignalException e) {
                e.printStackTrace();
            } catch (UniformTestbenchException e) {
                e.printStackTrace();
            }
        }
    }

    public Testbench getInitialTestbench(InitTimingDialog initTimingDialog,
            String sourceName) throws UniformTestbenchException {

        Testbench tb = null;
        TimeScale timeScale = TimeScale
                .valueOf(initTimingDialog.getTimeScale());

        if (Math.log(TimeScale.getMultiplier(timeScale))
                + Math.log(initTimingDialog.getInitialLengthOfTestbench()) >= Math
                .log(Long.MAX_VALUE)) {
            throw new UniformTestbenchException(
                    "Testbench length is too large. Change the time scale.");
        }

        long testBenchLength = TimeScale.getMultiplier(timeScale)
                * initTimingDialog.getInitialLengthOfTestbench();

        if (initTimingDialog.isCombinatorial()) {
            CombinatorialTestbench.Properties p = new CombinatorialTestbench.Properties();
            p.assignInputsTime = TimeScale.getMultiplier(timeScale)
                    * initTimingDialog.getAssignInputs();
            p.checkOutputsTime = TimeScale.getMultiplier(timeScale)
                    * initTimingDialog.getCheckOutputs();
            tb = new CombinatorialTestbench(sourceName, testBenchLength,
                    timeScale, p);
        } else {
            SingleClockTestbench.Properties p = new SingleClockTestbench.Properties();
            if (initTimingDialog.isRisingEdgeSelected()) {
                p.changeStateEdge = ChangeStateEdge.rising;
            } else {
                p.changeStateEdge = ChangeStateEdge.falling;
            }
            p.clockSignalName = initTimingDialog.getClockSignal();
            p.clockTimeHigh = TimeScale.getMultiplier(timeScale)
                    * initTimingDialog.getClockTimeHigh();
            p.clockTimeLow = TimeScale.getMultiplier(timeScale)
                    * initTimingDialog.getClockTimeLow();
            p.inputSetupTime = TimeScale.getMultiplier(timeScale)
                    * initTimingDialog.getInputSetupTime();

            tb = new SingleClockTestbench(sourceName, testBenchLength,
                    timeScale, p);
        }

        return tb;
    }
}
