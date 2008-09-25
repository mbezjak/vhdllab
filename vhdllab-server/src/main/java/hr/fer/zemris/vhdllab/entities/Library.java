package hr.fer.zemris.vhdllab.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

/**
 * A library is a collection of library files.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public final class Library extends LibraryInfo {

    private static final long serialVersionUID = 1L;

    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<LibraryFile> files;

    /**
     * Default constructor for persistence provider.
     */
    Library() {
        super();
        initFiles();
    }

    /**
     * Constructs a library out of specified <code>name</code>.
     * 
     * @param name
     *            a name of a library
     * @throws NullPointerException
     *             if <code>name</code> is <code>null</code>
     * @throws IllegalArgumentException
     *             if <code>name</code> is too long or isn't correctly formatted
     * @see #NAME_LENGTH
     */
    public Library(Caseless name) {
        super(name);
        initFiles();
    }

    /**
     * Copy constructor.
     * <p>
     * Note: <code>id</code> and <code>version</code> properties are not copied.
     * </p>
     * <p>
     * Note: files are not duplicated to reduce aliasing problems.
     * </p>
     * 
     * @param library
     *            a library to duplicate
     * @throws NullPointerException
     *             if <code>library</code> is <code>null</code>
     */
    public Library(Library library) {
        super(library);
        /*
         * Files is not referenced to reduce aliasing problems!
         */
        initFiles();
    }

    /**
     * Initializes file collection.
     */
    private void initFiles() {
        this.files = new HashSet<LibraryFile>();
    }

    /**
     * Adds a <code>file</code> to a this library.
     * 
     * @param file
     *            a file to add
     * @throws NullPointerException
     *             if <code>file</code> is <code>null</code>
     * @throws IllegalArgumentException
     *             if a <code>file</code> already belongs to a library
     */
    public void addFile(LibraryFile file) {
        if (file == null) {
            throw new NullPointerException("File can't be null");
        }
        if (file.getLibrary() != null) {
            if (file.getLibrary() == this) {
                // a file is already in this library
                return;
            }
            throw new IllegalArgumentException(file.toString()
                    + " already belongs to a library; this library is "
                    + this.toString());
        }
        /*
         * Must set a library to a file before adding it to a collection because
         * library is required for hash code calculation (in library file).
         * 
         * @see LibraryFile#hashCode()
         */
        file.setLibrary(this);
        getFiles().add(file);
    }

    /**
     * Removes a file from this library.
     * 
     * @param file
     *            a file to remove
     * @throws NullPointerException
     *             is <code>file</code> is <code>null</code>
     */
    public void removeFile(LibraryFile file) {
        if (file == null) {
            throw new NullPointerException("File can't be null");
        }
        if (file.getLibrary() != this) {
            throw new IllegalArgumentException(file.toString()
                    + " doesn't belong to this " + this.toString());
        }
        // referencing though getter because of lazy loading
        if (getFiles().remove(file)) {
            file.setLibrary(null);
        }
    }

    /**
     * Returns a set of library files. Return value will never be
     * <code>null</code>.
     * <p>
     * Note: although this method returns direct reference to library files (and
     * not unmodifiable proxy) adding and deleting a library file <b>must</b> be
     * done using {@link #addFile(LibraryFile)} and
     * {@link #removeFile(LibraryFile)} methods.
     * </p>
     * 
     * @return a set of library files
     */
    public Set<LibraryFile> getFiles() {
        return files;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        // referencing though getter because of lazy loading
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("files", getFiles(), false)
                    .toString();
    }

    /**
     * Ensures that properties are in correct state after deserialization.
     */
    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        if (files == null) {
            throw new NullPointerException("Files can't be null");
        }
    }

}
