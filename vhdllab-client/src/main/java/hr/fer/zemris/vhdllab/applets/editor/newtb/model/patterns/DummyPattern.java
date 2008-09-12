package hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns;

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.SignalChange;

import java.util.List;
/**
 * Dummy pattern, used for testing. See patternTest for clarity.
 * @author Ivan Cesar
 *
 */
public class DummyPattern extends Pattern {

	public DummyPattern(int cycles) {
		super(cycles);
	}
	
	public String getWithTrailZeroesDummy(int num, int dim) throws UniformSignalChangeException, UniformPatternException
	{
		return this.getWithTrailZeroes(num, dim);
	}

	@Override
	public List<SignalChange> getChanges(long start, long end)
			throws UniformSignalChangeException {
		return null;
	}

}
