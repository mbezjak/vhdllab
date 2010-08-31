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
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.Pattern;

public class TogglePanel extends PulsePanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TogglePanel() {
		super();
		initLenTB.setVisible(false);
		initLenLab.setVisible(false);
	}
	@Override
	protected String getLow() {
		return "toggleLow.png";
	}
	@Override
	protected String getHigh() {
		return "toggleHigh.png";
	}
	@Override
	public String toString() {
		return "Toggle";
	}

	@Override
	protected Pattern getPattern(int cycles, long periodLength) 
			throws NumberFormatException, UniformPatternException {
		initLenTB.setText(pulseLenTB.getText());
		return super.getPattern(cycles, periodLength);
	}

}
