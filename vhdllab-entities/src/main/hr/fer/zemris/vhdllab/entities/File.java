package hr.fer.zemris.vhdllab.entities;

import javax.persistence.AssociationOverride;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A file that belongs to a project.
 * 
 * @author Miro Bezjak
 * @since 31/1/2008
 * @version 1.0
 */
@Entity
@AssociationOverride(name = "parent", joinColumns = { @JoinColumn(name = "project_id", nullable = false, updatable = false) })
@Table(name = "files", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"name", "project_id" }) })
@NamedQuery(name = File.FIND_BY_NAME_QUERY, query = "select f from File as f where f.name = :name and f.project.id = :id order by f.id")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class File extends BidiResource<Project, File> {

	private static final long serialVersionUID = 1L;
	public static final String FIND_BY_NAME_QUERY = "file.find.by.name";

	public File() {
		super();
	}

	public File(File f) {
		super(f);
	}

	@Transient
	public Project getProject() {
		return getParent();
	}

	public void setProject(Project project) {
		setParent(project);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		if (getId() != null) {
			return result;
		}
		result = prime * result
				+ ((getProject() == null) ? 0 : getProject().hashCode());
		return result;
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
		if (!super.equals(obj)) {
			return false;
		}
		if (getId() != null) {
			return true;
		}
		final File other = (File) obj;
		if (getProject() == null) {
			if (other.getProject() != null)
				return false;
		} else if (!getProject().equals(other.getProject()))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(File o) {
		if (this == o)
			return 0;
		if (o == null)
			throw new NullPointerException("Other file cant be null");
		if (!(o instanceof File)) {
			throw new ClassCastException("Object is not of File type");
		}
		final File other = o;
		int val = super.compareTo(other);
		if (getId() != null || val != 0) {
			return val;
		}
		if (getProject() == null) {
			if (other.getProject() != null)
				return -1;
		} else if (other.getProject() == null) {
			return 1;
		} else {
			val = getProject().compareTo(other.getProject());
		}

		if (val < 0) {
			return -1;
		} else if (val > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(50);
		sb.append("File [").append(super.toString());
		Project project = getProject();
		if (project != null) {
			sb.append(", projectId=").append(project.getId());
			sb.append(", projectName=").append(project.getName());
		}
		sb.append("]");
		return sb.toString();
	}

}