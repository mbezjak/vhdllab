package hr.fer.zemris.vhdllab.entities;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A project is a collection of files.
 * 
 * @author Miro Bezjak
 * @since 31/1/2008
 * @version 1.0
 */
@Entity
@Table(name = "projects", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"name", "userId" }) })
@NamedQueries(value = {
		@NamedQuery(name = Project.FIND_BY_NAME_QUERY, query = "select p from Project as p where p.userId = :userId and p.name = :name order by p.id"),
		@NamedQuery(name = Project.FIND_BY_USER_QUERY, query = "select p from Project as p where p.userId = :userId order by p.id") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project extends Container<File, Project> {

	private static final long serialVersionUID = 1L;
	public static final String FIND_BY_NAME_QUERY = "project.find.by.name";
	public static final String FIND_BY_USER_QUERY = "project.find.by.user";
	
	private String userId;

	public Project() {
		super();
	}

	public Project(Project p) {
		super(p);
		this.userId = p.getUserId();
	}

	@Override
	public void addChild(File child) {
		/*
		 * Overridden method to set parent for a child
		 */
		super.addChild(child);
		child.setParent(this);
	}

	public void addFile(File f) {
		addChild(f);
	}

	public void removeFile(File f) {
		removeChild(f);
	}

	@Transient
	public Set<File> getFiles() {
		return getChildren();
	}
	
	public void setFiles(Set<File> files) {
		setChildren(files);
	}

	@Basic
	@Column(name = "userId", length = 255, nullable = false, updatable = false)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
		result = prime
				* result
				+ ((getUserId() == null) ? 0 : getUserId().toLowerCase()
						.hashCode());
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
		if (!(obj instanceof Project))
			return false;
		if (!super.equals(obj)) {
			return false;
		}
		if (getId() != null) {
			return true;
		}
		final Project other = (Project) obj;
		if (getUserId() == null) {
			if (other.getUserId() != null)
				return false;
		} else if (!getUserId().equalsIgnoreCase(other.getUserId()))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Container<File, Project> o) {
		if (this == o)
			return 0;
		if (o == null)
			throw new NullPointerException("Other project cant be null");
		if (!(o instanceof Project)) {
			throw new ClassCastException("Object is not of Project type");
		}
		final Project other = (Project) o;
		int val = super.compareTo(other);
		if (getId() != null || val != 0) {
			return val;
		}
		if (getUserId() == null) {
			if (other.getUserId() != null)
				return -1;
		} else if (other.getUserId() == null) {
			return 1;
		} else {
			val = getUserId().compareToIgnoreCase(other.getUserId());
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
		StringBuilder sb = new StringBuilder(30 + getFiles().size() * 30);
		sb.append("Project [").append(super.toString()).append("]");
		return sb.toString();
	}
}