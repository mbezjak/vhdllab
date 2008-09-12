package hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.Messages;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.SignalChange;
import java.math.BigInteger;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
/**
 * Represents Alternate pattern dialog model. 
 * @author Ivan Cesar
 *
 */
public class AlternatePattern extends VectorPattern {
	
	private long BLen;
	private long ALen;
	private String B;
	private String A;
	private short dim;
	
	/**
	 * 
	 * @param cycles Number of cycles
	 * @param A Integer representing one step of a pattern, for example: A = 3 and dimension = 4, 
	 * pattern is: 0011
	 * @param B Same as A, just this is second step
	 * @param ALen Period length of pattern of type A 
	 * @param BLen -||-
	 * @param dim Signal dimension
	 * @param ATB This is passed from dialog - it is required to mark specific alternate pattern errors in dialog - if 
	 * signal dimension is too small to represent desired number A
	 * @param BTB -||-
	 * @throws UniformSignalChangeException 
	 * @throws UniformPatternException Thrown if format of numbers A, B, ALen or BLen is wrong
	 */
	public AlternatePattern(int cycles, BigInteger A, BigInteger B, long ALen, long BLen, int dim, 
			JTextField ATB, JTextField BTB) throws UniformSignalChangeException, UniformPatternException {
		super(cycles);
		try {
			ATB.setBackground(Color.white);
			this.A = getWithTrailZeroes(A, dim);
		} catch (UniformPatternException e) {
			throw new UniformPatternException(e.getMessage(), ATB);
		}
		finally
		{
			ATB.validate();
		}
		try {
			BTB.setBackground(Color.white);
			this.B = getWithTrailZeroes(B, dim);
		} catch (UniformPatternException e) {
			throw new UniformPatternException(e.getMessage(), BTB);
		}
		finally
		{
			BTB.validate();
		}
		this.ALen = ALen;
		this.BLen = BLen;
		if(ALen <= 0 || BLen <= 0 )
			throw new UniformPatternException(Messages.signalLen0);
		this.dim = (short)dim;
	}

	@Override
	public List<SignalChange> getChanges(long start, long end)
			throws UniformSignalChangeException {
		
		List<SignalChange> ret = new ArrayList<SignalChange>();
		for(long i = 0; i < cycles; i++)
		{
			if(start + i * (ALen + BLen) > end || start + i * (ALen + BLen) < start)
				break;
			ret.add(new SignalChange(
					dim,
					A,
					start + i * (ALen + BLen)
					));
			if(start + i*(ALen + BLen) + ALen > end || start + i*(ALen + BLen) + ALen < start)
				break;
			ret.add(new SignalChange(
					dim,
					B,
					start + i*(ALen + BLen) + ALen
					));
		}
		return ret;
	}
	@Override
	public String toString() {
		return super.toString() + " Dimenzija: " + dim + "\n" + 
			"A (len): " + A + "(" + ALen + ")\n" + 
			"B (len): " + B + "(" + BLen + ")\n" ;
			
	}

}
