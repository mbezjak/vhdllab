package hr.fer.zemris.vhdllab.entities;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Project is a container for library files.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class LibraryInfo extends EntityObject {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for persistence provider.
     */
    LibraryInfo() {
        super();
    }

    /**
     * Constructs a project out of specified <code>name</code>.
     * 
     * @param name
     *            the name to set
     * @throws NullPointerException
     *             if <code>name</code> is <code>null</code>
     * @throws IllegalArgumentException
     *             if <code>name</code> is too long or <code>name</code> isn't
     *             correctly formatted
     * @see #isCorrectName()
     * @see #NAME_LENGTH
     */
    protected LibraryInfo(Caseless name) {
        super(name);
    }

    /**
     * Copy constructor.
     * <p>
     * Note: <code>id</code> and <code>version</code> properties are not copied.
     * </p>
     * 
     * @param library
     *            a project to duplicate
     * @throws NullPointerException
     *             if <code>library</code> is <code>null</code>
     */
    public LibraryInfo(LibraryInfo library) {
        super(library);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .toString();
    }

}
