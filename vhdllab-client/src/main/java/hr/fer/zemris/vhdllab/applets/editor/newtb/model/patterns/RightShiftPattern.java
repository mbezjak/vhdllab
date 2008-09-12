package hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns;

import java.math.BigInteger;

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;

import javax.swing.JTextField;

public class RightShiftPattern extends ShiftPattern {

	public RightShiftPattern(int cycles, short dim, BigInteger initialValue,
			short shiftIn, long shiftLen, JTextField inValTB,
			JTextField shiftLenTB) throws UniformPatternException {
		super(cycles, dim, initialValue, shiftIn, shiftLen, inValTB, shiftLenTB);
	}

	@Override
	protected String shift(String g, short value) {
		return String.valueOf(value) + g.substring(0, g.length() - 1);
	}

}
