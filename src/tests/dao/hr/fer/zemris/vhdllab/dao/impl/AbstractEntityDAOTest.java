package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.UserFileDAO;
import hr.fer.zemris.vhdllab.entities.GlobalFile;
import hr.fer.zemris.vhdllab.entities.UserFile;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link AbstractEntityDAO} class.
 * 
 * @author Miro Bezjak
 */
public class AbstractEntityDAOTest {

	private static final Long UNUSED_ID = Long.valueOf(Long.MAX_VALUE);

	private static AbstractEntityDAO<GlobalFile> dao;

	@BeforeClass
	public static void initTestCase() throws DAOException {
		/*
		 * Entity type is not important in this test case so GlobalFileDAOImpl
		 * was picked because it is the simplest one.
		 */
		dao = new GlobalFileDAOImpl();
		EntityManagerUtil.createEntityManagerFactory();
	}

	@Before
	public void initEachTest() throws DAOException {
		EntityManagerUtil.currentEntityManager();
	}

	@After
	public void destroyEachTest() throws DAOException {
		EntityManagerUtil.closeEntityManager();
	}

	/**
	 * id is null
	 */
	@Test(expected = NullPointerException.class)
	public void load() throws DAOException {
		dao.load(null);
	}

	/**
	 * non-existing id
	 */
	@Test(expected = DAOException.class)
	public void load2() throws DAOException {
		dao.load(UNUSED_ID);
	}

	/**
	 * id is null
	 */
	@Test(expected = NullPointerException.class)
	public void save() throws DAOException {
		dao.save(null);
	}

	/**
	 * global file is null
	 */
	@Test(expected = NullPointerException.class)
	public void delete() throws DAOException {
		dao.delete(null);
	}

	/**
	 * non-existing global file
	 */
	@Test(expected = DAOException.class)
	public void delete2() throws DAOException {
		dao.delete(UNUSED_ID);
	}

	/**
	 * id is null
	 */
	@Test(expected = NullPointerException.class)
	public void exists() throws DAOException {
		dao.exists(null);
	}

	/**
	 * non-existing id
	 */
	@Test
	public void exists2() throws DAOException {
		assertEquals(false, dao.exists(UNUSED_ID));
	}

	/**
	 * named query is null
	 */
	@Test(expected = NullPointerException.class)
	public void existsEntity() throws DAOException {
		dao.existsEntity(null, new String[0], new Object[0]);
	}

	/**
	 * param names is null
	 */
	@Test(expected = NullPointerException.class)
	public void existsEntity2() throws DAOException {
		dao.existsEntity(GlobalFile.GET_ALL_QUERY, null, new Object[0]);
	}

	/**
	 * param values is null
	 */
	@Test(expected = NullPointerException.class)
	public void existsEntity3() throws DAOException {
		dao.existsEntity(GlobalFile.GET_ALL_QUERY, new String[0], null);
	}

	/**
	 * param names and param values doesn't have the same length
	 */
	@Test(expected = IllegalArgumentException.class)
	public void existsEntity4() throws DAOException {
		String[] paramNames = new String[] { "id" };
		Object[] paramValues = new Object[] {};
		dao.existsEntity(GlobalFile.GET_ALL_QUERY, paramNames, paramValues);
	}

	/**
	 * A named query returns different type of entity then that specified by generics
	 */
	@Test
	public void existsEntity5() throws DAOException {
		// prepair file
		UserFile file = new UserFile();
		file.setName("name");
		file.setUserId("user.identifier");
		UserFileDAO userFileDAO = new UserFileDAOImpl();
		userFileDAO.save(file);
		
		String[] paramNames = new String[] { "name", "userId" };
		Object[] paramValues = new Object[] {"name", "user.identifier"};
		try {
			dao.existsEntity(UserFile.FIND_BY_NAME_QUERY, paramNames, paramValues);
			fail("Expected ClassCastException");
		} catch (ClassCastException e) {
		} finally {
			// delete created file
			userFileDAO.delete(file.getId());
		}
	}
	
	/**
	 * named query is null
	 */
	@Test(expected = NullPointerException.class)
	public void findSingleEntity() throws DAOException {
		dao.findSingleEntity(null, new String[0], new Object[0]);
	}
	
	/**
	 * param names is null
	 */
	@Test(expected = NullPointerException.class)
	public void findSingleEntity2() throws DAOException {
		dao.findSingleEntity(GlobalFile.GET_ALL_QUERY, null, new Object[0]);
	}
	
	/**
	 * param values is null
	 */
	@Test(expected = NullPointerException.class)
	public void findSingleEntity3() throws DAOException {
		dao.findSingleEntity(GlobalFile.GET_ALL_QUERY, new String[0], null);
	}
	
	/**
	 * param names and param values doesn't have the same length
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findSingleEntity4() throws DAOException {
		String[] paramNames = new String[] { "id" };
		Object[] paramValues = new Object[] {};
		dao.findSingleEntity(GlobalFile.GET_ALL_QUERY, paramNames, paramValues);
	}
	
	/**
	 * A named query returns different type of entity then that specified by generics
	 */
	@Test
	public void findSingleEntity5() throws DAOException {
		// prepair file
		UserFile file = new UserFile();
		file.setName("name");
		file.setUserId("user.identifier");
		UserFileDAO userFileDAO = new UserFileDAOImpl();
		userFileDAO.save(file);
		
		String[] paramNames = new String[] { "name", "userId" };
		Object[] paramValues = new Object[] {"name", "user.identifier"};
		try {
			dao.findSingleEntity(UserFile.FIND_BY_NAME_QUERY, paramNames, paramValues);
			fail("Expected ClassCastException");
		} catch (ClassCastException e) {
		} finally {
			// delete created file
			userFileDAO.delete(file.getId());
		}
	}
	
	/**
	 * named query is null
	 */
	@Test(expected = NullPointerException.class)
	public void findEntityList() throws DAOException {
		dao.findEntityList(null, new String[0], new Object[0]);
	}
	
	/**
	 * param names is null
	 */
	@Test(expected = NullPointerException.class)
	public void findEntityList2() throws DAOException {
		dao.findEntityList(GlobalFile.GET_ALL_QUERY, null, new Object[0]);
	}
	
	/**
	 * param values is null
	 */
	@Test(expected = NullPointerException.class)
	public void findEntityList3() throws DAOException {
		dao.findEntityList(GlobalFile.GET_ALL_QUERY, new String[0], null);
	}
	
	/**
	 * param names and param values doesn't have the same length
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findEntityList4() throws DAOException {
		String[] paramNames = new String[] { "id" };
		Object[] paramValues = new Object[] {};
		dao.findEntityList(GlobalFile.GET_ALL_QUERY, paramNames, paramValues);
	}
	
	/**
	 * A named query returns different type of entity then that specified by generics
	 */
	@Test
	public void findEntityList5() throws DAOException {
		// prepair file
		UserFile file = new UserFile();
		file.setName("name");
		file.setUserId("user.identifier");
		UserFileDAO userFileDAO = new UserFileDAOImpl();
		userFileDAO.save(file);
		
		String[] paramNames = new String[] { "userId" };
		Object[] paramValues = new Object[] {"user.identifier"};
		try {
			dao.findEntityList(UserFile.FIND_BY_USER_QUERY, paramNames, paramValues);
			fail("Expected ClassCastException");
		} catch (ClassCastException e) {
		} finally {
			// delete created file
			userFileDAO.delete(file.getId());
		}
	}
	
}
