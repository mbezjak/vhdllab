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
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.Pattern;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.RandomPattern;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RandomPanel extends ScalarPatternPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JPanel left;
	protected JPanel mid = new JPanel();
	protected JPanel right;
		
	protected JTextField pulseLenTB = new JTextField(5);
	protected JTextField seedTB = new JTextField(5);
	protected JTextField maxLenTB = new JTextField(5);
	
	

	private void initLeft()
	{
		left = new JPanel(new BorderLayout());
		JPanel tempPanel = new JPanel(new GridLayout(4,1));
		tempPanel.add(new JLabel("       "));
		tempPanel.add(new JLabel("       "));
		tempPanel.add(new JLabel("Max number of periods:"));
		tempPanel.add(maxLenTB);
		left.add(tempPanel, BorderLayout.NORTH);
	}
	
	private void initMid()
	{
		mid.removeAll();
		mid.setLayout(new BorderLayout());
		JLabel p = new JLabel(new ImageIcon(PulsePanel.class.getResource("random.png")));
		//p.setPreferredSize(new Dimension(113,113));
		mid.add(p);
		mid.repaint();
		mid.updateUI();
	}
	
	private void initRight()
	{
		right = new JPanel(new BorderLayout());
		JPanel tempPanel = new JPanel(new GridLayout(5,1));
		//tempPanel.add(new JLabel("N of periods:"));
		//tempPanel.add(pulseLenTB);
		tempPanel.add(new JLabel("       "));
		tempPanel.add(new JLabel("       "));
		tempPanel.add(new JLabel("       "));
		right.add(tempPanel, BorderLayout.SOUTH);
	}
	
	public RandomPanel() {
		super();
		main.setLayout(new GridLayout(1,3));
		
		initLeft();
		initMid();
		initRight();
		main.add(left);
		main.add(mid);
		main.add(right);
		this.updateUI();
		pulseLenTB.addKeyListener(getKeyListener());
		maxLenTB.addKeyListener(getKeyListener());
		seedTB.addKeyListener(getKeyListener());
	}
	
	@Override
	public String toString() {
		return "Random";
	}

	@Override
	public Pattern getPattern(int cycles, long periodLength) throws NumberFormatException, UniformPatternException {
		
		evaluate(pulseLenTB, EvaluationMethod.ParseLong);
		evaluate(maxLenTB, EvaluationMethod.ParseLong);
		
		return new RandomPattern(
				cycles, 
				1001,
				periodLength,
				Long.parseLong(this.maxLenTB.getText()));
	}
}
