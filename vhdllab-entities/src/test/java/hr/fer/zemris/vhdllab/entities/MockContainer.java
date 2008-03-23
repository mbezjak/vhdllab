package hr.fer.zemris.vhdllab.entities;

/**
 * A mock object for {@link Container} to help with testing.
 * 
 * @author Miro Bezjak
 */
public class MockContainer extends Container<MockResource, MockContainer> {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 * 
	 * @param name
	 *            a name of a container
	 */
	public MockContainer(String name) {
		super(name);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param c
	 *            a container
	 */
	public MockContainer(MockContainer c) {
		super(c);
	}

}
