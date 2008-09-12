/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.editor.newtb.listeners;

import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.Signal;

/**
 * @author Davor Jurisic
 *
 */
public interface SignalChangeListener {
	
	public void signalChanged(Signal signal);
	
	public void suspendSignalChangedEvents();
	
	public void resumeSignalChangedEvents();
}
