package hr.fer.zemris.vhdllab.entities;

/**
 * A file that belongs to a library. Libraries are stored on file system and for
 * that a custom dao implementation is written. Because of that this class is
 * not an entity!
 * 
 * @author Miro Bezjak
 * @since 31/1/2008
 * @version 1.0
 */
public class LibraryFile extends BidiResource<Library, LibraryFile> {

	private static final long serialVersionUID = 1L;

	public LibraryFile() {
		super();
	}

	public LibraryFile(LibraryFile f) {
		super(f);
	}

	public Library getLibrary() {
		return getParent();
	}

	public void setLibrary(Library l) {
		setParent(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		if (getId() != null) {
			return result;
		}
		result = prime * result
				+ ((getLibrary() == null) ? 0 : getLibrary().hashCode());
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
		if (!(obj instanceof LibraryFile))
			return false;
		if (!super.equals(obj)) {
			return false;
		}
		if (getId() != null) {
			return true;
		}
		final LibraryFile other = (LibraryFile) obj;
		if (getLibrary() == null) {
			if (other.getLibrary() != null)
				return false;
		} else if (!getLibrary().equals(other.getLibrary()))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.entities.Resource#compareTo(hr.fer.zemris.vhdllab.entities.Resource)
	 */
	@Override
	public int compareTo(Resource o) {
		if (this == o)
			return 0;
		if (o == null)
			throw new NullPointerException("Other library file cant be null");
		if (!(o instanceof LibraryFile)) {
			throw new ClassCastException("Object is not of Library file type");
		}
		final LibraryFile other = (LibraryFile) o;
		int val = super.compareTo(other);
		if (getId() != null || val != 0) {
			return val;
		}
		if (getLibrary() == null) {
			if (other.getLibrary() != null)
				return -1;
		} else if (other.getLibrary() == null) {
			return 1;
		} else {
			val = getLibrary().compareTo(other.getLibrary());
		}

		if (val < 0) {
			return -1;
		} else if (val > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.entities.Resource#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(50);
		sb.append("Library File [").append(super.toString());
		Library lib = getLibrary();
		if (lib != null) {
			sb.append(", libraryId=").append(lib.getId());
			sb.append(", libraryName=").append(lib.getName());
		}
		sb.append("]");
		return sb.toString();
	}

}
