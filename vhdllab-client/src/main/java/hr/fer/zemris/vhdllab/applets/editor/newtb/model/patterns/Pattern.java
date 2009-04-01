package hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.Messages;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.SignalChange;

import java.awt.Color;
import java.math.BigInteger;
import java.util.List;
/**
 * Base class for all pattern models.
 * @author Ivan Cesar
 *
 */
public abstract class Pattern {
	/**
	 * Color to paint error text fields
	 */
	protected Color defaultBadColor = new Color(255,200,200);
	/**
	 * Number of cycles for each pattern.
	 */
	protected int cycles;
	/**
	 * A must-implement method, used to generate SignalChanges from each pattern model.
	 * @param start Time at which to start generating signal changes
	 * @param end Time at which to end generating signal changes
	 * @return Returns a list of SignalChange objects
	 * @throws UniformSignalChangeException
	 * @throws UniformPatternException
	 */
	protected abstract List<SignalChange> getChanges(long start, long end) throws UniformSignalChangeException, UniformPatternException;
	/**
	 * Method which just calls getChanges(0, long end), but is here to provide simplicity
	 * @param offset
	 * @throws UniformSignalChangeException
	 * @throws UniformPatternException
	 */
	public final List<SignalChange> getChanges(long offset) throws UniformSignalChangeException, UniformPatternException
	{
		return getChanges(offset, Long.MAX_VALUE - 1100000000000000L);
	}
	public Pattern(int cycles)
	{
		this.cycles = cycles;
	}
	
	public int getCycles()
	{
		return this.cycles;
	}
	/**
	 * Method which returns binary representation of a number, with trailing zeroes
	 * @param num Number to be represented
	 * @param dim Number of digits which must be used to represent num
	 * @return returns String, for example calling a getWithTrailZeroes(5,5) would return "00101"
	 * @throws UniformSignalChangeException
	 * @throws UniformPatternException thrown when dim is not big enough to represent whole number
	 */
	protected String getWithTrailZeroes(int num, int dim) throws UniformSignalChangeException, UniformPatternException
	{
		String ret = Integer.toBinaryString(num);
		if(ret.length() > dim)
			throw new UniformPatternException(Messages.signalDimToSmallNum);
		while(ret.length() != dim)
			ret = "0" + ret;
		return ret;
	}
	protected String getWithTrailZeroes(BigInteger num, int dim) throws UniformSignalChangeException, UniformPatternException
	{
		String ret = num.toString(2);
		if(ret.length() > dim)
			throw new UniformPatternException(Messages.signalDimToSmallNum);
		while(ret.length() != dim)
			ret = "0" + ret;
		return ret;
	}
	
	@Override
    public String toString()
	{
		return "Cycles: " + cycles;
	}
	
}
