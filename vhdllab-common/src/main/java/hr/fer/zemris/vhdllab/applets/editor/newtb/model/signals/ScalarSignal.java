package hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals;

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.listeners.SignalChangeListener;

/**
 * 
 * @author Davor Jurisic
 *
 */
public class ScalarSignal extends EditableSignal {
	
	public ScalarSignal(String name) throws UniformSignalException {
		this(name, null);
	}

	public ScalarSignal(String name, SignalChangeListener listener) throws UniformSignalException {
		super(name, (short)1, listener);
	}
}
