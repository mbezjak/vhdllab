package hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.Messages;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.SignalChange;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
/**
 * Model for Count pattern dialog.
 * @author Ivan Cesar
 *
 */
public class CountPattern extends VectorPattern{

	private BigInteger A;
	private BigInteger B;
	private long toggleLen;
	private BigInteger step;
	/**
	 * Calls inherited methods validateSignalPositive and validateSignalDimension
	 * @param toggleLenTB Text field to validate
	 * @param stepTB Text field to validate
	 * @param ATB Text field to validate
	 * @param BTB Text field to validate
	 * @throws UniformPatternException
	 */
	private void validateForm(JTextField toggleLenTB, JTextField stepTB, JTextField ATB, JTextField BTB) throws UniformPatternException
	{
		validateSignalPositive(toggleLen, toggleLenTB, Messages.signalLen0);
		validateSignalPositive(step, stepTB, Messages.step0);
		validateSignalDimension(A, ATB);
		validateSignalDimension(B, BTB);	
	}
	/**
	 * Constructor for CountPattern type
	 * @param cycles Number of cycles
	 * @param toggleLen Length of one state of signal
	 * @param A integer value representing signal state - see Alternate pattern for more info
	 * @param B
	 * @param dim
	 * @param step Long representing decimal number difference between two neighbour states
	 * @param stepTB TextField in dialog which is used to get step value
	 * @param toggleLenTB
	 * @param ATB
	 * @param BTB
	 * @throws UniformSignalChangeException
	 * @throws UniformPatternException
	 */
	public CountPattern(int cycles,long toggleLen, BigInteger A, BigInteger B, int dim,
			BigInteger step, JTextField stepTB, JTextField toggleLenTB, JTextField ATB, JTextField BTB) 
			throws UniformSignalChangeException, UniformPatternException {
		
		super(cycles);
		this.A = A;
		this.B = B;
		this.dim = (short)dim;
		this.toggleLen = toggleLen;
		this.step = step;
		
		validateForm(toggleLenTB, stepTB, ATB, BTB);	
	}

	/**
	 * Two methods - separated for clarity, this one counts from A to B if B > A
	 * @param start
	 * @param end
	 * @return
	 * @throws UniformSignalChangeException
	 * @throws UniformPatternException
	 */
	private List<SignalChange> generateUp(long start, long end) throws UniformSignalChangeException, UniformPatternException
	{
		List<SignalChange> ret = new ArrayList<SignalChange>();
		
		for(long i = 0; i < cycles; i++)
		{
			for( BigInteger j = new BigInteger(A.toString()); j.compareTo(B) <= 0; j = j.add(step))
			{
				long nowTime = start + i * (toggleLen * (B.subtract(A).divide(step).add(BigInteger.ONE).longValue())) + (j.subtract(A).divide(step).longValue()) * toggleLen;
				if(nowTime > end || nowTime < start)
					return ret;
				ret.add(new SignalChange(
						dim,
						this.getWithTrailZeroes(j, dim),
						nowTime));
				
			}
		}
		return ret;
	}
	/**
	 * Two methods - separated for clarity, this one counts from A to B if A > B
	 * @param start
	 * @param end
	 * @return
	 * @throws UniformSignalChangeException
	 * @throws UniformPatternException
	 */
	private List<SignalChange> generateDown(long start, long end) throws UniformSignalChangeException, UniformPatternException
	{
		List<SignalChange> ret = new ArrayList<SignalChange>();
		
		for(long i = 0; i < cycles; i++)
		{
			for( BigInteger j = new BigInteger(A.toString()); j.compareTo(B) >= 0; j = j.subtract(step))
			{
				long nowTime = start + i * (toggleLen * (A.subtract(B).divide(step).add(BigInteger.ONE).longValue()))
						+ (A.subtract(j).divide(step).longValue()) * toggleLen;
				if(nowTime > end)
					return ret;
				ret.add(new SignalChange(
						dim,
						this.getWithTrailZeroes(j, dim),
						nowTime));
				
			}
		}
		return ret;
	}
	
	@Override
	public List<SignalChange> getChanges(long start, long end)
			throws UniformSignalChangeException, UniformPatternException {
		if(A.compareTo(B) > 0)
			return generateDown(start,end);
		return generateUp(start,end);
	}
	@Override
	public String toString() {
		return super.toString() + " Dimenzija: " + dim + "\n" + 
			"A - B: " + A + " - " + B + "\n" + 
			"Toggle len (step): " + toggleLen + "(" + step + ")";
	}
}
