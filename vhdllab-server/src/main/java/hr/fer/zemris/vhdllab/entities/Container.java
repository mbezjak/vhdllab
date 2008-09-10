package hr.fer.zemris.vhdllab.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
import org.hibernate.annotations.Cascade;

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
public class Container<TBidiResource extends BidiResource<TContainer, TBidiResource>,
				TContainer extends Container<TBidiResource, TContainer>>
		implements Serializable,
		Comparable<Container<TBidiResource, TContainer>>,
		Iterable<TBidiResource> {

	private static final long serialVersionUID = 1L;

	/**
	 * Maximum name length.
	 */
	public static final int NAME_LENGTH = 255;

	@Id
	@GeneratedValue
	@Column(name = "id", updatable = false, insertable = false)
	private Long id;
	@Basic
	@Column(name = "name", length = NAME_LENGTH, nullable = false)
	private String name;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false, updatable = false)
	private Date created;
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "parent", fetch = FetchType.LAZY)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<TBidiResource> children;

	/**
	 * Constructor for persistence provider.
	 */
	Container() {
		super();
	}

	/**
	 * Creates a container with specified name.
	 * 
	 * @param name
	 *            a name of a container
	 * @throws NullPointerException
	 *             if <code>name</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>name</code> is too long
	 * @see #NAME_LENGTH
	 */
	public Container(String name) {
		if (name == null) {
			throw new NullPointerException("Name cant be null");
		}
		if (name.length() > NAME_LENGTH) {
			throw new IllegalArgumentException("Name must be <= " + NAME_LENGTH
					+ " but was: " + name.length());
		}
		this.name = name;
		this.children = new HashSet<TBidiResource>();
		this.created = new Date();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param c
	 *            a container object to duplicate
	 * @throws NullPointerException
	 *             if <code>c</code> is <code>null</code>
	 */
	public Container(Container<TBidiResource, TContainer> c) {
		if (c == null) {
			throw new NullPointerException("Container cant be null");
		}
		this.id = c.id;
		this.name = c.name;
		this.created = c.created;
		/*
		 * children is not referenced to reduce aliasing problems
		 */
		this.children = new HashSet<TBidiResource>();
	}

	/**
	 * Returns a unique identifier for every instance. If an instance was not
	 * persisted then this method will return <code>null</code>.
	 * 
	 * @return a unique identifier for every instance
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Returns a name of this container. Return value will never be
	 * <code>null</code>. A name is case insensitive.
	 * 
	 * @return a name of this container
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns a created date of this container. Return value will never be
	 * <code>null</code>.
	 * 
	 * @return a created date of this container
	 */
	public Date getCreated() {
		return new Date(created.getTime());
	}

	/**
	 * Getter for persistence provider. To enable lazy loading.
	 * 
	 * @return children in this container
	 */
	Set<TBidiResource> getChildren() {
		return children;
	}

	/**
	 * Adds a <code>child</code> to a collection of resources for this
	 * container.
	 * 
	 * @param child
	 *            a resource to add
	 * @throws NullPointerException
	 *             if <code>child</code> is <code>null</code>
	 */
	void addChild(TBidiResource child) {
		if (child == null) {
			throw new NullPointerException("Child cant be null");
		}
		getChildren().add(child);
	}

	/**
	 * Removes a resource from this container.
	 * 
	 * @param child
	 *            a resource to remove
	 * @throws NullPointerException
	 *             is <code>child</code> is <code>null</code>
	 */
	void removeChild(TBidiResource child) {
		if (child == null) {
			throw new NullPointerException("Child cant be null");
		}
		if (getChildren().remove(child)) {
			child.disconnect();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<TBidiResource> iterator() {
		return new Iterator<TBidiResource>() {
			private Iterator<TBidiResource> iterator = getChildren().iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public TBidiResource next() {
				return iterator.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Cant remove resource");
			}
		};
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
		result = prime * result + name.toLowerCase().hashCode();
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
		return name.equalsIgnoreCase(other.name);
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

		long val = name.compareToIgnoreCase(other.name);

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
		sb.append(", children(").append(getChildren().size());
		sb.append(")={").append(getChildren()).append("}");
		return sb.toString();
	}

}
