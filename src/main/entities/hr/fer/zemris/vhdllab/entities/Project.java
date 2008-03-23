package hr.fer.zemris.vhdllab.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "projects", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"name", "userId" }) })
@NamedQueries(value = {
		@NamedQuery(name = Project.FIND_BY_NAME_QUERY, query = "select p from Project as p where p.userId = :userId and p.name = :name order by p.id"),
		@NamedQuery(name = Project.FIND_BY_USER_QUERY, query = "select p from Project as p where p.userId = :userId order by p.id") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project implements Comparable<Project> {

	public static final String FIND_BY_NAME_QUERY = "project.find.by.name";
	public static final String FIND_BY_USER_QUERY = "project.find.by.user";

	private Long id;
	private String userId;
	private String name;
	private Set<File> files;

	public Project() {
		files = new HashSet<File>();
	}

	public Project(Project p) {
		id = p.getId();
		name = p.getName();
		userId = p.getUserId();
		files = p.getFiles();
	}

	@Id
	@GeneratedValue
	@Column(name = "id", updatable = false, insertable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "userId", nullable = false)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Basic
	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "project", fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Set<File> getFiles() {
		return files;
	}

	public void setFiles(Set<File> files) {
		this.files = files;
	}

	public void addFile(File f) {
		if (f.getProject() != null) {
			f.getProject().removeFile(f);
		}
		f.setProject(this);
		files.add(f);
	}

	public void removeFile(File f) {
		files.remove(f);
		f.setProject(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		/*
		 * NOTE: properties must be accessed through methods rather then fields
		 * because of the nature of persistence provider (they will most likely
		 * use CGLIB for lazy loading)
		 */
		final int prime = 31;
		int result = 1;
		if (getId() != null) {
			return prime * result + getId().hashCode();
		}
		result = prime
				* result
				+ ((getName() == null) ? 0 : getName().toLowerCase().hashCode());
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
		/*
		 * NOTE: properties must be accessed through methods rather then fields
		 * because of the nature of persistence provider (they will most likely
		 * use CGLIB for lazy loading)
		 */
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Project))
			return false;
		final Project other = (Project) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (getId().equals(other.getId()))
			return true;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equalsIgnoreCase(other.getName()))
			return false;
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
	public int compareTo(Project other) {
		/*
		 * NOTE: properties must be accessed through methods rather then fields
		 * because of the nature of persistence provider (they will most likely
		 * use CGLIB for lazy loading)
		 */
		if (this == other)
			return 0;
		if (other == null)
			throw new NullPointerException("Project cant be null");

		if (getId() == null) {
			if (other.getId() != null)
				return -1;
		} else if (other.getId() == null) {
			return 1;
		} else {
			return getId().compareTo(other.getId());
		}

		long val = 0;
		// rest is invoked if both ids are null
		if (getUserId() == null) {
			if (other.getUserId() != null)
				return -1;
		} else if (other.getUserId() == null) {
			return 1;
		} else {
			val = getUserId().compareToIgnoreCase(other.getUserId());
			if (val < 0)
				return -1;
			else if (val > 0)
				return 1;
		}

		// rest is invoked if both userIds are null or equal
		if (getName() == null) {
			if (other.getName() != null)
				return -1;
		} else if (other.getName() == null) {
			return 1;
		} else {
			val = getName().compareToIgnoreCase(other.getName());
		}

		if (val < 0)
			return -1;
		else if (val > 0)
			return 1;
		else
			return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		/*
		 * NOTE: properties must be accessed through methods rather then fields
		 * because of the nature of persistence provider (they will most likely
		 * use CGLIB for lazy loading)
		 */
		StringBuilder sb = new StringBuilder(50 + getFiles().size() * 30);
		sb.append("Project: ").append(getName()).append(" [").append(getId()).append(
				"] by '").append(getUserId()).append("' has ").append(getFiles().size())
				.append(" files:\n");
		for (File f : getFiles()) {
			sb.append(f).append("\n");
		}
		return sb.toString();
	}
}