package hr.fer.zemris.vhdllab.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "files", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "project_id", "name" }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
// @EntityListeners(HistoryListener.class)
public class File extends FileInfo {

    private static final long serialVersionUID = -2001411661196642883L;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "project_id", updatable = false)
    private Project project;
    private transient Project postRemoveProjectReference;

    public File() {
        super();
    }

    public File(String name, FileType type, String data) {
        super(name, type, data);
    }

    public File(File clone) {
        super(clone);
        setProject(null);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        if (project == null) {
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
        if (this.postRemoveProjectReference != null) {
            Project projectReference = this.postRemoveProjectReference;
            setPostRemoveProjectReference(null);
            return projectReference;
        }
        return null;
    }

    /**
     * Sets a post remove project reference
     * 
     * @param project
     *            the post remove project reference to set
     * @see #getPostRemoveProjectReference()
     */
    void setPostRemoveProjectReference(Project project) {
        this.postRemoveProjectReference = project;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .appendSuper(super.hashCode())
                    .append(getProject())
                    .toHashCode();
    }

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
                    .appendSuper(super.equals(obj))
                    .append(getProject(), other.getProject())
                    .isEquals();
    }

    @Override
    public String toString() {
        Project p = getProject();
        Integer pId = null;
        String pUserId = null;
        String pName = null;
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
