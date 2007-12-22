package hr.fer.zemris.vhdllab.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "global_files")
@NamedQueries(value = {
		@NamedQuery(name = GlobalFile.FIND_BY_NAME_QUERY, query = "select f from GlobalFile as f where f.name = :name order by f.id"),
		@NamedQuery(name = GlobalFile.GET_ALL_QUERY, query = "select f from GlobalFile as f order by f.id") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GlobalFile implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String FIND_BY_NAME_QUERY = "global.file.find.by.name";
	public static final String GET_ALL_QUERY = "global.file.get.all";

	private Long id;
	private String name;
	private String content;

	public GlobalFile() {
	}

	public GlobalFile(GlobalFile file) {
		this.id = file.getId();
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
	@Column(name = "name", nullable = false, unique = true)
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
		if (!(obj instanceof GlobalFile))
			return false;
		final GlobalFile other = (GlobalFile) obj;
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
		StringBuilder sb = new StringBuilder(30);
		sb.append("Global file: ").append(getName()).append(" [").append(getId()).append(
				"]");
		return sb.toString();
	}
}
