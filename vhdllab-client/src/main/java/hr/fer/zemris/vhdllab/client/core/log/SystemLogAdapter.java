package hr.fer.zemris.vhdllab.client.core.log;

import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;

/**
 * An abstract adapter class for system log listener. The methods in this class
 * are empty. This class exists as convenience for creating listener objects.
 * <p>
 * Extend this class to create a <code>SystemLog</code> listener and override
 * the methods for the events of interest. (If you implement the
 * <code>SystemLogListener</code> interface, you have to define all of the
 * methods in it. This abstract class defines null methods for them all, so you
 * can only have to define methods for events you care about.)
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 19.8.2007
 */
public abstract class SystemLogAdapter implements SystemLogListener {
	@Override
	public void compilationTargetAdded(ResultTarget<CompilationResult> result) {
	}

	@Override
	public void errorMessageAdded(SystemMessage message) {
	}

	@Override
	public void simulationTargetAdded(ResultTarget<SimulationResult> result) {
	}

	@Override
	public void systemMessageAdded(SystemMessage message) {
	}
}
