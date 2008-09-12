package hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels;

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.LeftShiftPattern;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.Pattern;

public class LShiftPatternPanel extends ShiftPatternPanel {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected String getPicName() {
		return "shiftL.png";
	}
	@Override
	public Pattern getPattern(int cycles, int dim, long periodLength)
			throws NumberFormatException, UniformSignalChangeException,
			UniformPatternException {
		super.getPattern(cycles, dim, periodLength);
		return new LeftShiftPattern(
				cycles,
				(short)dim,
				getValueBig(initTB.getText()),
				((Integer)shiftInCombo.getSelectedItem()).shortValue(),
				Long.parseLong(this.LenTB.getText()) * periodLength,
				initTB,
				LenTB
				);
	}
	@Override
	public String toString() {
		return "Shift left";
	}
}
