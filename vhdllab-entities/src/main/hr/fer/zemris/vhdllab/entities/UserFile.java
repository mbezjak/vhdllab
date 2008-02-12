package hr.fer.zemris.vhdllab.entities;

import java.io.Serializable;

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
@Table(name = "user_files", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"name", "userId" }) })
@NamedQueries(value = {
		@NamedQuery(name = UserFile.FIND_BY_NAME_QUERY, query = "select f from UserFile as f where f.userId = :userId and f.name = :name order by f.id"),
		@NamedQuery(name = UserFile.FIND_BY_USER_QUERY, query = "select f from UserFile as f where f.userId = :userId order by f.id") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserFile extends Resource implements Ownable, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * A named query for finding user files by name.
	 */
	public static final String FIND_BY_NAME_QUERY = "user.file.find.by.name";
	/**
	 * A named query for finding all user files containing specified userId.
	 */
	public static final String FIND_BY_USER_QUERY = "user.file.find.by.user";

	@Basic
	@Column(name = "userId", length = USER_ID_LENGTH, nullable = false, updatable = false)
	private String userId;
	
	/**
	 * Constructor for persistence provider.
	 */
	UserFile() {
		super();
	}

	/**
	 * Creates a user file with specified userId, name and type. Content will be
	 * set to empty string.
	 * 
	 * @param userId
	 *            a user identifier that this user files belongs to
	 * @param name
	 *            a name of a user file
	 * @param type
	 *            a type of a user file
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if either parameter is too long
	 * @see #USER_ID_LENGTH
	 * @see #NAME_LENGTH
	 * @see #TYPE_LENGTH
	 * @see #CONTENT_LENGTH
	 */
	public UserFile(String userId, String name, String type) {
		this(userId, name, type, "");
	}

	/**
	 * Creates a user file with specified userId, name, type and content.
	 * 
	 * @param userId
	 *            a user identifier that this user files belongs to
	 * @param name
	 *            a name of a user file
	 * @param type
	 *            a type of a user file
	 * @param content
	 *            a content of a user file
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if either parameter is too long
	 * @see #USER_ID_LENGTH
	 * @see #NAME_LENGTH
	 * @see #TYPE_LENGTH
	 * @see #CONTENT_LENGTH
	 */
	public UserFile(String userId, String name, String type, String content) {
		super(name, type, content);
		if (userId == null) {
			throw new NullPointerException("User identifier cant be null");
		}
		if (userId.length() > USER_ID_LENGTH) {
			throw new IllegalArgumentException("User identifier must be <= "
					+ USER_ID_LENGTH + " but was: " + userId.length());
		}
		this.userId = userId;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param file
	 *            a user file object to duplicate
	 * @throws NullPointerException
	 *             if <code>file</code> is <code>null</code>
	 */
	public UserFile(UserFile file) {
		super(file);
		this.userId = file.userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.entities.Ownable#getUserId()
	 */
	@Override
	public String getUserId() {
		return userId;
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
		result = prime * result + getUserId().toLowerCase().hashCode();
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
		return getUserId().equalsIgnoreCase(other.getUserId());
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
			throw new NullPointerException("Other user file cant be null");
		if (!(o instanceof UserFile)) {
			throw new ClassCastException("Object is not of User file type");
		}
		final UserFile other = (UserFile) o;
		int val = super.compareTo(other);
		if (getId() != null || val != 0) {
			return val;
		}
		val = getUserId().compareToIgnoreCase(other.getUserId());

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
		sb.append("User File [userId=").append(getUserId());
		sb.append(", ").append(super.toString());
		sb.append("]");
		return sb.toString();
	}

}
