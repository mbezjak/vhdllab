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

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.SignalChange;
import hr.fer.zemris.vhdllab.applets.editor.newtb.view.PatternDialog;

public class RunnerDialog {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//JOptionPane.showMessageDialog(null, EvaluationMethod.ParseInt);
		//JOptionPane.showMessageDialog(null, PatternDialog.getResultVector(4, 400).toString(), "OK!", JOptionPane.INFORMATION_MESSAGE);
		//JOptionPane.showMessageDialog(null, PatternDialog.getResultScalar(200).toString(), "OK!", JOptionPane.INFORMATION_MESSAGE);
		
		try {
			for(SignalChange sc : PatternDialog.getResultVector(4, 100).getChanges(10000)) {
				System.out.println(sc.toString());
			}
		} catch (UniformSignalChangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UniformPatternException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
