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
package hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns;

import java.math.BigInteger;

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;

import javax.swing.JTextField;
/**
 * Model for Left Shift pattern dialog. 
 * @author Ivan Cesar
 *
 */
public class LeftShiftPattern extends ShiftPattern {

	public LeftShiftPattern(int cycles, short dim, BigInteger initialValue,
			short shiftIn, long shiftLen, JTextField inValTB,
			JTextField shiftLenTB) throws UniformPatternException {
		super(cycles, dim, initialValue, shiftIn, shiftLen, inValTB, shiftLenTB);
	}

	@Override
	protected String shift(String g, short value) {
		return g.substring(1) + String.valueOf(value);
	}

	

}
