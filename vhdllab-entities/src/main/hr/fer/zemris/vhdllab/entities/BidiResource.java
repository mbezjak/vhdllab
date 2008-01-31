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

	private TContainer parent;

	public BidiResource() {
		super();
	}

	public BidiResource(BidiResource<TContainer, TBidiResource> resource) {
		super(resource);
		/*
		 * parent is not referenced to reduce aliasing problems
		 */
		this.parent = null;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	public TContainer getParent() {
		return parent;
	}

	public void setParent(TContainer parent) {
		this.parent = parent;
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
