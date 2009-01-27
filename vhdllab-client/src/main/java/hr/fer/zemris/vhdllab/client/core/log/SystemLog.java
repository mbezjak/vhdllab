package hr.fer.zemris.vhdllab.client.core.log;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

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

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(SystemLog.class);

    /*
     * A system log is a singleton class.
     */

    /**
     * An instance of system log.
     */
    private static final SystemLog INSTANCE = new SystemLog();

    /**
     * All registered listeners.
     */
    private EventListenerList listeners;

    /**
     * Private constructor. Constructs an empty system log.
     */
    private SystemLog() {
        listeners = new EventListenerList();
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
     * Returns an array of all registered system log listeners. Returned array
     * will never be <code>null</code> although it can be empty list.
     * 
     * @return an array of all registered system log listeners.
     */
    private synchronized SystemLogListener[] getSystemLogListeners() {
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
    private void addSystemMessage(SystemMessage message) {
        if (message == null) {
            throw new NullPointerException("Message cant be null");
        }
        LOG.info(message.getContent());
        fireSystemMessageAdded(message);
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
        addErrorMessage(new SystemError(cause));
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
    private void addErrorMessage(SystemError message) {
        if (message == null) {
            throw new NullPointerException("Message cant be null");
        }
        LOG.error("Exception occurred", message.getCause());
        fireErrorMessageAdded(message);
    }

    /**
     * Clears all information in this system log.
     */
    public synchronized void clearAll() {
        listeners = new EventListenerList();
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
    private void fireErrorMessageAdded(SystemError message) {
        for (SystemLogListener l : getSystemLogListeners()) {
            l.errorMessageAdded(message);
        }
    }

}
