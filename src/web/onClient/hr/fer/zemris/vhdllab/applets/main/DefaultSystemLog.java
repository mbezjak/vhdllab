package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.event.SystemLogListener;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog;
import hr.fer.zemris.vhdllab.applets.main.model.ResultTarget;
import hr.fer.zemris.vhdllab.applets.main.model.SystemMessage;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.EventListenerList;

/**
 * This is a default implementation of {@link ISystemLog} interface.
 * 
 * @author Miro Bezjak
 */
public class DefaultSystemLog implements ISystemLog {

	/**
	 * Maximum number of system messages. Must always be positive!
	 */
	private static final int MAX_SYSTEM_MESSAGES_COUNT = 50;

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
	 * Constructs an empty system log.
	 */
	public DefaultSystemLog() {
		listeners = new EventListenerList();
		systemMessages = new LinkedList<SystemMessage>();
		errorMessages = new LinkedList<SystemMessage>();
		compilationTargets = new LinkedList<ResultTarget<CompilationResult>>();
		simulationTargets = new LinkedList<ResultTarget<SimulationResult>>();
	}

	/* LISTENERS METHODS */

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#addSystemLogListener(hr.fer.zemris.vhdllab.applets.main.event.SystemLogListener)
	 */
	@Override
	public void addSystemLogListener(SystemLogListener l) {
		listeners.add(SystemLogListener.class, l);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#removeSystemLogListener(hr.fer.zemris.vhdllab.applets.main.event.SystemLogListener)
	 */
	@Override
	public void removeSystemLogListener(SystemLogListener l) {
		listeners.remove(SystemLogListener.class, l);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#removeAllSystemLogListeners()
	 */
	@Override
	public void removeAllSystemLogListeners() {
		for(SystemLogListener l : getSystemLogListeners()) {
			listeners.remove(SystemLogListener.class, l);
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#getSystemLogListeners()
	 */
	@Override
	public SystemLogListener[] getSystemLogListeners() {
		return listeners.getListeners(SystemLogListener.class);
	}

	/* SYSTEM MESSAGE METHODS */

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#addSystemMessage(hr.fer.zemris.vhdllab.applets.main.model.SystemMessage)
	 */
	@Override
	public void addSystemMessage(SystemMessage message) {
		if (message == null) {
			throw new NullPointerException("Message cant be null");
		}
		if (systemMessages.size() == MAX_SYSTEM_MESSAGES_COUNT) {
			// remove first
			systemMessages.remove(0);
		}
		systemMessages.add(message);
		fireSystemMessageAdded(message);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#getSystemMessages()
	 */
	@Override
	public SystemMessage[] getSystemMessages() {
		return systemMessages.toArray(new SystemMessage[systemMessages.size()]);
	}

	/* ERROR MESSAGE METHODS */

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#addErrorMessage(hr.fer.zemris.vhdllab.applets.main.model.SystemMessage)
	 */
	@Override
	public void addErrorMessage(SystemMessage message) {
		if (message == null) {
			throw new NullPointerException("Message cant be null");
		}
		if (errorMessages.size() == MAX_ERROR_MESSAGES_COUNT) {
			// remove first
			errorMessages.remove(0);
		}
		errorMessages.add(message);
		fireErrorMessageAdded(message);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#getErrorMessages()
	 */
	@Override
	public SystemMessage[] getErrorMessages() {
		return errorMessages.toArray(new SystemMessage[errorMessages.size()]);
	}

	/* COMPILATION RESULT TARGET METHODS */

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#addCompilationResultTarget(hr.fer.zemris.vhdllab.applets.main.model.ResultTarget)
	 */
	@Override
	public void addCompilationResultTarget(
			ResultTarget<CompilationResult> result) {
		if (result == null) {
			throw new NullPointerException(
					"Compilation result target cant be null");
		}
		if (compilationTargets.size() == MAX_RESULT_TARGET_COUNT) {
			// remove first
			compilationTargets.remove(0);
		}
		compilationTargets.add(result);
		fireCompilationTargetAdded(result);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#getCompilationResultTargets()
	 */
	@Override
	public List<ResultTarget<CompilationResult>> getCompilationResultTargets() {
		return Collections.unmodifiableList(compilationTargets);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#getLastCompilationResultTarget()
	 */
	@Override
	public ResultTarget<CompilationResult> getLastCompilationResultTarget() {
		if(compilationHistoryIsEmpty()) {
			return null;
		} else {
			// last target
			return compilationTargets.get(compilationTargets.size() - 1);
		}
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#compilationHistoryIsEmpty()
	 */
	@Override
	public boolean compilationHistoryIsEmpty() {
		return compilationTargets.isEmpty();
	}

	/* SIMULATION RESULT TARGET METHODS */

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#addSimulationResultTarget(hr.fer.zemris.vhdllab.applets.main.model.ResultTarget)
	 */
	@Override
	public void addSimulationResultTarget(ResultTarget<SimulationResult> result) {
		if (result == null) {
			throw new NullPointerException(
					"Simulation result target cant be null");
		}
		if (simulationTargets.size() == MAX_RESULT_TARGET_COUNT) {
			// remove first
			simulationTargets.remove(0);
		}
		simulationTargets.add(result);
		fireSimulationTargetAdded(result);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#getSimulationResultTargets()
	 */
	@Override
	public List<ResultTarget<SimulationResult>> getSimulationResultTargets() {
		return Collections.unmodifiableList(simulationTargets);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#getLastSimulationResultTarget()
	 */
	@Override
	public ResultTarget<SimulationResult> getLastSimulationResultTarget() {
		if(simulationHistoryIsEmpty()) {
			return null;
		} else {
			// last target
			return simulationTargets.get(simulationTargets.size() - 1);
		}
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog#simulationHistoryIsEmpty()
	 */
	@Override
	public boolean simulationHistoryIsEmpty() {
		return simulationTargets.isEmpty();
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
