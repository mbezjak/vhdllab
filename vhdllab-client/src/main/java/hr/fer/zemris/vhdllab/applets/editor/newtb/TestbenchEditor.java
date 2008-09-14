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
import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.dialog.RunDialog;
import hr.fer.zemris.vhdllab.applets.main.interfaces.AbstractEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        final JComboBox radixSelector = new JComboBox(new String[] {"Binary", "Decimal", "Hexadecimal"});
        radixSelector.setSelectedItem(this.jTestbench.getTestbenchRadix().toString());
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
                HelpManager.openHelpDialog(HelpManager.getHelpCode(TestbenchEditor.class));
            }
        });
        
        radixSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTestbench.setTestbenchRadix(Radix.valueOf(radixSelector.getSelectedItem().toString()));
            }
        });
    }
    
    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public String getData() {
        if(this.testbench == null) {
            return "";
        } else {
            return this.testbench.toXml();
        }
    }

    @Override
    public IWizard getWizard() {
        return this;
    }

    @Override
    public void init() {
        super.init();
        if(!this.GUICreated) {
            this.createGUI();
        }
    }

    @Override
    public void setFileContent(FileContent content) {
        super.setFileContent(content);
        setModified(false);
        this.initTestbench(content.getContent());
        if(!this.GUICreated) {
            this.createGUI();
        }
        this.jTestbench.setModel(this.testbench);
    }

    @Override
    public FileContent getInitialFileContent(Component parent,
            String projectName) {
        RunDialog dialog = new RunDialog(parent, true, container, RunDialog.COMPILATION_TYPE);
        dialog.setChangeProjectButtonText("change");
        dialog.setTitle("Select a file for which to create testbench");
        dialog.setListTitle("Select a file for which to create testbench");
        dialog.startDialog();
        if(dialog.getOption() != RunDialog.OK_OPTION) return null;
        FileIdentifier file = dialog.getSelectedFile();
        if(!projectName.equalsIgnoreCase(file.getProjectName())) {
            SystemLog.instance().addSystemMessage("Cant create testbench for file outside of '"+projectName+"'", MessageType.INFORMATION);
            return null;
        }
        // Ovo gore ostaviti
        // Ovo dolje zamijeniti
        
        CircuitInterface ci;
        try {
            ci = container.getResourceManager().getCircuitInterfaceFor(file.getProjectName(), file.getFileName());
        } catch (UniformAppletException e) {
            e.printStackTrace();
            return null;
        }
        
        String testbench = file.getFileName() + "_tb";
        
        while(true) {
            testbench = JOptionPane.showInputDialog(parent, "Enter a name of a testbench", testbench);
            if(testbench == null) {
                return null;
            }
            else {
                // Provjera dal postoje duplikati
                try {
                    if(container.getResourceManager().existsFile(projectName, testbench)) {
                        JOptionPane.showMessageDialog(
                                null,
                                "A file with the name you specified already exists.",
                                "Error saving testbench",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        break;
                    }
                } catch (UniformAppletException e) {
                    e.printStackTrace();
                }
            }
        }
        
        while(true) {
            InitTimingDialog initTimingDialog = new InitTimingDialog(parent, true, ci, testbench, projectName);
            initTimingDialog.startDialog();
            if(initTimingDialog.getOption() != RunDialog.OK_OPTION) return null;
            
            Testbench tb = null;
            
            try {
                tb = this.getInitialTestbench(initTimingDialog, file.getFileName());
                this.addSignals(tb, ci);
                return new FileContent(projectName, testbench, tb.toXml());
            }
            catch(UniformTestbenchException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        "Error creating testbench",
                        JOptionPane.ERROR_MESSAGE);
                continue;
                //return null;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
    
    private void addSignals(Testbench tb, CircuitInterface ci) {    
        Signal s = null;
        for(Port p : ci.getPorts()) {
            try {
                if(p.getDirection().equals(PortDirection.IN)) {
                    if(p.getType().getRange().isScalar()) {
                        s = new ScalarSignal(p.getName());
                    }
                    else {
                        short d = (short) (1 + Math.abs(p.getType().getRange().getFrom() - p.getType().getRange().getTo()));
                        s = new VectorSignal(p.getName(), d, VectorDirection.valueOf(p.getType().getRange().getDirection().toString().toLowerCase()));
                    }
                }
                tb.addSignal((EditableSignal) s);
            }
            catch(UniformSignalException e) {
                e.printStackTrace();
            } catch (UniformTestbenchException e) {
                e.printStackTrace();
            }
        }
    }

    public Testbench getInitialTestbench(InitTimingDialog initTimingDialog, String sourceName) throws UniformTestbenchException {
        
        Testbench tb = null;
        TimeScale timeScale = TimeScale.valueOf(initTimingDialog.getTimeScale());
        
        if(Math.log(TimeScale.getMultiplier(timeScale)) + Math.log(initTimingDialog.getInitialLengthOfTestbench()) >= Math.log(Long.MAX_VALUE)) {
            throw new UniformTestbenchException("Testbench length is too large. Change the time scale.");
        }
        
        long testBenchLength = TimeScale.getMultiplier(timeScale) * initTimingDialog.getInitialLengthOfTestbench();
        
        if(initTimingDialog.isCombinatorial()) {
            CombinatorialTestbench.Properties p = new CombinatorialTestbench.Properties();
            p.assignInputsTime = TimeScale.getMultiplier(timeScale) * initTimingDialog.getAssignInputs();
            p.checkOutputsTime = TimeScale.getMultiplier(timeScale) * initTimingDialog.getCheckOutputs();
            tb = new CombinatorialTestbench(sourceName, testBenchLength, timeScale, p);
        }
        else {
            SingleClockTestbench.Properties p = new SingleClockTestbench.Properties();
            if(initTimingDialog.isRisingEdgeSelected()) {
                p.changeStateEdge = ChangeStateEdge.rising;
            }
            else {
                p.changeStateEdge = ChangeStateEdge.falling;
            }
            p.clockSignalName = initTimingDialog.getClockSignal();
            p.clockTimeHigh = TimeScale.getMultiplier(timeScale) * initTimingDialog.getClockTimeHigh();
            p.clockTimeLow = TimeScale.getMultiplier(timeScale) * initTimingDialog.getClockTimeLow();
            p.inputSetupTime = TimeScale.getMultiplier(timeScale) * initTimingDialog.getInputSetupTime();
            
            tb = new SingleClockTestbench(sourceName, testBenchLength, timeScale, p);
        }
        
        return tb;
    }
}
