/**
 * 
 */
package hr.fer.zemris.vhdllab.client.core.prefs;

/**
 * A preferences event fired by {@link PreferencesListener} when a property has
 * changed. This class is immutable and therefor thread-safe.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 1/9/2007
 */
public final class PreferencesEvent {

	/**
	 * A name of a property.
	 */
	private String name;
	/**
	 * An old property value.
	 */
	private String oldValue;
	/**
	 * A new property value.
	 */
	private String newValue;

	/**
	 * Constructs a preferences event out of specified parameters.
	 * 
	 * @param name
	 *            a name of a property
	 * @param oldValue
	 *            an old property value
	 * @param newValue
	 *            a new property value
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>oldValue</code> is same as <code>newValue</code>
	 */
	public PreferencesEvent(String name, String oldValue, String newValue) {
		if (name == null) {
			throw new NullPointerException("Property name cant be null");
		}
		if (oldValue == null) {
			throw new NullPointerException("Old value cant be null");
		}
		if (newValue == null) {
			throw new NullPointerException("New value cant be null");
		}
		if (oldValue.equals(newValue)) {
			throw new IllegalArgumentException("Old value equals new value ('"
					+ oldValue + "')");
		}
		this.name = name;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Returns a name of a property. Return value will never be
	 * <code>null</code>.
	 * 
	 * @return a name of a property
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns an old property value. Return value will never be
	 * <code>null</code> and will never be same as {@link #getNewValue()}.
	 * 
	 * @return an old property value
	 */
	public String getOldValue() {
		return oldValue;
	}

	/**
	 * Returns a new property value. Return value will never be
	 * <code>null</code> and will never be same as {@link #getOldValue()}.
	 * 
	 * @return a new property value
	 */
	public String getNewValue() {
		return newValue;
	}

	/**
	 * Returns a user preferences that caused this event. Return value will
	 * never be <code>null</code>.
	 * 
	 * @return a user preferences
	 */
	public UserPreferences getPreferences() {
		return UserPreferences.instance();
	}

}
