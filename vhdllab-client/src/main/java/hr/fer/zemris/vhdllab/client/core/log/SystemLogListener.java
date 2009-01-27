package hr.fer.zemris.vhdllab.client.core.log;

import java.util.EventListener;

/**
 * The listener interface for system log.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 19.8.2007
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
	void errorMessageAdded(SystemError message);

}
