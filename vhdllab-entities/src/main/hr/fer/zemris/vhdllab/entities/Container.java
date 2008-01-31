package hr.fer.zemris.vhdllab.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * This is a generic class for all containers. Basically it defines all
 * properties that a container must have. A container simply contains a
 * collection of resources.
 * 
 * @author Miro Bezjak
 * @since 31/1/2008
 * @version 1.0
 */
@MappedSuperclass
class Container<TBidiResource extends BidiResource<TContainer, TBidiResource>,
				TContainer extends Container<TBidiResource, TContainer>>
		implements Serializable,
		Comparable<Container<TBidiResource, TContainer>> {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Date created;
	private Set<TBidiResource> children;

	public Container() {
		children = new HashSet<TBidiResource>();
	}

	public Container(Container<TBidiResource, TContainer> c) {
		this.id = c.id;
		this.name = c.name;
		this.created = c.created;
		/*
		 * children is not referenced to reduce aliasing problems
		 */
		this.children = new HashSet<TBidiResource>();
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false, updatable = false)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "parent", fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Set<TBidiResource> getChildren() {
		return children;
	}

	public void setChildren(Set<TBidiResource> children) {
		this.children = children;
	}

	public void addChild(TBidiResource child) {
		if (child.getParent() != null) {
			child.getParent().removeChild(child);
		}
		children.add(child);
	}

	public void removeChild(TBidiResource child) {
		if (child == null) {
			throw new NullPointerException("Child cant be null");
		}
		if (children.remove(child)) {
			child.setParent(null);
		}
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
		if (getClass() != obj.getClass())
			return false;
		final Container<?, ?> other = (Container<?, ?>) obj;
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
	public int compareTo(Container<TBidiResource, TContainer> other) {
		if (this == other)
			return 0;
		if (other == null)
			throw new NullPointerException("Other container cant be null");

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
		/*
		 * NOTE: children is accessed through getter because of possibility of
		 * using CGLIB (or similar technology) by persistence provider.
		 */
		StringBuilder sb = new StringBuilder(30 + getChildren().size() * 30);
		sb.append("id=").append(id);
		sb.append(", name=").append(name);
		sb.append(", created=").append(created);
		sb.append(", children={").append(getChildren()).append("}");
		return sb.toString();
	}

}
