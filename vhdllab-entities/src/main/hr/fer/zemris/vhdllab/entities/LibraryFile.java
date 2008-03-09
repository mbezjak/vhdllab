package hr.fer.zemris.vhdllab.entities;

import javax.persistence.AssociationOverride;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A file that belongs to a library.
 * 
 * @author Miro Bezjak
 * @since 31/1/2008
 * @version 1.0
 */
@Entity
@AssociationOverride(name = "parent", joinColumns = { @JoinColumn(name = "library_id", nullable = false, updatable = false) })
@Table(name = "library_files", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"name", "library_id" }) })
@NamedQuery(name = LibraryFile.FIND_BY_NAME_QUERY, query = "select l from LibraryFile as l where l.name = :name and l.parent.id = :id order by l.id")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LibraryFile extends BidiResource<Library, LibraryFile> {

	private static final long serialVersionUID = 1L;
	/**
	 * A named query for finding library files by name.
	 */
	public static final String FIND_BY_NAME_QUERY = "library.file.find.by.name";

	/**
	 * Constructor for persistence provider.
	 */
	LibraryFile() {
		super();
	}

	/**
	 * Creates a library file with specified name and type. Content will be set
	 * to empty string.
	 * 
	 * @param library
	 *            a container of a library file
	 * @param name
	 *            a name of a library file
	 * @param type
	 *            a type of a library file
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if either parameter is too long
	 * @see #NAME_LENGTH
	 * @see #TYPE_LENGTH
	 * @see #CONTENT_LENGTH
	 */
	public LibraryFile(Library library, String name, String type) {
		this(library, name, type, "");
	}

	/**
	 * Creates a library file with specified name, type and content.
	 * 
	 * @param library
	 *            a container of a library file
	 * @param name
	 *            a name of a library file
	 * @param type
	 *            a type of a library file
	 * @param content
	 *            content of a library file
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if either parameter is too long
	 * @see #NAME_LENGTH
	 * @see #TYPE_LENGTH
	 * @see #CONTENT_LENGTH
	 */
	public LibraryFile(Library library, String name, String type, String content) {
		super(library, name, type, content);
		library.addChild(this);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param file
	 *            a library file object to duplicate
	 * @param library
	 *            a container for duplicate
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public LibraryFile(LibraryFile file, Library library) {
		super(file, library);
		library.addChild(this);
	}

	/**
	 * Returns a container for this file. Return value will be <code>null</code>
	 * only if file doesn't belong to any library.
	 * 
	 * @return a container for this file
	 */
	public Library getLibrary() {
		return getParent();
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
		if (val != 0) {
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
