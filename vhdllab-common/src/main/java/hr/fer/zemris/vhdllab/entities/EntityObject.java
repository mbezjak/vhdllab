package hr.fer.zemris.vhdllab.entities;

import hr.fer.zemris.vhdllab.api.util.StringFormat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class defines properties that every entity must have.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class EntityObject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Maximum length of a name.
     */
    public static final int NAME_LENGTH = 255;

    private Integer id;
    private Integer version;
    private Caseless name;

    /**
     * Default constructor for persistence provider.
     */
    protected EntityObject() {
    }

    /**
     * Constructs an entity object out of specified <code>name</code>.
     * 
     * @param name
     *            the name to set
     * @throws NullPointerException
     *             if <code>name</code> is <code>null</code>
     * @throws IllegalArgumentException
     *             if <code>name</code> is too long or isn't correctly formatted
     * @see #isCorrectName()
     * @see #NAME_LENGTH
     */
    protected EntityObject(Caseless name) {
        super();
        this.name = name;
        checkProperties();
    }

    /**
     * Copy constructor.
     * <p>
     * Note: <code>id</code> and <code>version</code> properties are not copied.
     * </p>
     * 
     * @param entity
     *            an entity to duplicate
     * @param deepCopy
     *            if <code>id</code> and <code>version</code> properties should
     *            also be copied
     * @throws NullPointerException
     *             if <code>entity</code> is <code>null</code>
     */
    protected EntityObject(EntityObject entity, boolean deepCopy) {
        super();
        this.name = entity.name;
        if(deepCopy) {
            this.id = entity.id;
            this.version = entity.version;
        }
    }

    /**
     * Ensures that properties are in correct state.
     * 
     * @throws NullPointerException
     *             if <code>name</code> is <code>null</code>
     * @throws IllegalArgumentException
     *             if <code>name</code> is too long or isn't correctly formatted
     * @see #isCorrectName()
     */
    private void checkProperties() {
        if (name == null) {
            throw new NullPointerException("Name can't be null");
        }
        if (name.length() > NAME_LENGTH) {
            throw new IllegalArgumentException("Name length must be <= "
                    + NAME_LENGTH + " but was: " + name.length());
        }
        if (!isCorrectName()) {
            throw new IllegalArgumentException("'" + name
                    + "' isn't correctly formatted name");
        }
    }

    /**
     * Returns boolean indicating if <code>name</code> is correctly formatted.
     * <p>
     * Note: for most entities correct name is dictated by
     * {@link StringFormat#isCorrectEntityName(String)} method.
     * </p>
     * 
     * @return boolean indicating if <code>name</code> is correctly formatted
     * @see StringFormat#isCorrectEntityName(String)
     */
    protected boolean isCorrectName() {
        return StringFormat.isCorrectEntityName(name.toString());
    }

    /**
     * Getter for id.
     * 
     * @return the id
     */
    public final Integer getId() {
        return id;
    }

    /**
     * Getter for version.
     * 
     * @return the version
     */
    public final Integer getVersion() {
        return version;
    }

    /**
     * Getter for name.
     * 
     * @return the name
     */
    public final Caseless getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(name)
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
        if (!(obj instanceof EntityObject))
            return false;
        EntityObject other = (EntityObject) obj;
        return new EqualsBuilder()
                    .append(name, other.name)
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
                    .append("id", id)
                    .append("version", version)
                    .append("name", name)
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
