package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.server.FileTypes;
import hr.fer.zemris.vhdllab.server.api.StatusCodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for {@link ProjectDAOImpl}.
 * 
 * @author Miro Bezjak
 */
public class ProjectDAOImplTest {

	private static final String NAME = "simple.project.name";
	private static final String USER_ID = "user.identifier";
	private static final String NEW_NAME = "new" + NAME;
	private static final String NEW_USER_ID = "new" + USER_ID;

	private static FileDAO fileDAO;
	private static ProjectDAO dao;
	private Project project;
	private File file;

	@BeforeClass
	public static void initTestCase() throws DAOException {
		fileDAO = new FileDAOImpl();
		dao = new ProjectDAOImpl();
		EntityManagerUtil.createEntityManagerFactory();
		destroyFiles();
	}

	@Before
	public void initEachTest() throws DAOException {
		initFiles();
		EntityManagerUtil.currentEntityManager();
	}

	private void initFiles() throws DAOException {
		project = new Project(USER_ID, NAME);
		file = new File(project, "file.name", FileTypes.VHDL_SOURCE,
				"<file>int main() {}</file>");
	}

	@After
	public void destroyEachTest() throws DAOException {
		EntityManagerUtil.closeEntityManager();
		destroyFiles();
	}

	private static void destroyFiles() throws DAOException {
		/*
		 * Create a new entity manager for destroying projects to isolate any
		 * errors in a test
		 */
		EntityManagerUtil.currentEntityManager();
		for (Project p : dao.findByUser(USER_ID)) {
			dao.delete(p.getId());
		}
		for (Project p : dao.findByUser(NEW_USER_ID)) {
			dao.delete(p.getId());
		}
		EntityManagerUtil.closeEntityManager();
	}

	/**
	 * save a project then load it and see it they are the same
	 */
	@Test
	public void saveAndLoad() throws DAOException {
		dao.save(project);
		Project loadedProject = dao.load(project.getId());
		assertEquals("projects not equal.", project, loadedProject);
		assertEquals("user ids not equal.", USER_ID, loadedProject.getUserId());
		assertEquals("names not equal.", NAME, loadedProject.getName());
	}

	/**
	 * Project is null
	 */
	@Test(expected = NullPointerException.class)
	public void save() throws DAOException {
		dao.save(null);
	}

	/**
	 * Once project is persisted an ID is no longer null
	 */
	@Test
	public void save2() throws DAOException {
		assertNull("project has id set.", project.getId());
		dao.save(project);
		assertNotNull("project id wasn't set after creation.", project.getId());
	}

	/**
	 * Cascade to saving files when creating a project
	 */
	@Test
	public void save3() throws DAOException {
		dao.save(project);
		assertTrue("file not saved.", fileDAO.exists(file.getId()));
	}
	
	/**
	 * Project name and user id are unique (i.e. form secondary key)
	 */
	@Test
	public void save4() throws Exception {
		dao.save(project);
		Project newProject = new Project(USER_ID, NAME);
		try {
			dao.save(newProject);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_ALREADY_EXISTS) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * Save a project with same user id but different name
	 */
	@Test
	public void save5() throws Exception {
		dao.save(project);
		Project newProject = new Project(USER_ID, NEW_NAME);
		dao.save(newProject);
		assertTrue("new project not saved.", dao.exists(newProject.getId()));
		assertEquals("projects are not same.", newProject, dao.load(newProject
				.getId()));
	}

	/**
	 * Save a project with same name but different user id
	 */
	@Test
	public void save6() throws Exception {
		dao.save(project);
		Project newProject = new Project(NEW_USER_ID, NAME);
		dao.save(newProject);
		assertTrue("new project not saved.", dao.exists(newProject.getId()));
		assertEquals("projects are not same.", newProject, dao.load(newProject
				.getId()));
	}

	/**
	 * add a file after a project is saved
	 */
	@Test
	public void save7() throws DAOException {
		dao.save(project);
		File newFile = new File(project, "new.file.name", "new.file.type");
		dao.save(project);

		Project loadedProject = dao.load(project.getId());
		assertTrue("file not save.", fileDAO.exists(newFile.getId()));
		assertTrue("file not save.", fileDAO.exists(loadedProject.getId(),
				newFile.getName()));
		assertEquals("projects not equal.", project, loadedProject);
		assertTrue("project doesn't contain a new file.", loadedProject
				.getFiles().contains(newFile));
	}

	/**
	 * Id is null
	 */
	@Test(expected = NullPointerException.class)
	public void load() throws Exception {
		dao.load(null);
	}

	/**
	 * save a project then delete it
	 */
	@Test
	public void delete() throws DAOException {
		dao.save(project);
		assertTrue("project not saved.", dao.exists(project.getId()));
		dao.delete(project.getId());
		assertFalse("project still exists.", dao.exists(project.getId()));
		assertFalse("project still exists.", dao.exists(project.getUserId(),
				project.getName()));
		assertFalse("file still exists.", fileDAO.exists(file.getId()));
	}

	/**
	 * remove a file from a project then save project
	 */
	@Test
	public void delete2() throws DAOException {
		dao.save(project);
		assertTrue("file not saved.", fileDAO.exists(file.getId()));
		project.removeFile(file);
		assertNull("file not removed from collection.", file.getProject());
		assertFalse("file still in collection.", project.getFiles().contains(
				file));
		dao.save(project);
		assertFalse("file still exists.", fileDAO.exists(file.getId()));
		assertFalse("project still contains a file.", project.getFiles()
				.contains(file));
	}

	/**
	 * id is null
	 */
	@Test(expected = NullPointerException.class)
	public void exists() throws DAOException {
		dao.exists((Long)null);
	}

	/**
	 * non-existing id
	 */
	@Test
	public void exists2() throws DAOException {
		assertFalse("project exists.", dao.exists(Long.MAX_VALUE));
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void exists3() throws DAOException {
		dao.save(project);
		assertTrue("project doesn't exist.", dao.exists(project.getId()));
	}
	
	/**
	 * user id is null
	 */
	@Test(expected = NullPointerException.class)
	public void exists4() throws DAOException {
		dao.exists(null, NAME);
	}

	/**
	 * name is null
	 */
	@Test(expected = NullPointerException.class)
	public void exists5() throws DAOException {
		dao.exists(USER_ID, null);
	}

	/**
	 * non-existing name and user id
	 */
	@Test
	public void exists6() throws DAOException {
		assertFalse("file exists when it shouldn't.", dao.exists(NEW_USER_ID,
				NAME));
		assertFalse("file exists when it shouldn't.", dao.exists(USER_ID,
				NEW_NAME));
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists7() throws DAOException {
		dao.save(project);
		assertTrue("project doesn't exist.", dao.exists(project.getId()));
		assertTrue("project doesn't exist.", dao.exists(project.getUserId(),
				project.getName()));
		assertTrue("project name and user id are not case insensitive.", dao
				.exists(project.getUserId().toUpperCase(), project.getName()
						.toUpperCase()));
	}

	/**
	 * user id is null
	 */
	@Test(expected = NullPointerException.class)
	public void findByName() throws DAOException {
		dao.findByName(null, NAME);
	}

	/**
	 * name is null
	 */
	@Test(expected = NullPointerException.class)
	public void findByName2() throws DAOException {
		dao.findByName(USER_ID, null);
	}

	/**
	 * non-existing user id
	 */
	@Test
	public void findByName3() throws DAOException {
		try {
			dao.findByName(NEW_USER_ID, NAME);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_DOESNT_EXIST) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * non-existing name
	 */
	@Test
	public void findByName4() throws DAOException {
		try {
			dao.findByName(USER_ID, NEW_NAME);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_DOESNT_EXIST) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * everything ok
	 */
	@Test
	public void findByName5() throws DAOException {
		dao.save(project);
		assertEquals("project not found.", project, dao.findByName(project
				.getUserId(), project.getName()));
		assertEquals("project name and user id are not case insensitive.",
				project, dao.findByName(project.getUserId().toUpperCase(),
						project.getName().toUpperCase()));
	}

	/**
	 * user id is null
	 */
	@Test(expected = NullPointerException.class)
	public void findByUser() throws DAOException {
		dao.findByUser(null);
	}

	/**
	 * non-existing user id
	 */
	@Test()
	public void findByUser2() throws DAOException {
		assertEquals(Collections.emptyList(), dao.findByUser(NEW_USER_ID));
	}

	/**
	 * everything ok. one project in collection
	 */
	@Test
	public void findByUser3() throws DAOException {
		dao.save(project);
		List<Project> projects = new ArrayList<Project>(1);
		projects.add(project);
		assertEquals("projects not equal.", projects, dao.findByUser(project
				.getUserId()));
		assertEquals("user id is not case insensitive.", projects, dao
				.findByUser(project.getUserId().toUpperCase()));
	}

	/**
	 * everything ok. two projects in collection
	 */
	@Test
	public void findByUser4() throws DAOException {
		Project project2 = new Project(USER_ID, NEW_NAME);
		dao.save(project);
		dao.save(project2);
		List<Project> projects = new ArrayList<Project>(2);
		projects.add(project);
		projects.add(project2);
		assertEquals("collections not equal.", projects, dao.findByUser(project
				.getUserId()));
		assertEquals("user id is not case insensitive.", projects, dao
				.findByUser(project.getUserId().toUpperCase()));
	}

	/**
	 * everything ok. two collections
	 */
	@Test
	public void findByUser5() throws DAOException {
		Project project2 = new Project(NEW_USER_ID, NEW_NAME);
		dao.save(project);
		dao.save(project2);
		List<Project> collection1 = new ArrayList<Project>(1);
		List<Project> collection2 = new ArrayList<Project>(1);
		collection1.add(project);
		collection2.add(project2);
		assertEquals("collections not equal.", collection1, dao
				.findByUser(project.getUserId()));
		assertEquals("user id is not case insensitive.", collection1, dao
				.findByUser(project.getUserId().toUpperCase()));
		assertEquals("collections not equal.", collection2, dao
				.findByUser(project2.getUserId()));
		assertEquals("user id is not case insensitive.", collection2, dao
				.findByUser(project2.getUserId().toUpperCase()));
	}

	/**
	 * Test to see if hibernate second level cache is working
	 */
	@Ignore("already tested")
	@Test
	public void cacheTest() throws Exception {
		// prepair test by storing 500 projects in database
		System.out.print("Prepairing Project cache test...");
		EntityManagerUtil.currentEntityManager();
		for (int i = 0; i < 500; i++) {
			String name = "name" + (i + 1);
			Project p = new Project(USER_ID, name);
			dao.save(p);
		}
		EntityManagerUtil.closeEntityManager();
		System.out.println("done");
		/*
		 * Register hibernate statistics service to see if second level cache is
		 * working.
		 */
		EntityManagerUtil.registerPersistenceJMX();

		/*
		 * Actual test (not automated - requires user interaction). Note that
		 * caching behavioral can't be determined just by looking at spent time.
		 * One reason for this is because initCacheTest method indirectly
		 * populates caches. On the other hand by simply viewing statistics of
		 * persistence provider user can be sure that caches are working.
		 * jconsole tool will help with that.
		 */
		for (int i = 0; i < 5; i++) {
			EntityManagerUtil.currentEntityManager();
			long start = System.currentTimeMillis();
			List<Project> projects = dao.findByUser(USER_ID);
			EntityManagerUtil.closeEntityManager();
			long end = System.currentTimeMillis();
			System.out.println("ProjectDAO.findByUser() - query cache test: "
					+ (end - start) + "ms");
			assertNotNull("projects are null", projects);
		}
		/*
		 * Pause so user can view statistics in jconsole
		 */
		Thread.sleep(Long.MAX_VALUE);
	}

	/**
	 * Test to see if hibernate second level collection cache is working
	 */
	@Ignore("already tested")
	@Test
	public void collectionLazyLoadingTest() throws Exception {
		// prepair test by storing 500 files in database
		System.out.print("Prepairing Project cache collection test...");
		for (int i = 0; i < 500; i++) {
			String name = "name" + (i + 1);
			new File(project, name, FileTypes.VHDL_SOURCE, "abcdef");
		}
		EntityManagerUtil.currentEntityManager();
		dao.save(project);
		EntityManagerUtil.closeEntityManager();
		System.out.println("done");
		/*
		 * Register hibernate statistics service to see if second level cache is
		 * working.
		 */
		EntityManagerUtil.registerPersistenceJMX();

		/*
		 * Actual test (not automated - requires user interaction). Note that
		 * caching behavioral can't be determined just by looking at spent time.
		 * One reason for this is because above test preparation indirectly
		 * populates caches. On the other hand by simply viewing statistics of
		 * persistence provider user can be sure that caches are working.
		 * jconsole tool will help with that.
		 * 
		 * Also note that lazy loading will have to be disabled in order to
		 * preform this test!
		 */
		for (int i = 0; i < 5; i++) {
			EntityManagerUtil.currentEntityManager();
			long start = System.currentTimeMillis();
			Project p = dao.load(project.getId());
			for (File f : p) {
				assertNotNull("file id is null.", f.getId());
				assertNotNull("file name is null.", f.getName());
			}
			EntityManagerUtil.closeEntityManager();
			long end = System.currentTimeMillis();
			System.out.println("Project.files - cache test: " + (end - start)
					+ "ms");
			assertNotNull("project is null", p);
		}
		/*
		 * Pause so user can view statistics in jconsole
		 */
		Thread.sleep(Long.MAX_VALUE);
	}

	/**
	 * Test to see if hibernate second level collection cache is working
	 */
	@Ignore("already tested")
	@Test
	public void collectionCacheTest() throws Exception {
		// prepair test by storing 500 files in database
		System.out.print("Prepairing Project cache collection test...");
		for (int i = 0; i < 500; i++) {
			String name = "name" + (i + 1);
			new File(project, name, FileTypes.VHDL_SOURCE, "abcdef");
		}
		EntityManagerUtil.currentEntityManager();
		dao.save(project);
		EntityManagerUtil.closeEntityManager();
		System.out.println("done");
		/*
		 * Register hibernate statistics service to see if second level cache is
		 * working.
		 */
		EntityManagerUtil.registerPersistenceJMX();

		/*
		 * Actual test (not automated - requires user interaction). Note that
		 * caching behavioral can't be determined just by looking at spent time.
		 * One reason for this is because above test preparation indirectly
		 * populates caches. On the other hand by simply viewing statistics of
		 * persistence provider user can be sure that caches are working.
		 * jconsole tool will help with that.
		 * 
		 * Also note that lazy loading will have to be disabled in order to
		 * preform this test!
		 */
		for (int i = 0; i < 5; i++) {
			EntityManagerUtil.currentEntityManager();
			long start = System.currentTimeMillis();
			Project p = dao.load(project.getId());
			EntityManagerUtil.closeEntityManager();
			long end = System.currentTimeMillis();
			System.out.println("Project.files - cache test: " + (end - start)
					+ "ms");
			assertNotNull("project is null", p);
		}
		/*
		 * Pause so user can view statistics in jconsole
		 */
		Thread.sleep(Long.MAX_VALUE);
	}

}
