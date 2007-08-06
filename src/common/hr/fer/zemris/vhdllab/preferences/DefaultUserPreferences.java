package hr.fer.zemris.vhdllab.preferences;

import hr.fer.zemris.ajax.shared.XMLUtil;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.event.EventListenerList;

public class DefaultUserPreferences implements IUserPreferences {

	private Map<String, EventListenerList> listeners;
	private Properties properties;

	public DefaultUserPreferences(Properties properties) {
		if(properties == null) {
			throw new NullPointerException("Properties can not be null.");
		}
		this.properties = properties;
		listeners = new HashMap<String, EventListenerList>();
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.preferences.IUserPreferences#propertyKeys()
	 */
	public Set<String> propertyKeys() {
		Set<String> keys = new HashSet<String>();
		for(Object o : properties.keySet()) {
			keys.add((String) o);
		}
		return keys;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.preferences.IUserPreferences#addPropertyListener(hr.fer.zemris.vhdllab.preferences.PropertyListener, java.lang.String)
	 */
	public void addPropertyListener(PropertyListener l, String name) {
		if (!listeners.containsKey(name)) {
			listeners.put(name, new EventListenerList());
		}
		listeners.get(name).add(PropertyListener.class, l);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.preferences.IUserPreferences#removePropertyListener(hr.fer.zemris.vhdllab.preferences.PropertyListener, java.lang.String)
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
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.preferences.IUserPreferences#removeAllPropertyListeners()
	 */
	@Override
	public void removeAllPropertyListeners() {
		for(EventListenerList eventList : listeners.values()) {
			for(PropertyListener l : eventList.getListeners(PropertyListener.class)) {
				eventList.remove(PropertyListener.class, l);
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

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.preferences.IUserPreferences#setProperty(java.lang.String, java.lang.String)
	 */
	public boolean setProperty(String name, String data) {
		if (name == null) {
			throw new NullPointerException("Key must not be null.");
		}
		if (data == null) {
			throw new NullPointerException("Data must not be null.");
		}
		String oldValue = properties.getProperty(name);
		if(oldValue.equals(data)) {
			return false;
		}
		properties.put(name, data);
		firePropertyChanged(name, oldValue, data);
		return true;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.preferences.IUserPreferences#getProperty(java.lang.String)
	 */
	public String getProperty(String name) {
		if (name == null) {
			throw new NullPointerException("Name must not be null.");
		}
		return properties.getProperty(name);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.preferences.IUserPreferences#getPropertyAsDouble(java.lang.String)
	 */
	public Double getPropertyAsDouble(String name) {
		String data = getProperty(name).replace(',', '.');
		return Double.parseDouble(data);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.preferences.IUserPreferences#getPropertyAsBoolean(java.lang.String)
	 */
	public Boolean getPropertyAsBoolean(String name) {
		String data = getProperty(name);
		return Boolean.parseBoolean(data);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.preferences.IUserPreferences#getPropertyAsInteger(java.lang.String)
	 */
	public Integer getPropertyAsInteger(String name) {
		String data = getProperty(name);
		return Integer.parseInt(data);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.preferences.IUserPreferences#getPropertyAsLong(java.lang.String)
	 */
	public Long getPropertyAsLong(String name) {
		String data = getProperty(name);
		return Long.parseLong(data);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.preferences.IUserPreferences#getPropertyAsColor(java.lang.String)
	 */
	public Color getPropertyAsColor(String name) {
		String data = getProperty(name);
		return PrefUtil.deserializeColor(data);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.preferences.IUserPreferences#serialize()
	 */
	public String serialize() {
		return XMLUtil.serializeProperties(properties);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.preferences.IUserPreferences#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof DefaultUserPreferences))
			return false;
		DefaultUserPreferences other = (DefaultUserPreferences) obj;

		return this.properties.equals(other.properties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.preferences.IUserPreferences#hashCode()
	 */
	@Override
	public int hashCode() {
		return properties.hashCode();
	}

}
