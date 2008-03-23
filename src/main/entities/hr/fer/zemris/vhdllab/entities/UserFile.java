package hr.fer.zemris.vhdllab.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "user_files", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"name", "userId" }) })
@NamedQueries(value = {
		@NamedQuery(name = UserFile.FIND_BY_NAME_QUERY, query = "select f from UserFile as f where f.userId = :userId and f.name = :name order by f.id"),
		@NamedQuery(name = UserFile.FIND_BY_USER_QUERY, query = "select f from UserFile as f where f.userId = :userId order by f.id") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserFile {

	public static final String FIND_BY_NAME_QUERY = "user.file.find.by.name";
	public static final String FIND_BY_USER_QUERY = "user.file.find.by.user";

	private Long id;
	private String userId;
	private String name;
	private String content;

	public UserFile() {
	}

	public UserFile(UserFile file) {
		this.id = file.getId();
		this.userId = file.getUserId();
		this.name = file.getName();
		this.content = file.getContent();
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

	@Basic()
	@Column(name = "content", length = 65535)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
		result = prime * result
				+ ((getName() == null) ? 0 : getName().toLowerCase().hashCode());
		result = prime * result
				+ ((getUserId() == null) ? 0 : getUserId().toLowerCase().hashCode());
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
		if (!(obj instanceof UserFile))
			return false;
		final UserFile other = (UserFile) obj;
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		/*
		 * NOTE: properties must be accessed through methods rather then fields
		 * because of the nature of persistence provider (they will most likely
		 * use CGLIB for lazy loading)
		 */
		StringBuilder sb = new StringBuilder(40);
		sb.append("User file: ").append(getName()).append(" [").append(getId()).append(
				"]").append(" by '").append(getUserId()).append("'");
		return sb.toString();
	}
}
