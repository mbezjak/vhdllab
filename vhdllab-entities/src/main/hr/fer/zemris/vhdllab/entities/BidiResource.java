package hr.fer.zemris.vhdllab.entities;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * This is a generic class for all bidi-resources. Basically it defines all
 * properties that a bidi-resource must have. A bidi-resource differs from
 * normal resource because it contains a reference to {@link Container} that
 * contains this resource. By linking to a parent container they form
 * bidirectional relationship.
 * 
 * @author Miro Bezjak
 * @since 31/1/2008
 * @version 1.0
 */
@MappedSuperclass
class BidiResource<TContainer extends Container<TBidiResource, TContainer>,
					TBidiResource extends BidiResource<TContainer, TBidiResource>>
		extends Resource {
	/*
	 * NOTE: Parent is not used in equals, hashCode, compareTo or toString
	 * methods because that would cause endless loop (because this object with
	 * its parent container form bidirectional relationship).
	 */

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	private TContainer parent;

	/**
	 * Constructor for persistence provider.
	 */
	BidiResource() {
		super();
	}

	/**
	 * Creates a bidi-resource with specified name and type. Content will be set
	 * to empty string.
	 * 
	 * @param parent
	 *            a container of a bidi-resource
	 * @param name
	 *            a name of a bidi-resource
	 * @param type
	 *            a type of a bidi-resource
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if either parameter is too long
	 * @see #NAME_LENGTH
	 * @see #TYPE_LENGTH
	 * @see #CONTENT_LENGTH
	 */
	public BidiResource(TContainer parent, String name, String type) {
		this(parent, name, type, "");
	}

	/**
	 * Creates a bidi-resource with specified name, type and content.
	 * 
	 * @param parent
	 *            a container of a bidi-resource
	 * @param name
	 *            a name of a bidi-resource
	 * @param type
	 *            a type of a bidi-resource
	 * @param a
	 *            content of a bidi-resource
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if either parameter is too long
	 * @see #NAME_LENGTH
	 * @see #TYPE_LENGTH
	 * @see #CONTENT_LENGTH
	 */
	public BidiResource(TContainer parent, String name, String type,
			String content) {
		super(name, type, content);
		if (parent == null) {
			throw new NullPointerException("Parent cant be null");
		}
		this.parent = parent;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param resource
	 *            a bidi-resource object to duplicate
	 * @param parent
	 *            a container for duplicate
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public BidiResource(BidiResource<TContainer, TBidiResource> resource,
			TContainer parent) {
		super(resource);
		if (parent == null) {
			throw new NullPointerException("Parent cant be null");
		}
		this.parent = parent;
	}

	/**
	 * Returns a container of bidi-resource. Return value will be
	 * <code>null</code> if this bidi-resource is disconnected from its
	 * container.
	 * 
	 * @return a container of bidi-resource
	 */
	TContainer getParent() {
		return parent;
	}

	/**
	 * Removes reference to parent. This method should be called when deleting a
	 * bidi-resource.
	 */
	void disconnect() {
		parent = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.entities.Resource#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		/*
		 * Overriding just to check if given resource is a bidi-resource.
		 */
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BidiResource<?, ?>))
			return false;
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.entities.Resource#compareTo(hr.fer.zemris.vhdllab.entities.Resource)
	 */
	@Override
	public int compareTo(Resource o) {
		/*
		 * Overriding just to check if given resource is a bidi-resource.
		 */
		if (this == o)
			return 0;
		if (o == null)
			throw new NullPointerException("Other bidi-resource cant be null");
		if (!(o instanceof BidiResource<?, ?>)) {
			throw new ClassCastException("Object is not of bidi-resource type");
		}
		return super.compareTo(o);
	}

}
