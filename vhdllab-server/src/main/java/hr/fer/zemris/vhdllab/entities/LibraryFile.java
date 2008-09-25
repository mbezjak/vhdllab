package hr.fer.zemris.vhdllab.entities;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A file that belongs to a library.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public final class LibraryFile extends Resource {

    private static final long serialVersionUID = 1L;

    private Library library;

    /**
     * Default constructor for persistence provider.
     */
    LibraryFile() {
        super();
    }

    /**
     * Constructs a file out of specified <code>name</code> and
     * <code>data</code>.
     * 
     * @param name
     *            a name of a file
     * @param data
     *            a file data
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             if either parameter is too long or <code>name</code> isn't
     *             correctly formatted
     * @see #NAME_LENGTH
     * @see #DATA_LENGTH
     */
    public LibraryFile(Caseless name, String data) {
        super(name, data);
        library = null;
    }

    /**
     * Copy constructor.
     * <p>
     * Note: <code>id</code> and <code>version</code> properties are not copied.
     * </p>
     * <p>
     * Note: reference to library is not duplicated.
     * </p>
     * 
     * @param file
     *            a file to duplicate
     * @throws NullPointerException
     *             if <code>file</code> is <code>null</code>
     */
    public LibraryFile(LibraryFile file) {
        super(file);
        library = null;
    }

    /**
     * Every name is correct library file name.
     * 
     * @return <code>true</code>
     */
    @Override
    protected boolean isCorrectName() {
        return true;
    }

    /**
     * Returns a library for this file. Return value will be <code>null</code>
     * only if file doesn't belong to any library.
     * 
     * @return a library for this file
     */
    public Library getLibrary() {
        return library;
    }

    /**
     * Sets a library for this file.
     * 
     * @param library
     *            a library to set; can be null
     */
    void setLibrary(Library library) {
        this.library = library;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(getLibrary())
                    .appendSuper(super.hashCode())
                    .toHashCode();
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
        LibraryFile other = (LibraryFile) obj;
        return new EqualsBuilder()
                    .append(getLibrary(), other.getLibrary())
                    .appendSuper(super.equals(obj))
                    .isEquals();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        Library l = getLibrary();
        Integer lId = null;
        Caseless lName = null;
        if (l != null) {
            lId = l.getId();
            lName = l.getName();
        }
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("libraryId", lId)
                    .append("libraryName", lName)
                    .toString();
    }

}
