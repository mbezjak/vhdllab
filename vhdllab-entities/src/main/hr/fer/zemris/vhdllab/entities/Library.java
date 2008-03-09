package hr.fer.zemris.vhdllab.entities;

import java.util.Collections;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A library is a collection of library files.
 * 
 * @author Miro Bezjak
 * @since 31/1/2008
 * @version 1.0
 */
@Entity
@Table(name = "libraries", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
@NamedQueries(value = {
		@NamedQuery(name = Library.FIND_BY_NAME_QUERY, query = "select l from Library as l where l.name = :name order by l.id"),
		@NamedQuery(name = Library.GET_ALL_QUERY, query = "select l from Library as l order by l.id") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Library extends Container<LibraryFile, Library> {

	private static final long serialVersionUID = 1L;
	/**
	 * A named query for finding libraries by name.
	 */
	public static final String FIND_BY_NAME_QUERY = "library.find.by.name";
	/**
	 * A named query for finding all libraries.
	 */
	public static final String GET_ALL_QUERY = "library.get.all";

	/**
	 * Constructor for persistence provider.
	 */
	Library() {
		super();
	}

	/**
	 * Creates a library with specified name.
	 * 
	 * @param name
	 *            a name of a library
	 * @throws NullPointerException
	 *             if <code>name</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>name</code> is too long
	 * @see #NAME_LENGTH
	 */
	public Library(String name) {
		super(name);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param l
	 *            a library object to duplicate
	 * @throws NullPointerException
	 *             if <code>l</code> is <code>null</code>
	 */
	public Library(Library l) {
		super(l);
	}

	/**
	 * Removes a file from this library.
	 * 
	 * @param f
	 *            a library file to remove
	 * @throws NullPointerException
	 *             is <code>f</code> is <code>null</code>
	 */
	public void removeFile(LibraryFile f) {
		removeChild(f);
	}

	/**
	 * Returns an unmodifiable set of library files. Return value will never be
	 * <code>null</code>. Beware of performance penalties when using this
	 * method. If you simply wish to iterate over a collection then used
	 * {@link #iterator()} method instead.
	 * 
	 * @return an unmodifiable set of library files
	 */
	public Set<LibraryFile> getFiles() {
		return Collections.unmodifiableSet(getChildren());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.entities.Container#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		/*
		 * Overriding just to check if given resource is a library.
		 */
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Library))
			return false;
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.entities.Resource#compareTo(hr.fer.zemris.vhdllab.entities.Resource)
	 */
	@Override
	public int compareTo(Container<LibraryFile, Library> o) {
		/*
		 * Overriding just to check if given resource is a library.
		 */
		if (this == o)
			return 0;
		if (o == null)
			throw new NullPointerException("Other library cant be null");
		if (!(o instanceof Library)) {
			throw new ClassCastException("Object is not of Library type");
		}
		return super.compareTo(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.entities.Container#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(30 + getChildren().size() * 30);
		sb.append("Library [").append(super.toString()).append("]");
		return sb.toString();
	}

}
