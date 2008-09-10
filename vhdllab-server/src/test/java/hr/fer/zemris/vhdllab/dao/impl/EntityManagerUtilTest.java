package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.dao.DAOException;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A test case for {@link EntityManagerUtil} class.
 * 
 * @author Miro Bezjak
 */
public class EntityManagerUtilTest {

	@BeforeClass
	public static void initTestCase() {
		EntityManagerUtil.createEntityManagerFactory();
	}

	@After
	public void destroyEachTest() {
		EntityManagerUtil.closeEntityManager();
	}

	/**
	 * Two invocations on {@link EntityManagerUtil#currentEntityManager()}
	 * method from same thread will return same reference to entity manager
	 */
	@Test
	public void currentEntityManager() {
		EntityManager em1 = EntityManagerUtil.currentEntityManager();
		EntityManager em2 = EntityManagerUtil.currentEntityManager();
		assertTrue("Not same Entity Manager reference.", em1 == em2);
	}

	/**
	 * Two invocations on {@link EntityManagerUtil#currentEntityManager()}
	 * method from two different threads will return two different references to
	 * entity manager (i.e. two entity managers will be created)
	 */
	@Test
	public void currentEntityManager2() throws InterruptedException {
		final EntityManager em1 = EntityManagerUtil.currentEntityManager();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				EntityManager em2 = EntityManagerUtil.currentEntityManager();
				assertTrue("Same Entity Manager reference.", em1 != em2);
			}
		});
		t.start();
		t.join();
	}

	/**
	 * No exception is thrown when invoking
	 * {@link EntityManagerUtil#closeEntityManager()} when no entity manager is
	 * opened
	 */
	@Test
	public void closeEntityManager() {
		EntityManagerUtil.closeEntityManager();
	}

	/**
	 * No exception is thrown when invoking
	 * {@link EntityManagerUtil#beginTransaction()} two times in a row
	 */
	@Test
	public void beginTransaction() throws DAOException {
		EntityManagerUtil.beginTransaction();
		EntityManagerUtil.beginTransaction();
	}

	/**
	 * No exception is thrown when invoking
	 * {@link EntityManagerUtil#commitTransaction()} when no transaction is
	 * opened
	 */
	@Test
	public void commitTransaction() throws DAOException {
		EntityManagerUtil.commitTransaction();
	}

	/**
	 * No exception is thrown when invoking
	 * {@link EntityManagerUtil#rollbackTransaction()} two times in a row
	 */
	@Test
	public void rollbackTransaction() {
		EntityManagerUtil.rollbackTransaction();
		EntityManagerUtil.rollbackTransaction();
	}

}
