package hr.fer.zemris.vhdllab.applets.main.event;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Generic implemetation of SDK's <code>EventListenerList</code>.
 * @author Miro Bezjak
 */
public class EventListenerList<T extends EventListener> {
	
	/** A list of registered listener */
	private List<T> listenerList;
	
	/**
	 * Constructor.
	 */
	public EventListenerList() {
		this.listenerList = new ArrayList<T>();
	}

	/**
	 * Add a listener to list.
	 * @param <X> Any interface that extends <code>EventListener</code>
	 * @param listener event listener to add
	 */
	public synchronized <X extends T> void add(X listener) {
		this.listenerList.add(listener);
	}
	
	/**
	 * Remove listener from list.
	 * @param <X> Any interface that extends <code>EventListener</code>
	 * @param listener event listener to remove
	 */
	public synchronized <X extends T> void remove(X listener) {
		this.listenerList.remove(listener);
	}
	
	/**
	 * Returns all listeners from a list.
	 * @return all listeners from a list
	 */
	@SuppressWarnings("unchecked")
	public synchronized T[] getListeners() {
		return (T[]) this.listenerList.toArray();
	}
	
}
