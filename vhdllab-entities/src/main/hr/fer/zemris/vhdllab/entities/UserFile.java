package hr.fer.zemris.vhdllab.entities;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A user file stores preferences for a user.
 * 
 * @author Miro Bezjak
 * @since 31/1/2008
 * @version 1.0
 */
@Entity
@AttributeOverride(name = "content", column = @Column(name = "content", length = 65535, nullable = false))
@Table(name = "user_files", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"name", "userId" }) })
@NamedQueries(value = {
		@NamedQuery(name = UserFile.FIND_BY_NAME_QUERY, query = "select f from UserFile as f where f.userId = :userId and f.name = :name order by f.id"),
		@NamedQuery(name = UserFile.FIND_BY_USER_QUERY, query = "select f from UserFile as f where f.userId = :userId order by f.id") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserFile extends Resource implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String FIND_BY_NAME_QUERY = "user.file.find.by.name";
	public static final String FIND_BY_USER_QUERY = "user.file.find.by.user";

	private String userId;

	public UserFile() {
	}

	public UserFile(UserFile file) {
		super(file);
		this.userId = file.userId;
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
		if (!(obj instanceof UserFile))
			return false;
		if (!super.equals(obj)) {
			return false;
		}
		if (getId() != null) {
			return true;
		}
		final UserFile other = (UserFile) obj;
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
	 * @see hr.fer.zemris.vhdllab.entities.Resource#compareTo(hr.fer.zemris.vhdllab.entities.Resource)
	 */
	@Override
	public int compareTo(Resource o) {
		if (this == o)
			return 0;
		if (o == null)
			throw new NullPointerException("Other resource cant be null");
		if (!(o instanceof UserFile)) {
			throw new ClassCastException("Object is not of User file type");
		}
		final UserFile other = (UserFile) o;
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
		StringBuilder sb = new StringBuilder(50);
		sb.append("User File [").append(super.toString());
		sb.append(", userId=").append(getUserId());
		sb.append("]");
		return sb.toString();
	}
}
