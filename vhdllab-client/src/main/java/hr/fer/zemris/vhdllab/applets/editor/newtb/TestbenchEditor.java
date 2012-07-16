/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TestbenchEditor extends AbstractEditor {

    private static final long serialVersionUID = 1L;

    private Testbench testbench = null;
    protected JTestbench jTestbench = null;

    public TestbenchEditor() {
        super();

        wrapInScrollPane = false;
    }

    private void initTestbench(String xml) {
        try {
            this.testbench = TestbenchParser.parseXml(xml);
        } catch (UniformTestbenchParserException e) {
            e.printStackTrace();
        }
    }

    private JComponent createGUI() {
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

        JPanel control = new JPanel(new BorderLayout());
        control.add(topPanel, BorderLayout.NORTH);
        control.add(this.jTestbench, BorderLayout.CENTER);

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

        return control;
    }

    @Override
    protected void doDispose() {
    }

    @Override
    protected String getData() {
        if (this.testbench == null) {
            return "";
        }
        return this.testbench.toXml();
    }

    @Override
    protected JComponent doInitWithoutData() {
        return createGUI();
    }

    @Override
    protected void doInitWithData(File f) {
        setModified(false);
        this.initTestbench(f.getData());
        this.jTestbench.setModel(this.testbench);
    }

}
