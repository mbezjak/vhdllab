package hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.Messages;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.SignalChange;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

public abstract class ShiftPattern extends VectorPattern {

	private BigInteger initialValue;
	private short shiftIn;
	private long shiftLen;
	
	private void validateForm(JTextField inValTB,JTextField shiftLenTB) throws UniformPatternException
	{
		validateSignalDimension(initialValue, inValTB);
		validateSignalPositive(shiftLen, shiftLenTB, Messages.signalLen0);
	}
	
	public ShiftPattern(int cycles, short dim, BigInteger initialValue, short shiftIn, long shiftLen, 
			JTextField inValTB, JTextField shiftLenTB) throws UniformPatternException {
		
		super(cycles);
		this.dim = dim;
		this.initialValue = initialValue;
		this.shiftIn = shiftIn;
		this.shiftLen = shiftLen;
		validateForm(inValTB, shiftLenTB);
		
	}

	@Override
	public List<SignalChange> getChanges(long start, long end)
			throws UniformSignalChangeException, UniformPatternException {
		
		String temp = getWithTrailZeroes(initialValue, dim);
		List<SignalChange> ret = new ArrayList<SignalChange>();
		
		for(int i  = 0; i < cycles && start + i*shiftLen <= end && start + i*shiftLen >= start; i++ ){
			
			ret.add(new SignalChange(
					dim,
					temp, 
					start + i * shiftLen));
			temp = shift(temp, shiftIn);
		}
		return ret;
	}
	
	@Override
	public String toString() {
		return super.toString() + " initial value: " + initialValue + "\n" + 
		"Shift input: " + shiftIn + ", Length: " + shiftLen;
	}
	
	protected abstract String shift(String g, short value);

}
