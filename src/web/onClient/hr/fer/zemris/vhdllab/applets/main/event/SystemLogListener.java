package hr.fer.zemris.vhdllab.applets.main.event;

import java.util.EventListener;

import hr.fer.zemris.vhdllab.applets.main.model.ResultTarget;
import hr.fer.zemris.vhdllab.applets.main.model.SystemMessage;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;

/**
 * The listener interface for system log.
 * 
 * @author Miro Bezjak
 */
public interface SystemLogListener extends EventListener {

	/**
	 * Indicates that a system message was added to the system log.
	 * 
	 * @param message
	 *            a system message added
	 */
	void systemMessageAdded(SystemMessage message);

	/**
	 * Indicates that an error message was added to the system log. Error
	 * messages are strictly for debugging purposes only.
	 * 
	 * @param message
	 *            an error message added
	 */
	void errorMessageAdded(SystemMessage message);

	/**
	 * Indicates that a compilation result target was added to the system log.
	 * 
	 * @param result
	 *            a compilation result target
	 */
	void compilationTargetAdded(ResultTarget<CompilationResult> result);

	/**
	 * Indicates that a simulation result target was added to the system log.
	 * 
	 * @param result
	 *            a simulation result target
	 */
	void simulationTargetAdded(ResultTarget<SimulationResult> result);

}
