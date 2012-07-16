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
package hr.fer.zemris.vhdllab.applets.editor.newtb.view.components2;

import hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels.GroupBox;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * 
 * @author Davor Jurisic
 *
 */
public class SetSignalChangeValueDialog {
	
	private JTextField valueTextField;
	private JRadioButton binaryRB;
	private JRadioButton decimalRB;
	private JRadioButton hexadecimalRB;
	private GroupBox radixGB;
	
	private JPanel mainPanel;
	
	public SetSignalChangeValueDialog() {
		this.createMainPanel();
	}
	
	private void createMainPanel() {
		JPanel radixPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.binaryRB = new JRadioButton("Binary");
		this.decimalRB = new JRadioButton("Decimal");
		this.hexadecimalRB = new JRadioButton("Hexadecimal");
		radixPanel.add(this.binaryRB);
		radixPanel.add(this.decimalRB);
		radixPanel.add(this.hexadecimalRB);
		ButtonGroup radixButGroup = new ButtonGroup();
		radixButGroup.add(this.binaryRB);
		radixButGroup.add(this.decimalRB);
		radixButGroup.add(this.hexadecimalRB);
		this.binaryRB.setSelected(true);
		
		this.radixGB = new GroupBox("Radix", radixPanel);
		
		JPanel inputPanel = new JPanel(new GridLayout(2, 1));
		this.valueTextField = new JTextField("");
		inputPanel.add(new JLabel("Enter new signal value:"));
		inputPanel.add(this.valueTextField);
		
		this.mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(radixGB, BorderLayout.NORTH);
		mainPanel.add(inputPanel, BorderLayout.SOUTH);		
	}
	
	public int show() {
		return JOptionPane.showConfirmDialog(null, this.mainPanel, "Input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
	}
	
	public String getSignalValue() {
		return this.valueTextField.getText();
	}
	
	public void setSignalValue(String value) {
		this.valueTextField.setText(value);
	}
	
	public void setRadix(int radix) {
		switch(radix) {
			case 2:
				this.binaryRB.setSelected(true);
				break;
			case 10:
				this.decimalRB.setSelected(true);
				break;
			case 16:
				this.hexadecimalRB.setSelected(true);
				break;
			default:
				this.binaryRB.setSelected(true);
		}
	}
	
	public int getRadix() {
		if(this.binaryRB.isSelected()) {
			return 2;
		}
		else if(this.decimalRB.isSelected()) {
			return 10;
		}
		else {
			return 16;
		}
	}
	
	public void setVisibleRadixBox(boolean visible) {
		this.radixGB.setVisible(visible);
	}
}
