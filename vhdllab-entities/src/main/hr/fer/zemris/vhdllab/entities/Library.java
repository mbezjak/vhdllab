package hr.fer.zemris.vhdllab.entities;

import javax.persistence.Entity;

/**
 * A library is a collection of library files. Libraries are stored on file
 * system and for that a custom dao implementation is written. Because of that
 * this class is not an entity (at least not {@link Entity})!
 * 
 * @author Miro Bezjak
 * @since 31/1/2008
 * @version 1.0
 */
public class Library extends Container<LibraryFile, Library> {

	private static final long serialVersionUID = 1L;

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
	public void removeLibraryFile(LibraryFile f) {
		removeChild(f);
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
