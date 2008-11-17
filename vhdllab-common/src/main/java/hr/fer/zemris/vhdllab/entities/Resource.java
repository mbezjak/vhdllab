package hr.fer.zemris.vhdllab.entities;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A resource fully implements DataContainer.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class Resource extends EntityObject implements DataContainer {

    private static final long serialVersionUID = 1L;

    private String data;

    /**
     * Default constructor for persistence provider.
     */
    protected Resource() {
        super();
    }

    /**
     * Constructs a resource out of specified <code>name</code> and
     * <code>data</code>.
     * 
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
    protected Resource(Caseless name, String data) {
        super(name);
        setData(data);
    }

    /**
     * Copy constructor.
     * <p>
     * Note: <code>id</code> and <code>version</code> properties are not copied.
     * </p>
     * 
     * @param resource
     *            a resource to duplicate
     * @throws NullPointerException
     *             if <code>resource</code> is <code>null</code>
     */
    protected Resource(Resource resource) {
        super(resource);
        this.data = resource.data;
    }

    /**
     * Ensures that properties are in correct state.
     * 
     * @throws NullPointerException
     *             if <code>data</code> is <code>null</code>
     * @throws IllegalArgumentException
     *             if <code>data</code> is too long
     */
    private void checkProperties() {
        if (data == null) {
            throw new NullPointerException("Data can't be null");
        }
        if (data.length() > DATA_LENGTH) {
            throw new IllegalArgumentException("Data length must be <= "
                    + DATA_LENGTH + " but was: " + data.length());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.api.entities.DataContainer#getData()
     */
    @Override
    public final String getData() {
        return data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.api.entities.DataContainer#setData(java.lang.String
     * )
     */
    @Override
    public final void setData(String data) {
        this.data = data;
        checkProperties();
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
                    .append("dataLength", data.length())
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
