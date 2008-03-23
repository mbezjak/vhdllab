package hr.fer.zemris.vhdllab.dao.impl;

import static hr.fer.zemris.vhdllab.dao.impl.StringGenerationUtil.generateJunkString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for {@link FileDAOImpl}.
 * 
 * @author Miro Bezjak
 */
public class FileDAOImplTest {

	private static final String NAME = "simple.file.name";
	private static final String TYPE = "simple.file.type";
	private static final String USER_ID = "user.identifier";
	private static final Long UNUSED_ID = Long.valueOf(Long.MAX_VALUE);
	private static final Long UNUSED_PROJECT_ID = Long
			.valueOf(Long.MAX_VALUE - 1);
	private static final String UNUSED_NAME = "unused.name";
	private static final String UNUSED_TYPE = "unused.type";
	private static final int MAX_NAME_LENGTH = 255;
	private static final int MAX_TYPE_LENGTH = MAX_NAME_LENGTH;
	private static final int MAX_CONTENT_LENGTH = 16000000;

	private static FileDAO dao;
	private static ProjectDAO projectDAO;
	private Project project;
	private File file;

	@BeforeClass
	public static void initTestCase() throws DAOException {
		dao = new FileDAOImpl();
		projectDAO = new ProjectDAOImpl();
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
		project.setName("project.name");
		project.setUserId(USER_ID);
		EntityManagerUtil.currentEntityManager();
		projectDAO.save(project);
		EntityManagerUtil.closeEntityManager();

		file = new File();
		file.setName(NAME);
		file.setType(TYPE);
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
		 * Create a new entity manager for destroying files to isolate any
		 * errors in a test
		 */
		EntityManagerUtil.currentEntityManager();
		for (Project p : projectDAO.findByUser(USER_ID)) {
			projectDAO.delete(p.getId());
		}
		EntityManagerUtil.closeEntityManager();
	}

	/**
	 * save a file then load it and see it they are the same
	 */
	@Test
	public void saveAndLoad() throws DAOException {
		dao.save(file);
		File loadedFile = dao.load(file.getId());
		assertEquals(file, loadedFile);
	}

	/**
	 * save a file then delete it
	 */
	@Test
	public void delete() throws DAOException {
		dao.save(file);
		dao.delete(file.getId());
		assertEquals(false, dao.exists(file.getId()));
		assertEquals(false, dao.exists(file.getProject().getId(), file
				.getName()));
	}

	/**
	 * project id is null
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
		dao.exists(file.getProject().getId(), null);
	}

	/**
	 * non-existing project id and name
	 */
	@Test
	public void exists3() throws DAOException {
		assertEquals(false, dao.exists(UNUSED_PROJECT_ID, file.getName()));
		assertEquals(false, dao.exists(file.getProject().getId(), UNUSED_NAME));
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists4() throws DAOException {
		dao.save(file);
		assertEquals(true, dao.exists(file.getId()));
		assertEquals(true, dao
				.exists(file.getProject().getId(), file.getName()));
		assertEquals(true, dao.exists(file.getProject().getId(), file.getName()
				.toUpperCase()));
	}

	/**
	 * Once file is persisted an ID is no longer null
	 */
	@Test()
	public void save() throws DAOException {
		assertEquals(null, file.getId());
		dao.save(file);
		assertNotSame(null, file.getId());
	}

	/**
	 * ID can't be a part of insert statement
	 */
	@Test(expected = DAOException.class)
	public void save2() throws DAOException {
		file.setId(UNUSED_ID);
		dao.save(file);
	}

	/**
	 * ID can't be a part of update statement
	 */
	@Test(expected = DAOException.class)
	public void save3() throws DAOException {
		dao.save(file);
		file.setId(UNUSED_ID);
		dao.save(file);
	}

	/**
	 * name is null
	 */
	@Test(expected = DAOException.class)
	public void save4() throws DAOException {
		file.setName(null);
		dao.save(file);
	}

	/**
	 * name is of max length
	 */
	@Test()
	public void save5() throws DAOException {
		file.setName(generateJunkString(MAX_NAME_LENGTH));
		dao.save(file);
	}

	/**
	 * name is too long
	 */
	@Test(expected = DAOException.class)
	public void save6() throws DAOException {
		file.setName(generateJunkString(MAX_NAME_LENGTH + 1));
		dao.save(file);
	}

	/**
	 * type is null
	 */
	@Test(expected = DAOException.class)
	public void save7() throws DAOException {
		file.setType(null);
		dao.save(file);
	}

	/**
	 * type is of max length
	 */
	@Test()
	public void save8() throws DAOException {
		file.setType(generateJunkString(MAX_TYPE_LENGTH));
		dao.save(file);
	}

	/**
	 * type is too long
	 */
	@Test(expected = DAOException.class)
	public void save9() throws DAOException {
		file.setType(generateJunkString(MAX_TYPE_LENGTH + 1));
		dao.save(file);
	}

	/**
	 * content is null
	 */
	@Test
	public void save10() throws DAOException {
		file.setContent(null);
		dao.save(file);
	}

	/**
	 * content is of max length
	 */
	@Test()
	public void save11() throws DAOException {
		file.setContent(generateJunkString(MAX_CONTENT_LENGTH));
		dao.save(file);
	}

	/**
	 * content is too long
	 */
	@Test(expected = DAOException.class)
	public void save12() throws DAOException {
		// add 1MB to MAX_CONTENT_LENGTH to be sure
		file.setContent(generateJunkString(MAX_CONTENT_LENGTH + 1000000));
		dao.save(file);
	}

	/**
	 * project is null
	 */
	@Test(expected = DAOException.class)
	public void save13() throws DAOException {
		file.setProject(null);
		dao.save(file);
	}

	/**
	 * non-existing project (can't cascade to persist a project)
	 */
	@Ignore("there is no way to implement this feature because mysql is alowing foreign reference to be set to any value")
	@Test(expected = DAOException.class)
	public void save14() throws DAOException {
		Project newProject = new Project();
		newProject.setId(UNUSED_PROJECT_ID);
		newProject.setUserId(USER_ID);
		newProject.setName("new.project.name");
		newProject.addFile(file);
		dao.save(file);
	}

	/**
	 * project id and name are unique
	 */
	@Test(expected = DAOException.class)
	public void save15() throws DAOException {
		dao.save(file);
		File clone = new File(file);
		clone.setId(null);
		clone.setType("type");
		clone.setContent("abc");
		dao.save(clone);
	}

	/**
	 * save a file then update it
	 */
	@Test
	public void save16() throws DAOException {
		dao.save(file);
		file.setName(UNUSED_NAME);
		file.setType(UNUSED_TYPE);
		file.setContent("abc");
		dao.save(file);
		assertEquals(true, dao.exists(file.getId()));
		assertEquals(file, dao.load(file.getId()));
	}

	/**
	 * project ID can't be updated
	 */
	@Test
	public void save17() throws DAOException {
		Project newProject = new Project(project);
		newProject.setId(null);
		newProject.setFiles(new HashSet<File>(0));
		newProject.setName("new.project.name");
		projectDAO.save(newProject);
		dao.save(file);

		newProject.addFile(file);
		dao.save(file);
		EntityManagerUtil.closeEntityManager();
		EntityManagerUtil.currentEntityManager();
		assertEquals(true, dao.exists(file.getId()));
		assertEquals(project, dao.load(file.getId()).getProject());
	}

	/**
	 * project id is null
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
		dao.findByName(file.getProject().getId(), null);
	}

	/**
	 * non-existing project id
	 */
	@Test(expected = DAOException.class)
	public void findByName3() throws DAOException {
		dao.findByName(UNUSED_PROJECT_ID, NAME);
	}

	/**
	 * non-existing name
	 */
	@Test(expected = DAOException.class)
	public void findByName4() throws DAOException {
		dao.findByName(file.getProject().getId(), UNUSED_NAME);
	}

	/**
	 * everything ok
	 */
	@Test
	public void findByName5() throws DAOException {
		dao.save(file);
		assertEquals(file, dao.findByName(file.getProject().getId(), file
				.getName()));
		assertEquals(file, dao.findByName(file.getProject().getId(), file
				.getName().toUpperCase()));
	}

}
