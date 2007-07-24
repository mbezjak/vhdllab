package hr.fer.zemris.vhdllab.preferences;

import java.util.EventListener;

/**
 * Listener for a certain property in {@link UserPreferences}.
 * 
 * @author Miro Bezjak
 */
public interface PropertyListener extends EventListener {

	/**
	 * Invoked when certain property value in {@link UserPreferences} has changed.
	 * 
	 * @param oldValue
	 *            an old property value
	 * @param newValue
	 *            a new property value
	 */
	void propertyChanged(String oldValue, String newValue);

}
