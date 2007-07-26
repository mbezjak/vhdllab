package hr.fer.zemris.vhdllab.preferences;

import hr.fer.zemris.ajax.shared.XMLUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.event.EventListenerList;

public class UserPreferences {

	private Map<String, EventListenerList> listeners;
	private Properties properties;

	public UserPreferences(Properties properties) {
		if(properties == null) {
			throw new NullPointerException("Properties can not be null.");
		}
		this.properties = properties;
		listeners = new HashMap<String, EventListenerList>();
	}
	
	public Set<String> getAllPropertyKeys() {
		Set<String> keys = new HashSet<String>();
		for(Object o : properties.keySet()) {
			keys.add((String) o);
		}
		return keys;
	}

	/**
	 * Adds a listener for specified property.
	 * 
	 * @param l
	 *            a listener to add
	 * @param name
	 *            a name of a property
	 */
	public void addPropertyListener(PropertyListener l, String name) {
		if (!listeners.containsKey(name)) {
			listeners.put(name, new EventListenerList());
		}
		listeners.get(name).add(PropertyListener.class, l);
	}

	/**
	 * Removes a listener for specified property.
	 * 
	 * @param l
	 *            a listener to remove
	 * @param name
	 *            a name of a property
	 */
	public void removePropertyListener(PropertyListener l, String name) {
		if (listeners.containsKey(name)) {
			EventListenerList list = listeners.get(name);
			list.remove(PropertyListener.class, l);
			if (list.getListenerCount() == 0) {
				listeners.remove(name);
			}
		}
	}

	private void firePropertyChanged(String name, String oldValue, String newValue) {
		EventListenerList list = listeners.get(name);
		if(list == null) {
			return;
		}
		for(PropertyListener l : list.getListeners(PropertyListener.class)) {
			l.propertyChanged(name, oldValue, newValue);
		}
	}

	public void setProperty(String name, String data) {
		if (name == null) {
			throw new NullPointerException("Key must not be null.");
		}
		if (data == null) {
			throw new NullPointerException("Data must not be null.");
		}
		String oldValue = properties.getProperty(name);
		if(oldValue.equals(data)) {
			return;
		}
		properties.put(name, data);
		firePropertyChanged(name, oldValue, data);
	}

	public String getProperty(String name) {
		if (name == null) {
			throw new NullPointerException("Name must not be null.");
		}
		return properties.getProperty(name);
	}
	
	public String serialize() {
		return XMLUtil.serializeProperties(properties);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof UserPreferences))
			return false;
		UserPreferences other = (UserPreferences) obj;

		return this.properties.equals(other.properties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return properties.hashCode();
	}

}
