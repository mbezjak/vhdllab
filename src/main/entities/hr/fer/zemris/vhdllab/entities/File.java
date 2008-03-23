package hr.fer.zemris.vhdllab.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "files", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"name", "project_id" }) })
@NamedQueries(value = { @NamedQuery(name = File.FIND_BY_NAME_QUERY, query = "select f from File as f where f.name = :name and f.project.id = :id order by f.id") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class File implements Comparable<File> {

	public static final String FIND_BY_NAME_QUERY = "file.find.by.name";

	private Long id;
	private String name;
	private String type;
	private String content;
	private Project project;

	public File() {
	}

	public File(File f) {
		id = f.getId();
		name = f.getName();
		type = f.getType();
		content = f.getContent();
		project = f.getProject();
		if (project != null) {
			project.addFile(this);
		}
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
	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Basic
	@Column(name = "type", nullable = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Note that column length is only theoretically the amount specified. This
	 * is because some DBMS won't listen to specified length. e.g. MySQL will
	 * set data type to 'mediumtext' which has fixed amount of characters
	 * (16,777,215 (2^24 – 1) characters). Also this is huge amount of data
	 * (16MB) and when sending data over network (local or internet) DBMS could
	 * refuse to accept it. In MySQL this can be controlled by
	 * 'max_allowed_packet' server variable which is by default set to less then
	 * 'mediumtext' size (default value is 16,776,192).
	 */
	@Basic()
	@Column(name = "content", length = 16000000)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false, updatable = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
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
		/*
		 * NOTE: properties must be accessed through methods rather then fields
		 * because of the nature of persistence provider (they will most likely
		 * use CGLIB for lazy loading)
		 */
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof File))
			return false;
		final File other = (File) obj;
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
	public int compareTo(File other) {
		/*
		 * NOTE: properties must be accessed through methods rather then fields
		 * because of the nature of persistence provider (they will most likely
		 * use CGLIB for lazy loading)
		 */
		if (this == other)
			return 0;
		if (other == null)
			throw new NullPointerException("File cant be null");

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
		if (getProject() == null) {
			if (other.getProject() != null)
				return -1;
		} else if (other.getProject() == null) {
			return 1;
		} else {
			val = getProject().compareTo(other.getProject());
			if (val < 0)
				return -1;
			else if (val > 0)
				return 1;
		}

		// rest is invoked if both projects are null or equal
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
		StringBuilder sb = new StringBuilder(50);
		sb.append("File: ").append(getName()).append(".").append(getType())
				.append(" [").append(getId()).append("] / ");
		if (getProject() != null) {
			sb.append(getProject().getName());
		} else {
			sb.append("null");
		}
		return sb.toString();
	}

}