package hr.fer.zemris.vhdllab.entities;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A file resource extends Resource by adding file type property.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
class FileResource extends Resource {

    private static final long serialVersionUID = 1L;

    private FileType type;

    /**
     * Default constructor for persistence provider.
     */
    FileResource() {
        super();
    }

    /**
     * Constructs a file resource out of specified <code>type</code>,
     * <code>name</code> and <code>data</code>.
     * 
     * @param type
     *            the type to set
     * @param name
     *            the name to set
     * @param data
     *            the data to set
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             if either parameter is too long or <code>name</code> isn't
     *             correctly formatted
     * @see #isCorrectName()
     * @see #NAME_LENGTH
     * @see #DATA_LENGTH
     */
    protected FileResource(FileType type, Caseless name, String data) {
        super(name, data);
        this.type = type;
        checkProperties();
    }

    /**
     * Copy constructor.
     * <p>
     * Note: <code>id</code> and <code>version</code> properties are not copied.
     * </p>
     * 
     * @param file
     *            a file resource to duplicate
     * @throws NullPointerException
     *             if <code>file</code> is <code>null</code>
     */
    protected FileResource(FileResource file) {
        super(file);
        this.type = file.type;
    }

    /**
     * Ensures that properties are in correct state.
     * 
     * @throws NullPointerException
     *             if <code>type</code> is <code>null</code>
     */
    private void checkProperties() {
        if (type == null) {
            throw new NullPointerException("Type can't be null");
        }
    }

    /**
     * Getter for type.
     * 
     * @return the type
     */
    public final FileType getType() {
        return type;
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
                    .append("type", type)
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
