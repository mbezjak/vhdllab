package hr.fer.zemris.vhdllab.entities;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A library file info contains information about a library file.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class LibraryFileInfo extends Resource {

    private static final long serialVersionUID = 1L;

    private Integer libraryId;

    /**
     * Default constructor for persistence provider.
     */
    LibraryFileInfo() {
        super();
    }

    /**
     * Constructs a library file info out of specified <code>file</code> and
     * <code>libraryId</code>.
     * 
     * @param file
     *            a resource to duplicate
     * @param libraryId
     *            a library identifier that a file belongs to
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     */
    public LibraryFileInfo(Resource file, Integer libraryId) {
        super(file);
        this.libraryId = libraryId;
        checkProperties();
    }

    /**
     * Copy constructor.
     * <p>
     * Note: <code>id</code> and <code>version</code> properties are not copied.
     * </p>
     * 
     * @param file
     *            a library file to duplicate
     * @throws NullPointerException
     *             if <code>file</code> is <code>null</code>
     */
    public LibraryFileInfo(LibraryFileInfo file) {
        super(file);
        this.libraryId = file.libraryId;
    }

    /**
     * Ensures that properties are in correct state.
     * 
     * @throws NullPointerException
     *             if <code>libraryId</code> is <code>null</code>
     */
    private void checkProperties() {
        if (libraryId == null) {
            throw new NullPointerException("Library identifier can't be null");
        }
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
     * Getter for library identifier.
     * 
     * @return the library identifier
     */
    public final Integer getLibraryId() {
        return libraryId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(libraryId)
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
        if (!(obj instanceof LibraryFileInfo))
            return false;
        LibraryFileInfo other = (LibraryFileInfo) obj;
        return new EqualsBuilder()
                    .append(libraryId, other.libraryId)
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
        return new ToStringBuilder(this)
                    .append("libraryId", libraryId)
                    .appendSuper(super.toString())
                    .toString();
    }

    /**
     * Ensures that properties are in correct state after deserialization.
     */
    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        checkProperties();
    }

}
