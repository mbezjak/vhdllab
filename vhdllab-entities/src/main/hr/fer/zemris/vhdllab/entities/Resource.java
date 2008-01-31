package hr.fer.zemris.vhdllab.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This is a superclass for all resources. Basically it defines all properties
 * that a resource must have.
 * 
 * @author Miro Bezjak
 * @since 31/1/2008
 * @version 1.0
 */
@MappedSuperclass
class Resource implements Serializable, Comparable<Resource> {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String type;
	private String content;
	private Date created;

	public Resource() {
	}

	public Resource(Resource r) {
		this.id = r.id;
		this.name = r.name;
		this.type = r.type;
		this.content = r.content;
		this.created = r.created;
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
	@Column(name = "name", length = 255, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Basic
	@Column(name = "type", length = 255, nullable = false, updatable = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * Note that column length is only theoretically the amount specified. This
	 * is because some DBMS won't listen to specified length. e.g. MySQL will
	 * set data type to 'mediumtext' which has fixed amount of characters
	 * (16,777,215 (2^24 – 1) characters). Also this is huge amount of data
	 * (16MB) and when sending data over network (local or Internet) DBMS could
	 * refuse to accept it. In MySQL this can be controlled by
	 * 'max_allowed_packet' server variable which is by default set to less then
	 * 'mediumtext' size (default value is 16,776,192).
	 */
	@Basic
	@Column(name = "content", length = 16000000, nullable = false)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false, updatable = false)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (id != null) {
			return prime * result + id.hashCode();
		}
		result = prime * result
				+ ((name == null) ? 0 : name.toLowerCase().hashCode());
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
		if (!(obj instanceof Resource))
			return false;
		final Resource other = (Resource) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (id.equals(other.id))
			return true;

		// rest is invoked if both ids are null
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equalsIgnoreCase(other.name))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Resource other) {
		if (this == other)
			return 0;
		if (other == null)
			throw new NullPointerException("Other resource cant be null");

		if (id == null) {
			if (other.id != null)
				return -1;
		} else if (other.id == null) {
			return 1;
		} else {
			return id.compareTo(other.id);
		}

		// rest is invoked if both ids are null
		long val = 0;
		if (name == null) {
			if (other.name != null)
				return -1;
		} else if (other.name == null) {
			return 1;
		} else {
			val = name.compareToIgnoreCase(other.name);
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
		StringBuilder sb = new StringBuilder(30);
		sb.append("id=").append(id);
		sb.append(", name=").append(name);
		sb.append(", type=").append(type);
		sb.append(", created=").append(created);
		return sb.toString();
	}

}
