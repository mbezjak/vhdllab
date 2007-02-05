package hr.fer.zemris.vhdllab.preferences;

import java.util.EventListener;

/**
 * Listener for <code>SingleOption</code>.
 * @author Miro Bezjak
 */
public interface OptionListener extends EventListener {

	/**
	 * Invoked when chosen value in <code>SingleOption</code> has been changed.
	 * @param oldValue an old chosen value
	 * @param newValue a new value that has been chosen
	 */
	void optionChanged(String oldValue, String newValue);
	
}
