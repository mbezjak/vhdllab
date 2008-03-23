package hr.fer.zemris.vhdllab.dao.impl;

import static hr.fer.zemris.vhdllab.dao.impl.StringGenerationUtil.generateJunkString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
	private static final Long UNUSED_ID = Long.valueOf(Long.MAX_VALUE);
	private static final String UNUSED_NAME = "unused.name";
	private static final String UNUSED_USER_ID = "unused.user.identifier";
	private static final int MAX_NAME_LENGTH = 255;
	private static final int MAX_USER_ID_LENGTH = MAX_NAME_LENGTH;

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
		project = new Project();
		project.setName(NAME);
		project.setUserId(USER_ID);
		file = new File();
		file.setName("file.name");
		file.setType("file.type");
		file.setContent("<file>int main() {}</file>");
		project.addFile(file);
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
		for (Project p : dao.findByUser(UNUSED_USER_ID)) {
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
		assertEquals(project, loadedProject);
	}

	/**
	 * save a project then delete it
	 */
	@Test
	public void delete() throws DAOException {
		dao.save(project);
		dao.delete(project.getId());
		assertEquals(false, dao.exists(project.getId()));
		assertEquals(false, dao.exists(project.getUserId(), project.getName()));
		assertEquals(false, fileDAO.exists(file.getId()));
	}

	/**
	 * user id is null
	 */
	@Test(expected = NullPointerException.class)
	public void exists() throws DAOException {
		dao.exists(null, NAME);
	}

	/**
	 * name is null
	 */
	@Test(expected = NullPointerException.class)
	public void exists2() throws DAOException {
		dao.exists(USER_ID, null);
	}

	/**
	 * non-existing name and user id
	 */
	@Test
	public void exists3() throws DAOException {
		assertEquals(false, dao.exists(UNUSED_USER_ID, NAME));
		assertEquals(false, dao.exists(USER_ID, UNUSED_NAME));
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists4() throws DAOException {
		dao.save(project);
		assertEquals(true, dao.exists(project.getId()));
		assertEquals(true, dao.exists(project.getUserId(), project.getName()));
		assertEquals(true, dao.exists(project.getUserId().toUpperCase(),
				project.getName().toUpperCase()));
	}

	/**
	 * Once project is persisted an ID is no longer null
	 */
	@Test()
	public void save() throws DAOException {
		assertEquals(null, project.getId());
		dao.save(project);
		assertNotSame(null, project.getId());
	}

	/**
	 * ID can't be a part of insert statement
	 */
	@Test(expected = DAOException.class)
	public void save2() throws DAOException {
		project.setId(UNUSED_ID);
		dao.save(project);
	}

	/**
	 * ID can't be a part of update statement
	 */
	@Test(expected = DAOException.class)
	public void save3() throws DAOException {
		dao.save(project);
		project.setId(UNUSED_ID);
		dao.save(project);
	}

	/**
	 * user id is null
	 */
	@Test(expected = DAOException.class)
	public void save4() throws DAOException {
		project.setUserId(null);
		dao.save(project);
	}

	/**
	 * user id is of max length
	 */
	@Test()
	public void save5() throws DAOException {
		project.setUserId(generateJunkString(MAX_USER_ID_LENGTH));
		dao.save(project);
		// delete project afterwards
		dao.delete(project.getId());
	}

	/**
	 * user id is too long
	 */
	@Test(expected = DAOException.class)
	public void save6() throws DAOException {
		project.setUserId(generateJunkString(MAX_USER_ID_LENGTH + 1));
		dao.save(project);
	}

	/**
	 * name is null
	 */
	@Test(expected = DAOException.class)
	public void save7() throws DAOException {
		project.setName(null);
		dao.save(project);
	}

	/**
	 * user id and name are unique
	 */
	@Test(expected = DAOException.class)
	public void save8() throws DAOException {
		dao.save(project);
		Project clone = new Project(project);
		clone.setId(null);
		clone.setFiles(null);
		dao.save(clone);
	}

	/**
	 * name is of max length
	 */
	@Test()
	public void save9() throws DAOException {
		project.setName(generateJunkString(MAX_NAME_LENGTH));
		dao.save(project);
	}

	/**
	 * name is too long
	 */
	@Test(expected = DAOException.class)
	public void save10() throws DAOException {
		project.setName(generateJunkString(MAX_NAME_LENGTH + 1));
		dao.save(project);
	}

	/**
	 * save a project then update it
	 */
	@Test
	public void save11() throws DAOException {
		dao.save(project);
		project.setUserId(UNUSED_USER_ID);
		project.setName(UNUSED_NAME);
		project.setFiles(null);
		dao.save(project);
		assertEquals(true, dao.exists(project.getId()));
		assertEquals(project, dao.load(project.getId()));
	}

	/**
	 * save a project with same user id but different name
	 */
	@Test
	public void save12() throws DAOException {
		Project newProject = new Project();
		newProject.setUserId(USER_ID);
		newProject.setName(UNUSED_NAME);
		newProject.setFiles(null);
		dao.save(project);
		dao.save(newProject);
		assertEquals(true, dao.exists(newProject.getId()));
		assertEquals(newProject, dao.load(newProject.getId()));
	}

	/**
	 * save a project with same name but different user id
	 */
	@Test
	public void save13() throws DAOException {
		Project newProject = new Project();
		newProject.setUserId(UNUSED_USER_ID);
		newProject.setName(NAME);
		newProject.setFiles(null);
		dao.save(project);
		dao.save(newProject);
		assertEquals(true, dao.exists(newProject.getId()));
		assertEquals(newProject, dao.load(newProject.getId()));
	}

	/**
	 * add a file after a project is saved
	 */
	@Test
	public void save14() throws DAOException {
		dao.save(project);
		File newFile = new File();
		newFile.setName("new.file.name");
		newFile.setType("new.file.type");
		project.addFile(newFile);
		dao.save(project);

		Project loadedProject = dao.load(project.getId());
		assertEquals(new HashSet<File>(project.getFiles()), new HashSet<File>(
				loadedProject.getFiles()));
		assertEquals(project, loadedProject);
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
	@Test(expected = DAOException.class)
	public void findByName3() throws DAOException {
		dao.findByName(UNUSED_USER_ID, NAME);
	}

	/**
	 * non-existing name
	 */
	@Test(expected = DAOException.class)
	public void findByName4() throws DAOException {
		dao.findByName(USER_ID, UNUSED_NAME);
	}

	/**
	 * everything ok
	 */
	@Test
	public void findByName5() throws DAOException {
		dao.save(project);
		assertEquals(project, dao.findByName(project.getUserId(), project
				.getName()));
		assertEquals(project, dao.findByName(project.getUserId().toUpperCase(),
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
		assertEquals(Collections.emptyList(), dao.findByUser(UNUSED_USER_ID));
	}

	/**
	 * everything ok. one project in collection
	 */
	@Test
	public void findByUser3() throws DAOException {
		dao.save(project);
		List<Project> projects = new ArrayList<Project>(1);
		projects.add(project);
		assertEquals(projects, dao.findByUser(project.getUserId()));
		assertEquals(projects, dao
				.findByUser(project.getUserId().toUpperCase()));
	}

	/**
	 * everything ok. two projects in collection
	 */
	@Test
	public void findByUser4() throws DAOException {
		Project project2 = new Project();
		project2.setUserId(USER_ID);
		project2.setName(UNUSED_NAME);
		project2.setFiles(null);
		dao.save(project);
		dao.save(project2);
		List<Project> projects = new ArrayList<Project>(2);
		projects.add(project);
		projects.add(project2);
		assertEquals(projects, dao.findByUser(project.getUserId()));
		assertEquals(projects, dao
				.findByUser(project.getUserId().toUpperCase()));
	}

	/**
	 * everything ok. two collections
	 */
	@Test
	public void findByUser5() throws DAOException {
		Project project2 = new Project();
		project2.setUserId(UNUSED_USER_ID);
		project2.setName(UNUSED_NAME);
		project2.setFiles(null);
		dao.save(project);
		dao.save(project2);
		List<Project> collection1 = new ArrayList<Project>(1);
		List<Project> collection2 = new ArrayList<Project>(1);
		collection1.add(project);
		collection2.add(project2);
		assertEquals(collection1, dao.findByUser(project.getUserId()));
		assertEquals(collection1, dao.findByUser(project.getUserId()
				.toUpperCase()));
		assertEquals(collection2, dao.findByUser(project2.getUserId()));
		assertEquals(collection2, dao.findByUser(project2.getUserId()
				.toUpperCase()));
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
			Project p = new Project();
			p.setUserId(USER_ID);
			p.setName("name" + (i + 1));
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
			System.out.println("Project.findByUser() - query cache test: "
					+ (end - start) + "ms");
			assertNotSame(null, projects);
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
			File f = new File();
			f.setName("name" + (i + 1));
			f.setType("file.type");
			f.setContent("abcdef");
			project.addFile(f);
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
			assertNotSame(null, p);
		}
		/*
		 * Pause so user can view statistics in jconsole
		 */
		Thread.sleep(Long.MAX_VALUE);
	}
}
