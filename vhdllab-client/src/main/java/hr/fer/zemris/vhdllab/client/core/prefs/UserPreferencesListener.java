package hr.fer.zemris.vhdllab.client.core.prefs;

import java.util.EventListener;

/**
 * Listener for a certain property in {@link UserPreferences}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 1/9/2007
 */
public interface UserPreferencesListener extends EventListener {

	/**
	 * Invoked when <code>name</code> property value in
	 * {@link UserPreferences} has changed. It is guaranteed that
	 * <code>event</code> is not <code>null</code>.
	 * 
	 * @param event
	 *            a preferences event
	 */
	void propertyChanged(PreferencesEvent event);

}
