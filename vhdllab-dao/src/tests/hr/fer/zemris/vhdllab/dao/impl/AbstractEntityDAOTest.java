package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.entities.UserFile;

import java.util.HashMap;
import java.util.Map;

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

	private static AbstractEntityDAO<UserFile> dao;

	@BeforeClass
	public static void initTestCase() {
		/*
		 * Entity type is not important in this test case so UserFile was picked
		 * because it is the simplest one.
		 */
		dao = new AbstractEntityDAO<UserFile>(UserFile.class) {
			// empty implementation
		};
		EntityManagerUtil.createEntityManagerFactory();
	}

	@Before
	public void initEachTest() {
		EntityManagerUtil.currentEntityManager();
	}

	@After
	public void destroyEachTest() {
		EntityManagerUtil.closeEntityManager();
	}

	/**
	 * clazz is null
	 */
	@Test(expected = NullPointerException.class)
	public void constructor() {
		new AbstractEntityDAO<UserFile>(null) {
			// empty implementation
		};
	}

	/**
	 * id is null
	 */
	@Test(expected = NullPointerException.class)
	public void save() throws DAOException {
		dao.save(null);
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
	public void exists() throws DAOException {
		dao.exists(null);
	}

	/**
	 * non-existing id
	 */
	@Test
	public void exists2() throws DAOException {
		assertEquals("Entity exists when it shouldn't.", false, dao.exists(UNUSED_ID));
	}

	/**
	 * entity is null
	 */
	@Test(expected = NullPointerException.class)
	public void delete() throws DAOException {
		dao.delete(null);
	}

	/**
	 * non-existing entity
	 */
	@Test(expected = DAOException.class)
	public void delete2() throws DAOException {
		dao.delete(UNUSED_ID);
	}

	/**
	 * named query is null
	 */
	@Test(expected = NullPointerException.class)
	public void existsEntity() throws DAOException {
		dao.existsEntity(null, new HashMap<String, Object>(0));
	}

	/**
	 * parameters is null
	 */
	@Test(expected = NullPointerException.class)
	public void existsEntity2() throws DAOException {
		dao.existsEntity(UserFile.FIND_BY_NAME_QUERY, null);
	}

	/**
	 * A named query returns different type of entity then that specified by
	 * generics
	 */
	@Test
	public void existsEntity3() throws DAOException {
		// prepair project
		Project project = new Project("user.identifier", "project.name");
		ProjectDAO projectDAO = new ProjectDAOImpl();
		projectDAO.save(project);

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("name", "project.name");
		params.put("userId", "user.identifier");
		try {
			dao.existsEntity(Project.FIND_BY_NAME_QUERY, params);
			fail("Expected ClassCastException.");
		} catch (ClassCastException e) {
			// expected exception!
		} finally {
			// delete created file
			projectDAO.delete(project.getId());
		}
	}

	/**
	 * named query is null
	 */
	@Test(expected = NullPointerException.class)
	public void findSingleEntity() throws DAOException {
		dao.findSingleEntity(null, new HashMap<String, Object>(0));
	}

	/**
	 * parameters is null
	 */
	@Test(expected = NullPointerException.class)
	public void findSingleEntity2() throws DAOException {
		dao.findSingleEntity(UserFile.FIND_BY_NAME_QUERY, null);
	}

	/**
	 * A named query returns different type of entity then that specified by
	 * generics
	 */
	@Test
	public void findSingleEntity3() throws DAOException {
		// prepair project
		Project project = new Project("user.identifier", "project.name");
		ProjectDAO projectDAO = new ProjectDAOImpl();
		projectDAO.save(project);

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("name", "project.name");
		params.put("userId", "user.identifier");
		try {
			dao.findSingleEntity(Project.FIND_BY_NAME_QUERY, params);
			fail("Expected ClassCastException.");
		} catch (ClassCastException e) {
			// expected exception!
		} finally {
			// delete created file
			projectDAO.delete(project.getId());
		}
	}

	/**
	 * named query is null
	 */
	@Test(expected = NullPointerException.class)
	public void findEntityList() throws DAOException {
		dao.findEntityList(null, new HashMap<String, Object>(0));
	}

	/**
	 * parameters is null
	 */
	@Test(expected = NullPointerException.class)
	public void findEntityList2() throws DAOException {
		dao.findEntityList(UserFile.FIND_BY_NAME_QUERY, null);
	}

	/**
	 * A named query returns different type of entity then that specified by
	 * generics
	 */
	@Test
	public void findEntityList3() throws DAOException {
		// prepair project
		Project project = new Project("user.identifier", "project.name");
		ProjectDAO projectDAO = new ProjectDAOImpl();
		projectDAO.save(project);

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("name", "project.name");
		params.put("userId", "user.identifier");
		try {
			dao.findEntityList(Project.FIND_BY_NAME_QUERY, params);
			fail("Expected ClassCastException.");
		} catch (ClassCastException e) {
			// expected exception!
		} finally {
			// delete created file
			projectDAO.delete(project.getId());
		}
	}

}
