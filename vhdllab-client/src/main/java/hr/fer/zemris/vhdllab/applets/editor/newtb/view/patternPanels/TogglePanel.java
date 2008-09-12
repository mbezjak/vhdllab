package hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels;

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.Pattern;

public class TogglePanel extends PulsePanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TogglePanel() {
		super();
		initLenTB.setVisible(false);
		initLenLab.setVisible(false);
	}
	@Override
	protected String getLow() {
		return "toggleLow.png";
	}
	@Override
	protected String getHigh() {
		return "toggleHigh.png";
	}
	@Override
	public String toString() {
		return "Toggle";
	}

	@Override
	protected Pattern getPattern(int cycles, long periodLength) 
			throws NumberFormatException, UniformPatternException {
		initLenTB.setText(pulseLenTB.getText());
		return super.getPattern(cycles, periodLength);
	}

}
