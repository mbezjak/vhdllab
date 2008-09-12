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
