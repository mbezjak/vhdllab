package hr.fer.zemris.vhdllab.entities;

/**
 * A mock object for {@link BidiResource} to help with testing.
 * 
 * @author Miro Bezjak
 */
public class MockResource extends BidiResource<MockContainer, MockResource> {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor without setting content.
	 * 
	 * @param parent
	 *            a parent container
	 * @param name
	 *            a name of a resource
	 * @param type
	 *            a type of a resource
	 */
	public MockResource(MockContainer parent, String name, String type) {
		super(parent, name, type);
		parent.addChild(this);
	}

	/**
	 * Default constructor.
	 * 
	 * @param parent
	 *            a parent container
	 * @param name
	 *            a name of a resource
	 * @param type
	 *            a type of a resource
	 * @param content
	 *            a content of a resource
	 */
	public MockResource(MockContainer parent, String name, String type,
			String content) {
		super(parent, name, type, content);
		parent.addChild(this);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param r
	 *            a resource
	 * @param parent
	 *            a parent container
	 */
	public MockResource(MockResource r, MockContainer parent) {
		super(r, parent);
		parent.addChild(this);
	}

}
