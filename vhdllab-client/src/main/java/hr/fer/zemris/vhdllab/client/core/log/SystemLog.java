package hr.fer.zemris.vhdllab.client.core.log;

import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.EventListenerList;

/**
 * Defines methods in a system log. A system log is place where all system
 * activity is logged. For example: system messages (presented to a user as a
 * part of system-to-user communication), each compilation or simulation is
 * logged here, etc. All methods in system log allow concurrent access by
 * multiple threads without external synchronization.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 19.8.2007
 */
public final class SystemLog {
	/*
	 * A system log is a singleton class.
	 */

	/**
	 * An instance of system log.
	 */
	private static final SystemLog INSTANCE = new SystemLog();

	/**
	 * Maximum number of system messages. Must always be positive!
	 */
	private static final int MAX_SYSTEM_MESSAGES_COUNT = 200;

	/**
	 * Maximum number of error messages. Must always be positive!
	 */
	private static final int MAX_ERROR_MESSAGES_COUNT = 50;

	/**
	 * Maximum number of result targets. Must always be positive!
	 */
	private static final int MAX_RESULT_TARGET_COUNT = 50;

	/**
	 * All registered listeners.
	 */
	private EventListenerList listeners;

	/**
	 * System messages.
	 */
	private List<SystemMessage> systemMessages;

	/**
	 * Error messages. For debugging purposes only!
	 */
	private List<SystemMessage> errorMessages;

	/**
	 * Compilation result targets.
	 */
	private List<ResultTarget<CompilationResult>> compilationTargets;

	/**
	 * Simulation result targets.
	 */
	private List<ResultTarget<SimulationResult>> simulationTargets;

	/**
	 * Private constructor. Constructs an empty system log.
	 */
	private SystemLog() {
		listeners = new EventListenerList();
		systemMessages = new LinkedList<SystemMessage>();
		errorMessages = new LinkedList<SystemMessage>();
		compilationTargets = new LinkedList<ResultTarget<CompilationResult>>();
		simulationTargets = new LinkedList<ResultTarget<SimulationResult>>();
	}

	/**
	 * Returns an instance of a system log. Return value will never be
	 * <code>null</code>.
	 * 
	 * @return an instance of a system log
	 */
	public static SystemLog instance() {
		return INSTANCE;
	}

	/* LISTENERS METHODS */

	/**
	 * Adds a system log listener.
	 * 
	 * @param l
	 *            a system log listener
	 */
	public synchronized void addSystemLogListener(SystemLogListener l) {
		listeners.add(SystemLogListener.class, l);
	}

	/**
	 * Removes a system log listener.
	 * 
	 * @param l
	 *            a system log listener
	 */
	public synchronized void removeSystemLogListener(SystemLogListener l) {
		listeners.remove(SystemLogListener.class, l);
	}

	/**
	 * Removes all system log listeners.
	 */
	public synchronized void removeAllSystemLogListeners() {
		for (SystemLogListener l : getSystemLogListeners()) {
			listeners.remove(SystemLogListener.class, l);
		}
	}

	/**
	 * Returns an array of all registered system log listeners. Returned array
	 * will never be <code>null</code> although it can be empty list.
	 * 
	 * @return an array of all registered system log listeners.
	 */
	public synchronized SystemLogListener[] getSystemLogListeners() {
		return listeners.getListeners(SystemLogListener.class);
	}

	/* SYSTEM MESSAGE METHODS */

	/**
	 * Adds a system message to this system log.
	 * 
	 * @param text
	 *            a text of a message
	 * @param type
	 *            a message type
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public void addSystemMessage(String text, MessageType type) {
		addSystemMessage(new SystemMessage(text, type));
	}

	/**
	 * Adds a system message to this system log.
	 * 
	 * @param message
	 *            a system message to add.
	 * @throws NullPointerException
	 *             if <code>message</code> is <code>null</code>
	 */
	public void addSystemMessage(SystemMessage message) {
		if (message == null) {
			throw new NullPointerException("Message cant be null");
		}
		synchronized (this) {
			if (systemMessages.size() == MAX_SYSTEM_MESSAGES_COUNT) {
				// remove first
				systemMessages.remove(0);
			}
			systemMessages.add(message);
		}
		fireSystemMessageAdded(message);
	}

	/**
	 * Returns an array of system messages. Return value will never be
	 * <code>null</code> although it can be empty array.
	 * 
	 * @return an array of system messages
	 */
	public synchronized SystemMessage[] getSystemMessages() {
		return systemMessages.toArray(new SystemMessage[systemMessages.size()]);
	}

	/* ERROR MESSAGE METHODS */

	/**
	 * Adds an error message to this system log. Unlike a system message, an
	 * error message is used for debugging purposes only!
	 * 
	 * @param cause
	 *            an exception that occurred
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public void addErrorMessage(Throwable cause) {
		addErrorMessage(new SystemMessage(cause));
	}

	/**
	 * Adds an error message to this system log. Unlike a system message, an
	 * error message is used for debugging purposes only!
	 * 
	 * @param message
	 *            an error message to add.
	 * @throws NullPointerException
	 *             if <code>message</code> is <code>null</code>
	 */
	public void addErrorMessage(SystemMessage message) {
		if (message == null) {
			throw new NullPointerException("Message cant be null");
		}
		synchronized (this) {
			if (errorMessages.size() == MAX_ERROR_MESSAGES_COUNT) {
				// remove first
				errorMessages.remove(0);
			}
			errorMessages.add(message);
		}
		fireErrorMessageAdded(message);
	}

	/**
	 * Returns an array of error messages. Unlike a system message, an error
	 * message is used for debugging purposes only! Return value will never be
	 * <code>null</code> although it can be empty array.
	 * 
	 * @return an array of error messages
	 */
	public synchronized SystemMessage[] getErrorMessages() {
		return errorMessages.toArray(new SystemMessage[errorMessages.size()]);
	}

	/* COMPILATION RESULT TARGET METHODS */

	/**
	 * Adds a compilation result target to this system log.
	 * 
	 * @param result
	 *            a compilation result target
	 */
	public void addCompilationResultTarget(
			ResultTarget<CompilationResult> result) {
		if (result == null) {
			throw new NullPointerException(
					"Compilation result target cant be null");
		}
		synchronized (this) {
			if (compilationTargets.size() == MAX_RESULT_TARGET_COUNT) {
				// remove first
				compilationTargets.remove(0);
			}
			compilationTargets.add(result);
		}
		fireCompilationTargetAdded(result);
	}

	/**
	 * Returns an unmodifiable list of compilation result targets. Return value
	 * will never be <code>null</code> although it can be empty list.
	 * 
	 * @return an unmodifiable list of compilation result targets
	 */
	public synchronized List<ResultTarget<CompilationResult>> getCompilationResultTargets() {
		return Collections.unmodifiableList(compilationTargets);
	}

	/**
	 * Returns a last compiled result target or <code>null</code> if
	 * compilation history is empty.
	 * 
	 * @return a last compiled result target or <code>null</code> if
	 *         compilation history is empty
	 */
	public synchronized ResultTarget<CompilationResult> getLastCompilationResultTarget() {
		if (compilationHistoryIsEmpty()) {
			return null;
		}
		// else last target
		return compilationTargets.get(compilationTargets.size() - 1);
	}

	/**
	 * Returns <code>true</code> if no compilation result target is stored in
	 * system log or <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if no compilation result target is stored in
	 *         system log; <code>false</code> otherwise
	 */
	public synchronized boolean compilationHistoryIsEmpty() {
		return compilationTargets.isEmpty();
	}

	/* SIMULATION RESULT TARGET METHODS */

	/**
	 * Adds a simulation result target to this system log.
	 * 
	 * @param result
	 *            a simulation result target
	 */
	public void addSimulationResultTarget(ResultTarget<SimulationResult> result) {
		if (result == null) {
			throw new NullPointerException(
					"Simulation result target cant be null");
		}
		synchronized (this) {
			if (simulationTargets.size() == MAX_RESULT_TARGET_COUNT) {
				// remove first
				simulationTargets.remove(0);
			}
			simulationTargets.add(result);
		}
		fireSimulationTargetAdded(result);
	}

	/**
	 * Returns an unmodifiable list of simulation result targets. Return value
	 * will never be <code>null</code> although it can be empty list.
	 * 
	 * @return an unmodifiable list of simulation result targets
	 */
	public synchronized List<ResultTarget<SimulationResult>> getSimulationResultTargets() {
		return Collections.unmodifiableList(simulationTargets);
	}

	/**
	 * Returns a last simulated result target or <code>null</code> if
	 * simulation history is empty.
	 * 
	 * @return a last simulated result target or <code>null</code> if
	 *         simulation history is empty
	 */
	public synchronized ResultTarget<SimulationResult> getLastSimulationResultTarget() {
		if (simulationHistoryIsEmpty()) {
			return null;
		}
		// else last target
		return simulationTargets.get(simulationTargets.size() - 1);
	}

	/**
	 * Returns <code>true</code> if no simulation result target is stored in
	 * system log or <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if no simulation result target is stored in
	 *         system log; <code>false</code> otherwise
	 */
	public synchronized boolean simulationHistoryIsEmpty() {
		return simulationTargets.isEmpty();
	}
	
	/**
	 * Clears all information in this system log.
	 */
	public synchronized void clearAll() {
		listeners = new EventListenerList();
		systemMessages.clear();
		errorMessages.clear();
		compilationTargets.clear();
		simulationTargets.clear();
	}

	/* FIRE EVENT METHODS */

	/**
	 * Fires systemMessageAdded event.
	 * 
	 * @param message
	 *            an added system message
	 */
	private void fireSystemMessageAdded(SystemMessage message) {
		for (SystemLogListener l : getSystemLogListeners()) {
			l.systemMessageAdded(message);
		}
	}

	/**
	 * Fires errorMessageAdded event.
	 * 
	 * @param message
	 *            an added error message
	 */
	private void fireErrorMessageAdded(SystemMessage message) {
		for (SystemLogListener l : getSystemLogListeners()) {
			l.errorMessageAdded(message);
		}
	}

	/**
	 * Fires compilationTargetAdded event.
	 * 
	 * @param result
	 *            a compilation result target
	 */
	private void fireCompilationTargetAdded(
			ResultTarget<CompilationResult> result) {
		for (SystemLogListener l : getSystemLogListeners()) {
			l.compilationTargetAdded(result);
		}
	}

	/**
	 * Fires simulationTargetAdded event.
	 * 
	 * @param result
	 *            a simulation result target
	 */
	private void fireSimulationTargetAdded(ResultTarget<SimulationResult> result) {
		for (SystemLogListener l : getSystemLogListeners()) {
			l.simulationTargetAdded(result);
		}
	}

}
