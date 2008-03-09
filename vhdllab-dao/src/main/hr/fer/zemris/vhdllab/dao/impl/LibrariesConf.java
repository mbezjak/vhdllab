package hr.fer.zemris.vhdllab.dao.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a libraries configuration (one &lt;libraries&gt; tag).
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public class LibrariesConf {

	/**
	 * All defined libraries in configuration file.
	 */
	private Map<String, LibraryConf> libraries;

	/**
	 * Default constructor.
	 */
	public LibrariesConf() {
		libraries = new HashMap<String, LibraryConf>();
	}

	/**
	 * Adds a single library configuration.
	 * 
	 * @param conf
	 *            a single library configuration to add
	 */
	public void addLibraryConf(LibraryConf conf) {
		libraries.put(conf.getName(), conf);
	}

	/**
	 * Removes a single library configuration.
	 * 
	 * @param conf
	 *            a single library configuration to remove
	 */
	public void removeLibraryConf(LibraryConf conf) {
		libraries.remove(conf.getName());
	}

	/**
	 * Returns a library configuration for specified library or
	 * <code>null</code> if such library doesn't exist.
	 * 
	 * @param libraryName
	 *            a library name for whom to retrieve configuration
	 * @return a library configuration for specified library
	 */
	public LibraryConf getLibraryConf(String libraryName) {
		return libraries.get(libraryName);
	}

	/**
	 * Returns all available libraries. Available libraries are ones that were
	 * set in configuration file. Return value will never be null although it
	 * can be empty collection.
	 * 
	 * @return all available libraries
	 */
	public Collection<LibraryConf> getAvailableLibraries() {
		return libraries.values();
	}
	
	/**
	 * Returns a number of libraries in configuration file.
	 * 
	 * @return a number of libraries in configuration file
	 */
	public int getLibraryCount() {
		return libraries.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + libraries.hashCode();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final LibrariesConf other = (LibrariesConf) obj;
		return libraries.equals(other.libraries);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("Libraries={");
		sb.append(libraries);
		sb.append("}");
		return sb.toString();
	}

}
