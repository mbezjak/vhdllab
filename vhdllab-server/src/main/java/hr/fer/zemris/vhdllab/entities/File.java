package hr.fer.zemris.vhdllab.entities;

import javax.persistence.EntityListeners;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A file that belongs to a project.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityListeners(HistoryListener.class)
public final class File extends FileResource {

    private static final long serialVersionUID = 1L;

    private Project project;
    @Transient
    private Project postRemoveProjectReference;

    /**
     * Default constructor for persistence provider.
     */
    File() {
        super();
    }

    /**
     * Constructs a file out of specified <code>type</code> and
     * <code>name</code> and <code>data</code>.
     * 
     * @param type
     *            a type of a file
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
    public File(FileType type, Caseless name, String data) {
        super(type, name, data);
        project = null;
    }

    /**
     * Copy constructor.
     * <p>
     * Note: <code>id</code> and <code>version</code> properties are not copied.
     * </p>
     * <p>
     * Note: reference to project is not duplicated.
     * </p>
     * 
     * @param file
     *            a file to duplicate
     * @throws NullPointerException
     *             if <code>file</code> is <code>null</code>
     */
    public File(File file) {
        super(file);
        project = null;
    }

    /**
     * Returns a project for this file. Return value will be <code>null</code>
     * only if file doesn't belong to any project.
     * 
     * @return a project for this file
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets a project for this file.
     * 
     * @param project a project to set; can be null
     */
    void setProject(Project project) {
        if(project == null) {
            setPostRemoveProjectReference(this.project);
        }
        this.project = project;
    }

    /**
     * This is a leftover reference to a project that this file used to belong.
     * When a file wants to be removed it need to be detached from project-file
     * relationship by calling {@link Project#removeFile(File)} method. This
     * will set project (property) to null. However this information is still
     * required on post remove event (see {@link HistoryListener}) in order to
     * set deletedOn timestamp on {@link FileHistory}. For that reason this
     * property holds a reference to project even after project-file
     * relationship (for this file) has been severed.
     * <p>
     * Please note that this method will destroy reference to a project once its
     * been called. In other words first time its called it will return
     * reference to a project but each other time it will return
     * <code>null</code>. Reason for this is to minimize problem with garbage
     * collection.
     * </p>
     * <p>
     * THIS METHOD SHOULD NOT BE CALLED BY ANY OTHER THEN POST REMOVE EVENT
     * METHOD IN {@link HistoryListener}!!
     * </p>
     * 
     * @return the post remove project reference or <code>null</code> if it has
     *         not been set
     */
    Project getPostRemoveProjectReference() {
        if(this.postRemoveProjectReference != null) {
            Project projectReference = this.postRemoveProjectReference;
            setPostRemoveProjectReference(null);
            return projectReference;
        }
        return null;
    }

    /**
     * Sets a post remove project reference
     * 
     * @param project the post remove project reference to set
     * @see #getPostRemoveProjectReference()
     */
    void setPostRemoveProjectReference(Project project) {
        this.postRemoveProjectReference = project;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(getProject())
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
        if (!(obj instanceof File))
            return false;
        File other = (File) obj;
        return new EqualsBuilder()
                    .append(getProject(), other.getProject())
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
        Project p = getProject();
        Integer pId = null;
        Caseless pUserId = null;
        Caseless pName = null;
        if (p != null) {
            pId = p.getId();
            pUserId = p.getUserId();
            pName = p.getName();
        }
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("projectId", pId)
                    .append("projectUserId", pUserId)
                    .append("projectName", pName)
                    .toString();
    }

}
