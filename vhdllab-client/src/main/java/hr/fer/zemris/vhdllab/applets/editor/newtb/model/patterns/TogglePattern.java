package hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns;

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;

public class TogglePattern extends PulsePattern {
	
	public TogglePattern(int cycles, int initial, int other, long toggleEvery ) throws UniformPatternException {
		super(cycles, initial, toggleEvery, other, toggleEvery);
	}

}
