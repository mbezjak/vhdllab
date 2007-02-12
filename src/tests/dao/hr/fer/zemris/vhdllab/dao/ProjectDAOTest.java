package hr.fer.zemris.vhdllab.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import hr.fer.zemris.vhdllab.init.TestManager;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ProjectDAOTest {

	private TestManager testManager;
	private FileDAO fileDAO;
	private ProjectDAO projectDAO;
	private GlobalFileDAO globalFileDAO;
	private UserFileDAO userFileDAO;
	private String userId;

	@Parameters
	public static Collection<Object[]> data() {
		return TestManager.getDAOMemoryParametars();
	}

	public ProjectDAOTest(FileDAO fileDAO, ProjectDAO projectDAO,
			GlobalFileDAO globalFileDAO, UserFileDAO userFileDAO) {

		this.fileDAO = fileDAO;
		this.projectDAO = projectDAO;
		this.globalFileDAO = globalFileDAO;
		this.userFileDAO = userFileDAO;
	}

	@Before
	public void initEachTest() {
		testManager = new TestManager(fileDAO, projectDAO, globalFileDAO, userFileDAO);
		userId = testManager.getUnusedUserId();
		testManager.initProjects(userId);
	}

	@After
	public void cleanEachTest() {
		testManager.cleanProjects(userId);
	}

	/**
	 * id is null
	 */
	@Test(expected=DAOException.class)
	public void load() throws DAOException {
		projectDAO.load(null);
	}

	/**
	 * everything ok
	 */
	public void load2() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		Project loadedProject = projectDAO.load(project.getId());
		assertEquals(project, loadedProject);
	}

	/**
	 * non-existing project id
	 */
	@Test(expected=DAOException.class)
	public void load3() throws DAOException {
		projectDAO.load(testManager.getUnusedProjectId());
	}

	/**
	 * test load after project's files are set to null
	 */
	@Test
	public void load4() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		project.setFiles(null);
		projectDAO.save(project);
		project.setFiles(new TreeSet<File>());
		Project project2 = projectDAO.load(project.getId());
		assertEquals(project, project2);
	}

	/**
	 * project is null
	 */
	@Test(expected=DAOException.class)
	public void save() throws DAOException {
		projectDAO.save(null);
	}

	/**
	 * owner id is null
	 */
	@Test(expected=DAOException.class)
	public void save2() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		project.setOwnerId(null);
		projectDAO.save(project);
	}

	/**
	 * owner id too long
	 */
	@Test(expected=DAOException.class)
	public void save3() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		project.setOwnerId(TestManager.generateOverMaxJunkOwnerId());
		projectDAO.save(project);
	}

	/**
	 * project name is null
	 */
	@Test(expected=DAOException.class)
	public void save4() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		project.setProjectName(null);
		projectDAO.save(project);
	}

	/**
	 * project name too long
	 */
	@Test(expected=DAOException.class)
	public void save5() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		project.setProjectName(TestManager.generateOverMaxJunkProjectName());
		projectDAO.save(project);
	}

	/**
	 * files is null
	 */
	@Test
	public void save6() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		project.setFiles(null);
		projectDAO.save(project);
		assertEquals(new ArrayList<Project>(0), projectDAO.load(project.getId()));
	}

	/**
	 * everything ok
	 */
	@Test
	public void save7() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		project.setProjectName("abc");
		projectDAO.save(project);
	}

	/**
	 * test transfering a file to another project
	 */
	@Test
	public void save8() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		Project previousProject = file.getProject();
		Project project = testManager.pickRandomEmptyProject(userId);
		project.addFile(file);
		projectDAO.save(project);
		assertEquals(true, project.getFiles().contains(file));
		assertEquals(false, previousProject.getFiles().contains(file));
		assertEquals(true, fileDAO.exists(file.getId()));
		assertNotSame(previousProject, file.getProject());
	}

	/**
	 * test removing a file from project
	 */
	@Test
	public void save9() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		Project project = file.getProject();
		project.removeFile(file);
		projectDAO.save(project);
		assertEquals(false, project.getFiles().contains(file));
		assertEquals(false, fileDAO.exists(file.getId()));
	}

	/**
	 * project is null
	 */
	@Test(expected=DAOException.class)
	public void delete() throws DAOException {
		projectDAO.delete(null);
	}

	/**
	 * everything ok
	 */
	@Test
	public void delete2() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		projectDAO.delete(project);
		assertEquals(false, projectDAO.exists(project.getId()));
	}

	/**
	 * non-existing project
	 */
	@Test(expected=DAOException.class)
	public void delete3() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		project.setId(testManager.getUnusedProjectId());
		projectDAO.delete(project);
	}

	/**
	 * project id is null
	 */
	@Test(expected=DAOException.class)
	public void exists() throws DAOException {
		projectDAO.exists(null);
	}

	/**
	 * non-existing project id
	 */
	@Test
	public void exists2() throws DAOException {
		assertEquals(false, projectDAO.exists(testManager.getUnusedProjectId()));
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists3() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		assertEquals(true, projectDAO.exists(project.getId()));
	}

	/**
	 * owner id is null
	 */
	@Test(expected=DAOException.class)
	public void exists4() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		projectDAO.exists(null, project.getProjectName());
	}

	/**
	 * project name is null
	 */
	@Test(expected=DAOException.class)
	public void exists5() throws DAOException {
		projectDAO.exists(userId, null);
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists6() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		assertEquals(true, projectDAO.exists(userId, project.getProjectName()));
	}

	/**
	 * non-existing project name
	 */
	@Test
	public void exists7() throws DAOException {
		assertEquals(false, projectDAO.exists(userId, testManager.getUnusedProjectName(userId)));
	}

	/**
	 * non-existing user id
	 */
	@Test
	public void exists8() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		assertEquals(false, projectDAO.exists(testManager.getUnusedUserId(), project.getProjectName()));
	}

	/**
	 * test file name casing
	 */
	@Test
	public void exists9() throws DAOException {
		Project project = testManager.pickRandomProject(userId);
		assertEquals(true, projectDAO.exists(userId, project.getProjectName().toUpperCase()));
	}

	/**
	 * user id is null
	 */
	@Test(expected=DAOException.class)
	public void findByUser() throws DAOException {
		projectDAO.findByUser(null);
	}

	/**
	 * non-existing user id
	 */
	@Test
	public void findByUser2() throws DAOException {
		assertEquals(new ArrayList<Project>(0), projectDAO.findByUser(testManager.getUnusedUserId()));
	}

	/**
	 * non-existing user id
	 */
	@Test
	public void findByUser3() throws DAOException {
		List<Project> expectedProjects = testManager.getProjectsByUser(userId);
		assertEquals(expectedProjects, projectDAO.findByUser(userId));
	}

}
