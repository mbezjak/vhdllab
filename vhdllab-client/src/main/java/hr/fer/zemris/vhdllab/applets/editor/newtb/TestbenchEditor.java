package hr.fer.zemris.vhdllab.applets.editor.newtb;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.Radix;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchParserException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.help.HelpManager;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.Testbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.TestbenchParser;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.components2.JTestbench;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TestbenchEditor extends AbstractEditor {

    private static final long serialVersionUID = 1L;

    private Testbench testbench = null;
    protected JTestbench jTestbench = null;
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
    protected void doInitWithoutData() {
        if (!this.GUICreated) {
            this.createGUI();
        }
    }

    @Override
    protected void doInitWithData(File f) {
        setModified(false);
        this.initTestbench(f.getData());
        if (!this.GUICreated) {
            this.createGUI();
        }
        this.jTestbench.setModel(this.testbench);
    }

}
