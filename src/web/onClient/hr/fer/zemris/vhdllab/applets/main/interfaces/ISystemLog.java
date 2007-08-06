package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.event.SystemLogListener;
import hr.fer.zemris.vhdllab.applets.main.model.ResultTarget;
import hr.fer.zemris.vhdllab.applets.main.model.SystemMessage;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;

import java.util.List;

/**
 * Defines methods in a system log. A system log is place where all system
 * activity is logged. For example: system messages (presented to a user as a
 * part of system-to-user communication), each compilation or simulation is
 * logged here, etc.
 * 
 * @author Miro Bezjak
 */
public interface ISystemLog {

	/**
	 * Adds a system log listener.
	 * 
	 * @param l
	 *            a system log listener
	 */
	void addSystemLogListener(SystemLogListener l);

	/**
	 * Removes a system log listener.
	 * 
	 * @param l
	 *            a system log listener
	 */
	void removeSystemLogListener(SystemLogListener l);

	/**
	 * Removes all system log listeners.
	 */
	void removeAllSystemLogListeners();

	/**
	 * Returns an array of all registered system log listeners. Returned array
	 * will never be <code>null</code> although it can be empty list.
	 * 
	 * @return an array of all registered system log listeners.
	 */
	SystemLogListener[] getSystemLogListeners();

	/**
	 * Adds a system message to this system log.
	 * 
	 * @param message
	 *            a system message to add.
	 * @throws NullPointerException
	 *             if <code>message</code> is <code>null</code>
	 */
	void addSystemMessage(SystemMessage message);

	/**
	 * Returns an array of system messages. Return value will never be
	 * <code>null</code> although it can be empty array.
	 * 
	 * @return an array of system messages
	 */
	SystemMessage[] getSystemMessages();

	/**
	 * Adds an error message to this system log. Unlike a system message, an
	 * error message is strictly for debugging purposes only!
	 * 
	 * @param message
	 *            an error message to add.
	 * @throws NullPointerException
	 *             if <code>message</code> is <code>null</code>
	 */
	void addErrorMessage(SystemMessage message);

	/**
	 * Returns an array of error messages. Unlike a system message, an error
	 * message is strictly for debugging purposes only! Return value will never
	 * be <code>null</code> although it can be empty array.
	 * 
	 * @return an array of error messages
	 */
	SystemMessage[] getErrorMessages();

	/**
	 * Adds a compilation result target to this system log.
	 * 
	 * @param result
	 *            a compilation result target
	 */
	void addCompilationResultTarget(ResultTarget<CompilationResult> result);

	/**
	 * Returns an unmodifiable list of compilation result targets. Return value
	 * will never be <code>null</code> although it can be empty list.
	 * 
	 * @return an unmodifiable list of compilation result targets
	 */
	List<ResultTarget<CompilationResult>> getCompilationResultTargets();

	/**
	 * Returns a last compiled result target or <code>null</code> if
	 * compilation history is empty.
	 * 
	 * @return a last compiled result target or <code>null</code> if
	 *         compilation history is empty
	 */
	ResultTarget<CompilationResult> getLastCompilationResultTarget();

	/**
	 * Returns <code>true</code> if no compilation result target is stored in
	 * system log or <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if no compilation result target is stored in
	 *         system log; <code>false</code> otherwise
	 */
	boolean compilationHistoryIsEmpty();

	/**
	 * Adds a simulation result target to this system log.
	 * 
	 * @param result
	 *            a simulation result target
	 */
	void addSimulationResultTarget(ResultTarget<SimulationResult> result);

	/**
	 * Returns an unmodifiable list of simulation result targets. Return value
	 * will never be <code>null</code> although it can be empty list.
	 * 
	 * @return an unmodifiable list of simulation result targets
	 */
	List<ResultTarget<SimulationResult>> getSimulationResultTargets();

	/**
	 * Returns a last simulated result target or <code>null</code> if
	 * simulation history is empty.
	 * 
	 * @return a last simulated result target or <code>null</code> if
	 *         simulation history is empty
	 */
	ResultTarget<SimulationResult> getLastSimulationResultTarget();

	/**
	 * Returns <code>true</code> if no simulation result target is stored in
	 * system log or <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if no simulation result target is stored in
	 *         system log; <code>false</code> otherwise
	 */
	boolean simulationHistoryIsEmpty();

}