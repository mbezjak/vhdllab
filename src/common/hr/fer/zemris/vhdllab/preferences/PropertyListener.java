package hr.fer.zemris.vhdllab.preferences;

import java.util.EventListener;

/**
 * Listener for a certain property in {@link DefaultUserPreferences}.
 * 
 * @author Miro Bezjak
 */
public interface PropertyListener extends EventListener {

	/**
	 * Invoked when <code>name</code> property value in {@link DefaultUserPreferences} has
	 * changed. <code>oldValue</code> will never be the same as <code>newValue</code>.
	 * 
	 * @param name
	 *            a property that changed
	 * @param oldValue
	 *            an old property value
	 * @param newValue
	 *            a new property value
	 */
	void propertyChanged(String name, String oldValue, String newValue);

}
