package hr.fer.zemris.vhdllab.client.core.prefs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.event.EventListenerList;

/**
 * This class is designed to replace properties API and provide simple
 * preferences for vhdllab client application (as a wrapper around properties).
 * This class is a singleton and can be accessed by invoking {@link #instance()}
 * method.
 * <p>
 * Note that before user preferences can be used it needs to be initialized
 * (depending on implementation: define how to access properties).
 * Initialization is done by invoking <code>init</code> method. However normal
 * user should not be burdened by initialization, it is up to a system to
 * initialize user preferences.
 * </p>
 * <p>
 * Also note that initialization must be done in a single thread (multi-threaded
 * initialization might result in corrupt data)! All other methods (like
 * getters, setters and listener methods) may be invoked concurrently by
 * multiple threads without the need for external synchronization.
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 1/9/2007
 */
public final class UserPreferences {

	/**
	 * An instance of user preferences.
	 */
	private static final UserPreferences INSTANCE = new UserPreferences();

	/**
	 * A map of registered listeners. key=propertyName,
	 * value=registeredListeners.
	 */
	private Map<String, EventListenerList> listeners;

	/**
	 * A bidi map of registered listeners. key=registeredListener,
	 * value=propertyNames.
	 */
	private Map<UserPreferencesListener, Collection<String>> bidiListeners;

	/**
	 * Contains all properties.
	 */
	private Properties properties;

	/**
	 * A private constructor.
	 */
	private UserPreferences() {
	    this.listeners = new HashMap<String, EventListenerList>();
	    this.bidiListeners = new HashMap<UserPreferencesListener, Collection<String>>();
	}

	/**
	 * Initializes user preferences. This method should be called only once
	 * during initialization of user preferences.
	 * 
	 * @param p
	 *            a properties to set
	 */
	public void init(Properties p) {
		if (p == null) {
			throw new NullPointerException("Properties cant be null");
		}
		UserPreferences pref = instance();
		pref.properties = (Properties) p.clone();
//		pref.listeners = new HashMap<String, EventListenerList>();
//		pref.bidiListeners = new HashMap<UserPreferencesListener, Collection<String>>();
	}

	/**
	 * Returns <code>true</code> if this user preferences has been initialized
	 * or <code>false</code> otherwise. This method is useful only during
	 * initialization of system or to important system component that is a part
	 * of system initialization. Editors and views are not those components so
	 * they have no use for this method because it will always return
	 * <code>true</code> (by the time editors or views begin initializing this
	 * user preferences will already be initialized).
	 * 
	 * @return <code>true</code> if this user preferences has been
	 *         initialized; <code>false</code> otherwise
	 */
	public boolean isInitialized() {
		return properties != null;
	}

	/**
	 * Returns an instance of user preferences.Return value will never be
	 * <code>null</code>. (Multiple calls on this method will return the same
	 * object reference.)
	 * 
	 * @return an instance of user preferences
	 */
	public static UserPreferences instance() {
		return INSTANCE;
	}

	/**
	 * Returns all property keys in this user preferences. Returned value will
	 * never be <code>null</code> although it can empty array and thus
	 * indicating that user preferences is empty.
	 * 
	 * @return all property keys in this user preferences
	 */
	public String[] keys() {
		/*
		 * no synchronization necessary because keys can't be changed after
		 * initialization (that is done in a single thread).
		 */
		String[] keys = new String[properties.size()];
		int i = 0;
		for (Object o : properties.keySet()) {
			keys[i] = (String) o;
			i++;
		}
		return keys;
	}

	/**
	 * Sets a property. If user preferences has not changed as a result of this
	 * call no event will be fired.
	 * 
	 * @param name
	 *            a name of a property to set
	 * @param data
	 *            a data to set to specified property
	 * @return <code>true</code> if user preferences changed as a result of
	 *         this call; <code>false</code> otherwise
	 * @throws NullPointerException
	 *             is either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if property with <code>name</code> does't exist
	 */
	public boolean set(String name, String data) {
		if (name == null) {
			throw new NullPointerException("Property name cant be null");
		}
		if (data == null) {
			throw new NullPointerException("Property data cant be null");
		}
		String oldValue;
		synchronized (this) {
			if (!properties.containsKey(name)) {
				throw new IllegalArgumentException("Property name '" + name
						+ "' doest exist");
			}
			oldValue = properties.getProperty(name);
			// oldValue can't be null!
			if (oldValue.equals(data)) {
				return false;
			}
			properties.put(name, data);
		}
		firePropertyChanged(name, oldValue, data);
		return true;
	}

	/**
	 * Returns a property value for specified property. A default value will be
	 * returned if specified property does not exists or if for some reason
	 * can't be accessed.
	 * 
	 * @param name
	 *            a name of a property
	 * @param def
	 *            a default value
	 * @return a value for a property
	 * @throws NullPointerException
	 *             if <code>name</code> is <code>null</code>
	 */
	public synchronized String get(String name, String def) {
		if (name == null) {
			throw new NullPointerException("Property name cant be null");
		}
		if(properties == null) {
		    return def;
		}
		return properties.getProperty(name, def);
	}

	/**
	 * Returns a property value for specified property. A default value will be
	 * returned if specified property does not exists or if for some reason
	 * can't be accessed.
	 * 
	 * @param name
	 *            a name of a property
	 * @param def
	 *            a default value
	 * @return a value for a property
	 * @throws NullPointerException
	 *             if <code>name</code> is <code>null</code>
	 */
	public double getDouble(String name, double def) {
		/*
		 * synchronization is achieved through #get(String) method
		 */
		String data = get(name, null);
		if (data == null) {
			return def;
		}
		return Double.parseDouble(data.replace(',', '.'));
	}

	/**
	 * Returns a property value for specified property. A default value will be
	 * returned if specified property does not exists or if for some reason
	 * can't be accessed.
	 * 
	 * @param name
	 *            a name of a property
	 * @param def
	 *            a default value
	 * @return a value for a property
	 * @throws NullPointerException
	 *             if <code>name</code> is <code>null</code>
	 */
	public boolean getBoolean(String name, boolean def) {
		/*
		 * synchronization is achieved through #get(String) method
		 */
		String data = get(name, null);
		if (data == null) {
			return def;
		}
		return Boolean.parseBoolean(data);
	}

	/**
	 * Returns a property value for specified property. A default value will be
	 * returned if specified property does not exists or if for some reason
	 * can't be accessed.
	 * 
	 * @param name
	 *            a name of a property
	 * @param def
	 *            a default value
	 * @return a value for a property
	 * @throws NullPointerException
	 *             if <code>name</code> is <code>null</code>
	 */
	public int getInt(String name, int def) {
		/*
		 * synchronization is achieved through #get(String) method
		 */
		String data = get(name, null);
		if (data == null) {
			return def;
		}
		return Integer.parseInt(data);
	}

	/**
	 * Returns a property value for specified property. A default value will be
	 * returned if specified property does not exists or if for some reason
	 * can't be accessed.
	 * 
	 * @param name
	 *            a name of a property
	 * @param def
	 *            a default value
	 * @return a value for a property
	 * @throws NullPointerException
	 *             if <code>name</code> is <code>null</code>
	 */
	public long getLong(String name, long def) {
		/*
		 * synchronization is achieved through #get(String) method
		 */
		String data = get(name, null);
		if (data == null) {
			return def;
		}
		return Long.parseLong(data);
	}

	/**
	 * Returns a property value for specified property. A default value will be
	 * returned if specified property does not exists or if for some reason
	 * can't be accessed.
	 * 
	 * @param name
	 *            a name of a property
	 * @param def
	 *            a default value
	 * @return a value for a property
	 * @throws NullPointerException
	 *             if <code>name</code> is <code>null</code>
	 */
	public Color getColor(String name, Color def) {
		/*
		 * synchronization is achieved through #get(String) method
		 */
		int rgb = getInt(name, def.getRGB());
		if (rgb == def.getRGB()) {
			return def;
		}
		return new Color(rgb);
	}

	/**
	 * Adds a preferences listener.
	 * 
	 * @param l
	 *            a listener to add
	 * @param name
	 *            a name of a property to add listener to
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public synchronized void addPreferencesListener(UserPreferencesListener l,
			String name) {
		if (l == null) {
			throw new NullPointerException("Preferences listener cant be null");
		}
		if (name == null) {
			throw new NullPointerException("Property name cant be null");
		}
		if (!listeners.containsKey(name)) {
			listeners.put(name, new EventListenerList());
		}
		listeners.get(name).add(UserPreferencesListener.class, l);
		if (!bidiListeners.containsKey(l)) {
			bidiListeners.put(l, new ArrayList<String>());
		}
		bidiListeners.get(l).add(name);
	}

	/**
	 * Removes a specified listener.
	 * 
	 * @param l
	 *            a listener to remove
	 * @throws NullPointerException
	 *             if <code>l</code> is <code>null</code>
	 */
	public synchronized void removePreferencesListener(UserPreferencesListener l) {
		if (l == null) {
			throw new NullPointerException("Preferences listener cant be null");
		}
		if (bidiListeners.containsKey(l)) {
			for (String name : bidiListeners.remove(l)) {
				removePreferencesListener(l, name);
			}
		}
	}

	/**
	 * Removes a specified listener for specified property.
	 * 
	 * @param l
	 *            a listener to remove
	 * @param name
	 *            a name of a property that will not receive preferences event
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public synchronized void removePreferencesListener(UserPreferencesListener l,
			String name) {
		if (l == null) {
			throw new NullPointerException("Preferences listener cant be null");
		}
		if (l == null) {
			throw new NullPointerException("Property name cant be null");
		}
		if (listeners.containsKey(name)) {
			EventListenerList list = listeners.get(name);
			list.remove(UserPreferencesListener.class, l);
			if (list.getListenerCount() == 0) {
				listeners.remove(name);
			}
		}
		if (bidiListeners.containsKey(l)) {
			Collection<String> names = bidiListeners.get(l);
			names.remove(name);
			if (names.isEmpty()) {
				bidiListeners.remove(l);
			}
		}
	}

	/**
	 * Removes all preferences listeners.
	 */
	public synchronized void removeAllPreferencesListeners() {
		listeners.clear();
		bidiListeners.clear();
	}

	/**
	 * Fires event that a property has changed.
	 * 
	 * @param name
	 *            a name of a property
	 * @param oldValue
	 *            an old value of a property
	 * @param newValue
	 *            a new value of a property
	 */
	private void firePropertyChanged(String name, String oldValue,
			String newValue) {
		EventListenerList list;
		synchronized (this) {
			list = listeners.get(name);
		}
		if (list == null) {
			return;
		}
		PreferencesEvent event = new PreferencesEvent(name, oldValue, newValue);
		for (UserPreferencesListener l : list
				.getListeners(UserPreferencesListener.class)) {
			l.propertyChanged(event);
		}
	}

}
