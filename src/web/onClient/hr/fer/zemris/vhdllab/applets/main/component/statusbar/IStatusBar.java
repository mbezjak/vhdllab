package hr.fer.zemris.vhdllab.applets.main.component.statusbar;

import java.awt.Component;
import java.util.List;

/**
 * Interface that every status bar must implement. A status bar is a component
 * at the bottom of a frame that displays useful information. Displayed
 * information does not have to be string exclusively. Status bar may contain
 * any custom component (such as for example a button) however primary task
 * of status bar is to display simple, yet valuable message to a user.
 * 
 * @author Miro Bezjak
 */
public interface IStatusBar {

	/**
	 * Registeres a <code>StatusListener</code> to the status bar.
	 * @param l a status listener to add
	 */
	void addStatusListener(StatusListener l);

	/**
	 * Unregisteres specified <code>StatusListener</code>.
	 * @param l a status listener to remove
	 */
	void removeStatusListener(StatusListener l);

	/**
	 * Returns all registered status listeners. Returned list is unmodifiable!
	 * @return all registered status listeners
	 */
	List<StatusListener> getStatusListeners();

	/**
	 * Sets specified message to be displayed in status bar. Message type will
	 * be set to {@link MessageEnum#Information}.
	 * @param message a message to be displayed
	 */
	void setMessage(String message);
	
	/**
	 * Sets specified message to be displayed in status bar.
	 * @param message a message to be displayed
	 * @param message type
	 */
	void setMessage(String message, MessageEnum type);
	
	/**
	 * Returns a message displayed in status bar.
	 * @return a message displayed in status bar
	 */
	String getMessage();
	
	/**
	 * Returns MessageEnum that represents message type.
	 * @return message type
	 */
	MessageEnum getMessageType();
	
	/**
	 * Adds custom component to status bar.
	 * @param c a component to add in status bar
	 * @param x position in pixels on x-axis; position is relative to the 
	 * 		begining of x position of status bar
	 */
	void addComponent(Component c, int x);

}
