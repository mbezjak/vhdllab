package hr.fer.zemris.vhdllab.entities;

import java.util.Set;

/**
 * A library is a collection of library files. Libraries are stored on file
 * system and for that a custom dao implementation is written. Because of that
 * this class is not an entity!
 * 
 * @author Miro Bezjak
 * @since 31/1/2008
 * @version 1.0
 */
public class Library extends Container<LibraryFile, Library> {

	private static final long serialVersionUID = 1L;

	public Library() {
		super();
	}

	public Library(Library l) {
		super(l);
	}

	@Override
	public void addChild(LibraryFile child) {
		/*
		 * Overridden method to set parent for a child
		 */
		super.addChild(child);
		child.setParent(this);
	}

	public void addLibraryFile(LibraryFile f) {
		addChild(f);
	}

	public void removeLibraryFile(LibraryFile f) {
		removeChild(f);
	}

	public Set<LibraryFile> getLibraryFiles() {
		return getChildren();
	}

	public void setLibraryFiles(Set<LibraryFile> files) {
		setChildren(files);
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
		StringBuilder sb = new StringBuilder(30 + getLibraryFiles().size() * 30);
		sb.append("Library [").append(super.toString()).append("]");
		return sb.toString();
	}

}
