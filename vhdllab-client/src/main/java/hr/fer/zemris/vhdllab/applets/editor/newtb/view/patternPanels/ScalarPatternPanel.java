package hr.fer.zemris.vhdllab.applets.editor.newtb.view.patternPanels;

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns.Pattern;

public abstract class ScalarPatternPanel extends PatternPanel {

	private static final long serialVersionUID = 1L;

	public ScalarPatternPanel() {
		super();
	}

	protected abstract Pattern getPattern(int cycles, long periodLength) throws NumberFormatException, UniformSignalChangeException,
	UniformPatternException;
	
	@Override
	public Pattern getPattern(int cycles, int dim, long periodLength)
			throws NumberFormatException, UniformSignalChangeException,
			UniformPatternException {
		return getPattern(cycles, periodLength);
	}

}
