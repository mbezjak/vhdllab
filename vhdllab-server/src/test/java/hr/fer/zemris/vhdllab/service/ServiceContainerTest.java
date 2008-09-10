package hr.fer.zemris.vhdllab.service;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A test case for {@link ServiceContainer} class.
 *
 * @author Miro Bezjak
 */
public class ServiceContainerTest {

	private static ServiceContainer container;

	@BeforeClass
	public static void initOnce() {
		container = ServiceContainer.instance();
	}

	/**
	 * Repeated invocation returns the same instance
	 */
	@Test
	public void getProjectManager() throws Exception {
		assertTrue("not cached.", container.getProjectManager() == container
				.getProjectManager());
	}

	/**
	 * Repeated invocation returns the same instance
	 */
	@Test
	public void getFileManager() throws Exception {
		assertTrue("not cached.", container.getFileManager() == container
				.getFileManager());
	}

	/**
	 * Repeated invocation returns the same instance
	 */
	@Test
	public void getLibraryManager() throws Exception {
		assertTrue("not cached.", container.getLibraryManager() == container
				.getLibraryManager());
	}

	/**
	 * Repeated invocation returns the same instance
	 */
	@Test
	public void getLibraryFileManager() throws Exception {
		assertTrue("not cached.",
				container.getLibraryFileManager() == container
						.getLibraryFileManager());
	}

	/**
	 * Repeated invocation returns the same instance
	 */
	@Test
	public void getUserFileManager() throws Exception {
		assertTrue("not cached.", container.getUserFileManager() == container
				.getUserFileManager());
	}

	/**
	 * Repeated invocation returns the same instance
	 */
	@Test
	public void getServiceManager() throws Exception {
		assertTrue("not cached.", container.getServiceManager() == container
				.getServiceManager());
	}

}
