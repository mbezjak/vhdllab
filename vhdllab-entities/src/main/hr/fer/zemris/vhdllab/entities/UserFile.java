package hr.fer.zemris.vhdllab.entities;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@AttributeOverrides(value = {
		@AttributeOverride(name = "content", column = @Column(name = "content", length = 65535, nullable = false)),
		@AttributeOverride(name = "parent", column = @Column(name = "userId", length = 255, nullable = false)) })
@Table(name = "user_files", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"name", "userId" }) })
@NamedQueries(value = {
		@NamedQuery(name = UserFile.FIND_BY_NAME_QUERY, query = "select f from UserFile as f where f.userId = :userId and f.name = :name order by f.id"),
		@NamedQuery(name = UserFile.FIND_BY_USER_QUERY, query = "select f from UserFile as f where f.userId = :userId order by f.id") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserFile extends Resource<String> implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String FIND_BY_NAME_QUERY = "user.file.find.by.name";
	public static final String FIND_BY_USER_QUERY = "user.file.find.by.user";

	public UserFile() {
	}

	public UserFile(UserFile file) {
		super(file);
	}

	public String getUserId() {
		return getParent();
	}

	public void setUserId(String userId) {
		setParent(userId);
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(40);
		sb.append("User File [").append(super.toString()).append(", userId=")
				.append(getUserId()).append("]");
		return sb.toString();
	}
}
