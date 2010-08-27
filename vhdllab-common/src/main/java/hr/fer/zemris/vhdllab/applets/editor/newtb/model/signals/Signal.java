package hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals;

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.listeners.SignalChangeListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * 
 * @author Davor Jurisic & Ivan Cesar
 *
 */
public abstract class Signal implements Iterable<SignalChange> {
	
	protected String name;
	protected short dimension;
	protected Map<Long, SignalChange> changes = null;
	protected SignalChangeListener signalChangeListener = null;
	
	public Signal(String name, short signalDimension, SignalChangeListener listener) throws UniformSignalException
    {
		if(name == null || name.length() == 0)
			throw new UniformSignalException("Ime signala ne smije biti null niti duljine 0.");
		if(signalDimension < 1)
			throw new UniformSignalException("Dimenzija signala mora biti veca ili jednaka 1.");
		
    	this.name = name;
    	this.dimension = signalDimension;
    	this.changes = new TreeMap<Long, SignalChange>();
    	this.signalChangeListener = listener;
    	
    	this.setInitSignalState();
    }

	protected void setInitSignalState() {
		this.changes.clear();
		
		StringBuilder sb = new StringBuilder(this.dimension);
		for(int i = 0; i < this.dimension; i++) {
			sb.append('0');
		}
		
		try {
			this.changes.put(Long.valueOf(0), new SignalChange(this.dimension, sb.toString(), 0, this));
		} catch (UniformSignalChangeException e) {}
		
		if(this.signalChangeListener != null) {
			this.signalChangeListener.signalChanged(this);
		}
	}

    public String getName() {
		return name;
	}

	public short getDimension() {
		return dimension;
	}
	
	public long getSignalLength() {
		try {
			return ((TreeMap<Long, SignalChange>)this.changes).lastKey().longValue();
		} catch(NoSuchElementException e) {
			return 0;
		}
	}
	
	public SignalChange getSignalChange(long time) {
		return ((TreeMap<Long, SignalChange>)this.changes).floorEntry(Long.valueOf(time)).getValue();
	}
	
	public List<SignalChange> getSignalChangeList(long beginTime, long endTime) {
		List<SignalChange> result = new ArrayList<SignalChange>();
		for(SignalChange sc : this.changes.values()) {
			if(sc.getTime() >= beginTime && sc.getTime() <= endTime)
				result.add(sc);
		}
		return result;
	}
	
	public SignalChangeListener getSignalChangeListener() {
		return signalChangeListener;
	}

	public void setSignalChangeListener(SignalChangeListener signalChangeListener) {
		this.signalChangeListener = signalChangeListener;
	}
	
	@Override
	public Iterator<SignalChange> iterator() {
		return this.changes.values().iterator();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dimension;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Signal other = (Signal) obj;
		if (dimension != other.dimension)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
