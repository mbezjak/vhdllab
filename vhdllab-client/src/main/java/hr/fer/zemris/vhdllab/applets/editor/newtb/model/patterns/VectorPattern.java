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

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.Messages;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;

import java.awt.Color;
import java.math.BigInteger;

import javax.swing.JTextField;
/**
 * Base class for all vector pattern models.
 * @author Ivan Cesar
 *
 */
public abstract class VectorPattern extends Pattern {

	protected short dim;
	
	public VectorPattern(int cycles) {
		super(cycles);
	}
	/**
	 * Method which validates if given value is positive
	 * @param val
	 * @param TB TextField which was used to enter value - passed here so in UniformPatternException it can be 
	 * colored red.
	 * @throws UniformPatternException
	 */
	protected void validateSignalPositive( long val, JTextField TB, String errorMsg) throws UniformPatternException
	{
		if(val <= 0)
			throw new UniformPatternException(errorMsg, TB);
	}
	protected void validateSignalPositive( BigInteger val, JTextField TB, String errorMsg) throws UniformPatternException
	{
		if(val.compareTo(BigInteger.ZERO) <= 0)
			throw new UniformPatternException(errorMsg, TB);
	}
	/**
	 * Similar as validateSignalPositive, but checks if signal dimension is sufficient to represent given number
	 * @param num
	 * @param TB
	 * @throws UniformPatternException
	 */
	protected void validateSignalDimension( int num, JTextField TB ) throws UniformPatternException
	{
		try {
			TB.setBackground(Color.white);
			if(num < 0)
				throw new UniformPatternException(Messages.countBorder0, TB);
			if(this.getWithTrailZeroes(num, dim).length() > dim )
				throw new UniformPatternException(Messages.signalDimToSmallNum, TB);
		} catch (Exception e) {
			TB.setBackground(defaultBadColor);
			TB.validate();
			throw new UniformPatternException(Messages.signalDimToSmallNum );
		}
		TB.validate();
	}
	protected void validateSignalDimension( BigInteger num, JTextField TB ) throws UniformPatternException
	{
		try {
			TB.setBackground(Color.white);
			if(num.compareTo(BigInteger.ZERO) < 0)
				throw new UniformPatternException(Messages.countBorder0, TB);
			if(this.getWithTrailZeroes(num, dim).length() > dim )
				throw new UniformPatternException(Messages.signalDimToSmallNum, TB);
		} catch (Exception e) {
			TB.setBackground(defaultBadColor);
			TB.validate();
			throw new UniformPatternException(Messages.signalDimToSmallNum );
		}
		TB.validate();
	}

}
