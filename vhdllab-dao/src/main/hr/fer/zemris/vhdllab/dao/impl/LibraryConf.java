package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.server.FileTypes;

/**
 * Represents a single library configuration (one &lt;library&gt; tag).
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public class LibraryConf {

	/**
	 * Name of a library.
	 */
	private String name;
	/**
	 * Standard extension of files inside a library.
	 */
	private String extension;
	/**
	 * A file type set when loading a file from a library. Basically an
	 * {@link #extension} in file system will be changed to {@link FileTypes} in
	 * vhdllab.
	 */
	private String fileType;

	/**
	 * Default constructor.
	 */
	public LibraryConf() {
	}

	/**
	 * Constructs a library configuration out of specified name, extension and
	 * mapped file type.
	 * 
	 * @param name
	 *            a name of a library
	 * @param extension
	 *            a standard extension of files inside library
	 * @param fileType
	 *            a file type set when loading a file from a library
	 */
	public LibraryConf(String name, String extension, String fileType) {
		setName(name);
		setExtension(extension);
		setFileType(fileType);
	}

	/**
	 * Returns a name of a library.
	 * 
	 * @return a name of a library
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets a name of a library
	 * 
	 * @param name
	 *            a library name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns a standard extension of files inside a library.
	 * 
	 * @return a standard extension of files inside a library
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Sets a standard extension of files inside a library.
	 * 
	 * @param extension
	 *            an extension of files inside a library
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * Returns a file type set when loading a file from a library.
	 * 
	 * @return a file type set when loading a file from a library
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * Sets a file type set when loading a file from a library
	 * 
	 * @param fileType
	 *            a file type set when loading a file from a library
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
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
		result = prime * result
				+ ((extension == null) ? 0 : extension.hashCode());
		result = prime * result
				+ ((fileType == null) ? 0 : fileType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		final LibraryConf other = (LibraryConf) obj;
		if (extension == null) {
			if (other.extension != null)
				return false;
		} else if (!extension.equals(other.extension))
			return false;
		if (fileType == null) {
			if (other.fileType != null)
				return false;
		} else if (!fileType.equals(other.fileType))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(30);
		sb.append("Library[");
		sb.append("name=").append(name);
		sb.append(", extension=").append(extension);
		sb.append(", mappedTo=").append(fileType);
		sb.append("]");
		return sb.toString();
	}

}
