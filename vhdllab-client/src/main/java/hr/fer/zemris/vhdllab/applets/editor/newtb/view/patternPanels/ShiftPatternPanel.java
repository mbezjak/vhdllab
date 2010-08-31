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
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.Pattern;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class ShiftPatternPanel extends VectorPatternPanel {

private static final long serialVersionUID = 1L;
	
	private JPanel left;
	private JPanel mid = new JPanel();
	private JPanel right;
	
	protected Integer nula = Integer.valueOf(0);
	protected Integer jedan = Integer.valueOf(1);
	
	protected JComboBox shiftInCombo = new JComboBox(new Integer[]{nula, jedan});
	protected JTextField initTB = new JTextField(5);
	protected JTextField LenTB = new JTextField(5);
	
	private void initLeft()
	{
		left = new JPanel(new BorderLayout(4,1));
		JPanel tempPanel = new JPanel(new GridLayout(3,1));
		tempPanel.add(new JLabel(" "));
		tempPanel.add(new JLabel("Initial value:"));
		tempPanel.add(initTB);
		left.add(tempPanel, BorderLayout.NORTH);
	}
	
	private void initMid()
	{
		mid.removeAll();
		mid.setLayout(new BorderLayout());
		JLabel p = new JLabel(new ImageIcon(PulsePanel.class.getResource(getPicName())));
		p.setPreferredSize(new Dimension(113,113));
		mid.add(p, BorderLayout.CENTER);
		mid.repaint();
		mid.updateUI();
	}
	
	private void initRight()
	{
		right = new JPanel(new BorderLayout());
		JPanel tempPanel = new JPanel(new GridLayout(3,1));
		tempPanel.add(new JLabel(" "));
		tempPanel.add(new JLabel("Shift in:"));
		tempPanel.add(shiftInCombo);
		right.add(tempPanel, BorderLayout.NORTH);
		JPanel tempPanel2 = new JPanel(new GridLayout(4,1));
		tempPanel2.add(new JLabel("N of periods:"));
		tempPanel2.add(LenTB);
		tempPanel2.add(new JLabel("  "));
		tempPanel2.add(new JLabel("  "));
		right.add(tempPanel2, BorderLayout.SOUTH);
	}
	
	public ShiftPatternPanel() {
		
		super();
		main.setLayout(new GridLayout(1,3));
		initLeft();
		initMid();
		initRight();
		main.add(left);
		main.add(mid);
		main.add(right);
		initTB.addKeyListener(getKeyListener());
		
	}
	
	protected abstract String getPicName();
	@Override
	public abstract String toString();

	@Override
	public Pattern getPattern(int cycles, int dim, long periodLength)
			throws NumberFormatException, UniformSignalChangeException,
			UniformPatternException {
		
		evaluate(initTB, EvaluationMethod.GetValueBig);
		evaluate(LenTB, EvaluationMethod.ParseLong);
		
		return null;
	}

}
