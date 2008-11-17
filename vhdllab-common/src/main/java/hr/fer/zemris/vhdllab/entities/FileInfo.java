package hr.fer.zemris.vhdllab.entities;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A file info contains information about a file.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class FileInfo extends FileResource {

    private static final long serialVersionUID = 1L;

    private Integer projectId;

    /**
     * Default constructor for persistence provider.
     */
    FileInfo() {
        super();
    }

    /**
     * Constructs a file info out of specified <code>type</code>,
     * <code>name</code>, <code>data</code> and <code>projectId</code>.
     * 
     * @param type
     *            the type to set
     * @param name
     *            the name to set
     * @param data
     *            the data to set
     * @param projectId
     *            a project identifier that a file belongs to
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             if either parameter is too long or <code>name</code> isn't
     *             correctly formatted
     * @see #NAME_LENGTH
     * @see #DATA_LENGTH
     */
    public FileInfo(FileType type, Caseless name, String data, Integer projectId) {
        this(new FileResource(type, name, data), projectId);
    }

    /**
     * Constructs a file info out of specified <code>file</code> and
     * <code>projectId</code>.
     * 
     * @param file
     *            a file resource to duplicate
     * @param projectId
     *            a project identifier that a file belongs to
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     */
    public FileInfo(FileResource file, Integer projectId) {
        super(file);
        this.projectId = projectId;
        checkProperties();
    }

    /**
     * Copy constructor.
     * <p>
     * Note: <code>id</code> and <code>version</code> properties are not copied.
     * </p>
     * 
     * @param file
     *            a file to duplicate
     * @throws NullPointerException
     *             if <code>file</code> is <code>null</code>
     */
    public FileInfo(FileInfo file) {
        super(file);
        this.projectId = file.projectId;
    }

    /**
     * Ensures that properties are in correct state.
     * 
     * @throws NullPointerException
     *             if <code>projectId</code> is <code>null</code>
     */
    private void checkProperties() {
        if (projectId == null) {
            throw new NullPointerException("Project identifier can't be null");
        }
    }

    /**
     * Getter for project identifier.
     * 
     * @return the project identifier
     */
    public final Integer getProjectId() {
        return projectId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(projectId)
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
        if (!(obj instanceof FileInfo))
            return false;
        FileInfo other = (FileInfo) obj;
        return new EqualsBuilder()
                    .append(projectId, other.projectId)
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
                    .append("projectId", projectId)
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
