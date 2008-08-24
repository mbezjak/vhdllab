package hr.fer.zemris.vhdllab.applets.main.component.statusbar;

import java.util.EventListener;

/**
 * Listener for status bar component.
 * @author Miro Bezjak
 */
public interface StatusListener extends EventListener {

	/**
	 * Invoked when status text in status bar has changed.
	 * @param c a status conent containing message content and message type
	 */
	void statusChanged(StatusContent c);

}
