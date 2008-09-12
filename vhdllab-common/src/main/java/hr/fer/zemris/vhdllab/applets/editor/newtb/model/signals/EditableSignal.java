/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.listeners.SignalChangeListener;

/**
 * @author Davor Jurisic
 *
 */
public abstract class EditableSignal extends Signal {

	public EditableSignal(String name, short signalDimension, SignalChangeListener listener) throws UniformSignalException {
		super(name, signalDimension, listener);
	}

	public void setSignalChangeValue(long time, String signalValue) throws UniformSignalChangeException {
		
		Map.Entry<Long, SignalChange> p = ((TreeMap <Long, SignalChange>)changes).floorEntry(time - 1);
		Map.Entry<Long, SignalChange> n = ((TreeMap <Long, SignalChange>)changes).ceilingEntry(time + 1);
		SignalChange prev = p == null ? null : p.getValue();
		SignalChange next = n == null ? null : n.getValue();
		
		if (this.changes.containsKey(new Long(time))) {
			this.changes.get(new Long(time)).setSignalValue(signalValue);
		} else {
			
			this.changes.put(new Long(time), new SignalChange(this.dimension, signalValue,
					time, this));
			if (this.signalChangeListener != null) {
				this.signalChangeListener.signalChanged(this);
			}

		}
		if (prev != null && prev.getSignalValue().equals(signalValue)){
			this.changes.remove(time);
		}
		if (next != null && signalValue.equals(next.getSignalValue())) {
			this.changes.remove(next.getTime());
		}
	}
	
	public void setSignalChangeValue(List<SignalChange> signalChangeList)
			throws UniformSignalChangeException, UniformSignalException {
		if(signalChangeList == null)
			throw new UniformSignalException("signalChangeList ne smije biti null!");
		try
		{
			if(this.signalChangeListener != null) {
				this.signalChangeListener.suspendSignalChangedEvents();
			}
			for(SignalChange sc : signalChangeList) {
				this.setSignalChangeValue(sc.getTime(), sc.getSignalValue());
			}
		}
		catch (UniformSignalChangeException e) {
			throw e;
		}
		finally {
			if(this.signalChangeListener != null) {
				this.signalChangeListener.resumeSignalChangedEvents();
			}
		}
	}
	
	public void deleteSignalChangeValues() {
		this.setInitSignalState();
	}
	
	public void optimizeSignalChangeValues() {
		List<SignalChange> deleteList = new ArrayList<SignalChange>();
		SignalChange last = null;
		
		for(SignalChange sc : this.changes.values()) {
			if(last == null) {
				last = sc;
				continue;
			}
			if(sc.getSignalValue().equals(last.getSignalValue())) {
				deleteList.add(sc);
			}
			else {
				last = sc;
			}
		}
		for(SignalChange sc : deleteList) {
			this.changes.remove(new Long(sc.getTime()));
		}
	}
}
