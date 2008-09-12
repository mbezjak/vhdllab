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
