package hr.fer.zemris.vhdllab.entities;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Project is a container for files.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ProjectInfo extends EntityObject implements Ownable {

    private static final long serialVersionUID = 1L;

    private Caseless userId;

    /**
     * Default constructor for persistence provider.
     */
    ProjectInfo() {
        super();
    }

    /**
     * Constructs a project out of specified <code>userId</code> and
     * <code>name</code>.
     * 
     * @param userId
     *            the user identifier to set
     * @param name
     *            the name to set
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             if either parameter is too long or <code>name</code> isn't
     *             correctly formatted
     * @see #USER_ID_LENGTH
     * @see #NAME_LENGTH
     */
    public ProjectInfo(Caseless userId, Caseless name) {
        super(name);
        this.userId = userId;
        checkProperties();
    }

    /**
     * Copy constructor.
     * <p>
     * Note: <code>id</code> and <code>version</code> properties are not copied.
     * </p>
     * 
     * @param project
     *            a project to duplicate
     * @throws NullPointerException
     *             if <code>project</code> is <code>null</code>
     */
    public ProjectInfo(ProjectInfo project) {
        super(project);
        this.userId = project.userId;
    }

    /**
     * Ensures that properties are in correct state.
     * 
     * @throws NullPointerException
     *             if <code>userId</code> is <code>null</code>
     * @throws IllegalArgumentException
     *             if <code>userId</code> is too long
     */
    private void checkProperties() {
        if (userId == null) {
            throw new NullPointerException("User identifier can't be null");
        }
        if (userId.length() > USER_ID_LENGTH) {
            throw new IllegalArgumentException(
                    "User identifier length must be <= " + USER_ID_LENGTH
                            + " but was: " + userId.length());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.api.entities.Ownable#getUserId()
     */
    @Override
    public final Caseless getUserId() {
        return userId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(userId)
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
        if (!(obj instanceof ProjectInfo))
            return false;
        ProjectInfo other = (ProjectInfo) obj;
        return new EqualsBuilder()
                    .append(userId, other.userId)
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
                    .append("userId", userId)
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
