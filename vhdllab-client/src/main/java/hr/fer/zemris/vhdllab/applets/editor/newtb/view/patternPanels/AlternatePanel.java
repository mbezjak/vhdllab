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
package hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.EvaluationMethod;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.AlternatePattern;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.Pattern;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AlternatePanel extends VectorPatternPanel {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private JPanel left;
    private JPanel mid = new JPanel();
    private JPanel right;
    
    private JTextField ATB = new JTextField(5);
    private JTextField BTB = new JTextField(5);
    private JTextField ALenTB = new JTextField(5);
    private JTextField BLenTB = new JTextField(5);
    
    private void initLeft()
    {
        left = new JPanel(new BorderLayout(4,1));
        JPanel tempPanel = new JPanel(new GridLayout(3,1));
        tempPanel.add(new JLabel("  "));
        tempPanel.add(new JLabel("A:"));
        tempPanel.add(ATB);
        JPanel tempPanel2 = new JPanel(new GridLayout(4,1));
        tempPanel2.add(new JLabel("N of periods for A:"));
        tempPanel2.add(ALenTB);
        tempPanel2.add(new JLabel("  "));
        tempPanel2.add(new JLabel("  "));
        left.add(tempPanel, BorderLayout.NORTH);
        left.add(tempPanel2, BorderLayout.SOUTH);
    }
    
    private void initMid()
    {
        mid.removeAll();
        mid.setLayout(new BorderLayout());
        JLabel p = new JLabel(new ImageIcon(PulsePanel.class.getResource("alternate.png")));
        p.setPreferredSize(new Dimension(113,113));
        mid.add(p, BorderLayout.CENTER);
        mid.repaint();
        mid.updateUI();
    }
    
    private void initRight()
    {
        right = new JPanel(new BorderLayout());
        JPanel tempPanel = new JPanel(new GridLayout(3,1));
        tempPanel.add(new JLabel("  "));
        tempPanel.add(new JLabel("B:"));
        tempPanel.add(BTB);
        right.add(tempPanel, BorderLayout.NORTH);
        JPanel tempPanel2 = new JPanel(new GridLayout(4,1));
        tempPanel2.add(new JLabel("N of periods for B:"));
        tempPanel2.add(BLenTB);
        tempPanel2.add(new JLabel("  "));
        tempPanel2.add(new JLabel("  "));
        right.add(tempPanel2, BorderLayout.SOUTH);
    }
    
    public AlternatePanel()
    {
        super();
        main.setLayout(new GridLayout(1,3));
        initLeft();
        initMid();
        initRight();
        main.add(left);
        main.add(mid);
        main.add(right);
        ALenTB.setOpaque(true);
        ATB.addKeyListener(getKeyListener());
        BTB.addKeyListener(getKeyListener());
    }
    
    @Override
    public String toString() {
        return "Alternate";
    }

    @Override
    public Pattern getPattern(int cycles, int dim, long periodLength) 
            throws NumberFormatException, UniformSignalChangeException, UniformPatternException {
        
        evaluate(ATB, EvaluationMethod.GetValueBig);
        evaluate(BTB, EvaluationMethod.GetValueBig);
        evaluate(ALenTB, EvaluationMethod.ParseLong);
        evaluate(BLenTB, EvaluationMethod.ParseLong);
        
        return new AlternatePattern(
                cycles,
                getValueBig(ATB.getText()),
                getValueBig(BTB.getText()),
                Long.parseLong(ALenTB.getText()) * periodLength,
                Long.parseLong(BLenTB.getText()) * periodLength,
                dim,
                ATB,
                BTB
                );
    }

}
