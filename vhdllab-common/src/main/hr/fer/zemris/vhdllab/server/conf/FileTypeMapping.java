package hr.fer.zemris.vhdllab.server.conf;

/**
 * A single file type mapping.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public final class FileTypeMapping {

	/**
	 * A file type of this mapping.
	 */
	private String type;

	/**
	 * Default constructor.
	 */
	public FileTypeMapping() {
	}

	/**
	 * Constructs a file type mapping for a specified <code>type</code>.
	 * 
	 * @param type
	 *            a file type if this mapping
	 * @throws NullPointerException
	 *             if type is <code>null</code>
	 */
	public FileTypeMapping(String type) {
		setType(type);
	}

	/**
	 * Returns a file type of this mapping.
	 * 
	 * @return a file type of this mapping
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets a file type of this mapping.
	 * 
	 * @param type
	 *            a file type of this mapping
	 */
	public void setType(String type) {
		if (type == null) {
			throw new NullPointerException("Type cant be null");
		}
		this.type = type;
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
		result = prime * result + type.hashCode();
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
		if (!(obj instanceof FileTypeMapping))
			return false;
		final FileTypeMapping other = (FileTypeMapping) obj;
		return type.equals(other.type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(20);
		sb.append("mapping ");
		sb.append("type=").append(type);
		return sb.toString();
	}

}
