package hr.fer.zemris.vhdllab.preferences;

import java.awt.Color;
import java.util.Set;

public interface IUserPreferences {

	Set<String> propertyKeys();

	/**
	 * Adds a listener for specified property.
	 * 
	 * @param l
	 *            a listener to add
	 * @param name
	 *            a name of a property
	 */
	void addPropertyListener(PropertyListener l, String name);

	/**
	 * Removes a listener for specified property.
	 * 
	 * @param l
	 *            a listener to remove
	 * @param name
	 *            a name of a property
	 */
	void removePropertyListener(PropertyListener l, String name);

	boolean setProperty(String name, String data) throws PropertyAccessException;

	String getProperty(String name) throws PropertyAccessException;

	Double getPropertyAsDouble(String name) throws PropertyAccessException;

	Boolean getPropertyAsBoolean(String name) throws PropertyAccessException;

	Integer getPropertyAsInteger(String name) throws PropertyAccessException;

	Long getPropertyAsLong(String name) throws PropertyAccessException;

	Color getPropertyAsColor(String name) throws PropertyAccessException;

	String serialize();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	boolean equals(Object obj);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	int hashCode();

}