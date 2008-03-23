package hr.fer.zemris.vhdllab.server;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.picocontainer.MutablePicoContainer;

import hr.fer.zemris.vhdllab.server.ServerContainer;

/**
 * A test case for {@link ServerContainer}.
 * 
 * @author Miro Bezjak
 */
public class ServerContainerTest {

	/**
	 * Repeated invocation of getComponent returns the same instance
	 */
	@Test
	public void constructor() throws Exception {
		MockContaner container = new MockContaner();
		MutablePicoContainer pico = container.getContainer();
		assertNotNull("pico container not instantiated.", pico);

		pico.addComponent(String.class);
		assertTrue("not cached.", pico.getComponent(String.class) == pico
				.getComponent(String.class));
	}

	/**
	 * Repeated invocation of getComponent returns the same instance
	 */
	@Test
	public void constructor2() throws Exception {
		MockContaner parent = new MockContaner();
		MockContaner container = new MockContaner(parent);
		MutablePicoContainer pico = container.getContainer();
		assertNotNull("pico container not instantiated.", pico);

		pico.addComponent(String.class);
		assertTrue("not cached.", pico.getComponent(String.class) == pico
				.getComponent(String.class));
	}

	/**
	 * Mock container simply exposes protected constructors and methods.
	 * 
	 * @author Miro Bezjak
	 */
	private static class MockContaner extends ServerContainer {
		public MockContaner() {
			super();
		}
	
		public MockContaner(MockContaner c) {
			super(c);
		}
	
		@Override
		public MutablePicoContainer getContainer() {
			return super.getContainer();
		}
	}

}
