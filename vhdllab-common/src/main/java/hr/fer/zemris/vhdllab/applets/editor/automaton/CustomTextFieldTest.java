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
package hr.fer.zemris.vhdllab.applets.editor.automaton;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class CustomTextFieldTest extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8526818186600110365L;
	public CustomTextFieldTest(){
		this.getContentPane().setLayout(new BorderLayout());
		final CustomTextField cost=new CustomTextField("",3);
		this.getContentPane().add(cost,BorderLayout.CENTER);
		JButton button=new JButton("test");
		button.addActionListener(new ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent e) {
				System.out.println(cost.getText());
			}

		});
		this.getContentPane().add(button,BorderLayout.NORTH);
	}
	public static void main(String[] args) {
		CustomTextFieldTest tf=new CustomTextFieldTest();
		tf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		tf.setVisible(true);
	}
}
