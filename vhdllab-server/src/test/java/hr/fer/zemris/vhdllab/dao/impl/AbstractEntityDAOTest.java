package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.entities.StubFactory;
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

    private static final Integer UNUSED_ID = Integer.valueOf(Integer.MAX_VALUE);
    private static final String FIND_BY_PROJECT_NAME_QUERY =
        "select p from Project p where p.userId = :userId and p.name = :name";

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
     * Entity is null
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
        assertEquals("Entity exists when it shouldn't.", false, dao
                .exists(UNUSED_ID));
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
     * query is null
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
        dao.existsEntity("a query", null);
    }

    /**
     * A query returns different type of entity then that specified by generics
     */
    @Test
    public void existsEntity3() throws Exception {
        // prepair project
        Project project = StubFactory.create(Project.class, 400);
        ProjectDAO projectDAO = new ProjectDAOImpl();
        projectDAO.save(project);

        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("name", StubFactory.getStubValue("name", 400));
        params.put("userId", StubFactory.getStubValue("userId", 400));
        try {
            dao.existsEntity(FIND_BY_PROJECT_NAME_QUERY, params);
            fail("Expected ClassCastException.");
        } catch (ClassCastException e) {
            // expected exception!
        } finally {
            // delete created file
            projectDAO.delete(project.getId());
        }
    }

    /**
     * query is null
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
        dao.findSingleEntity("a query", null);
    }

    /**
     * A query returns different type of entity then that specified by generics
     */
    @Test
    public void findSingleEntity3() throws Exception {
        // prepair project
        Project project = StubFactory.create(Project.class, 400);
        ProjectDAO projectDAO = new ProjectDAOImpl();
        projectDAO.save(project);

        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("name", StubFactory.getStubValue("name", 400));
        params.put("userId", StubFactory.getStubValue("userId", 400));
        try {
            dao.findSingleEntity(FIND_BY_PROJECT_NAME_QUERY, params);
            fail("Expected ClassCastException.");
        } catch (ClassCastException e) {
            // expected exception!
        } finally {
            // delete created file
            projectDAO.delete(project.getId());
        }
    }

    /**
     * query is null
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
        dao.findEntityList("a query", null);
    }

    /**
     * A query returns different type of entity then that specified by generics
     */
    @Test
    public void findEntityList3() throws Exception {
        // prepair project
        Project project = StubFactory.create(Project.class, 400);
        ProjectDAO projectDAO = new ProjectDAOImpl();
        projectDAO.save(project);

        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("name", StubFactory.getStubValue("name", 400));
        params.put("userId", StubFactory.getStubValue("userId", 400));
        try {
            dao.findEntityList(FIND_BY_PROJECT_NAME_QUERY, params);
            fail("Expected ClassCastException.");
        } catch (ClassCastException e) {
            // expected exception!
        } finally {
            // delete created file
            projectDAO.delete(project.getId());
        }
    }

}
