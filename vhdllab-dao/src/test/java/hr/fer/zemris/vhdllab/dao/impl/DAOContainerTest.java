package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.dao.DAOContainer;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A test case for {@link DAOContainer} class.
 * 
 * @author Miro Bezjak
 */
public class DAOContainerTest {

	private static DAOContainer container;

	@BeforeClass
	public static void initOnce() {
		container = new DAOContainer();
	}

	/**
	 * Repeated invocation returns the same instance
	 */
	@Test
	public void getProjectDAO() throws Exception {
		assertTrue("not cached.", container.getProjectDAO() == container
				.getProjectDAO());
	}

	/**
	 * Repeated invocation returns the same instance
	 */
	@Test
	public void getFileDAO() throws Exception {
		assertTrue("not cached.", container.getFileDAO() == container
				.getFileDAO());
	}

	/**
	 * Repeated invocation returns the same instance
	 */
	@Test
	public void getLibraryDAO() throws Exception {
		assertTrue("not cached.", container.getLibraryDAO() == container
				.getLibraryDAO());
	}

	/**
	 * Repeated invocation returns the same instance
	 */
	@Test
	public void getLibraryFileDAO() throws Exception {
		assertTrue("not cached.", container.getLibraryFileDAO() == container
				.getLibraryFileDAO());
	}

	/**
	 * Repeated invocation returns the same instance
	 */
	@Test
	public void getUserFileDAO() throws Exception {
		assertTrue("not cached.", container.getUserFileDAO() == container
				.getUserFileDAO());
	}

}
