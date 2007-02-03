package hr.fer.zemris.vhdllab.applets.main.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

/**
 * Generic implemetation of SDK's <code>EventListenerList</code>.
 * @see javax.swing.event.EventListenerList
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
	 * @param <X> Any interface that extends <code>T</code>
	 * @param listener event listener to add
	 * @throws NullPointerException if <code>listener</code> is <code>null</code>
	 */
	public synchronized <X extends T> void add(X listener) {
		if(listener == null) {
			throw new NullPointerException("Listener can not be null.");
		}
		this.listenerList.add(listener);
	}
	
	/**
	 * Remove listener from list.
	 * @param <X> Any interface that extends <code>EventListener</code>
	 * @param listener event listener to remove
	 * @throws NullPointerException if <code>listener</code> is <code>null</code>
	 */
	public synchronized <X extends T> void remove(X listener) {
		if(listener == null) {
			throw new NullPointerException("Listener can not be null.");
		}
		this.listenerList.remove(listener);
	}
	
	/**
	 * Returns all listeners from a list. Returned list is unmodifiable!
	 * @return all listeners from a list
	 */
	public synchronized List<T> getListeners() {
		return Collections.unmodifiableList(listenerList);
	}
	
}
