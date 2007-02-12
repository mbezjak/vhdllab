package hr.fer.zemris.vhdllab.dao;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.init.TestManager;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class FileDAOTest {

	private TestManager testManager;
	private FileDAO fileDAO;
	private ProjectDAO projectDAO;
	private GlobalFileDAO globalFileDAO;
	private UserFileDAO userFileDAO;
	private String userId;

	@Parameters
	public static Collection<Object[]> data() {
		return TestManager.getDAOParametars();
	}

	public FileDAOTest(FileDAO fileDAO, ProjectDAO projectDAO,
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
		fileDAO.load(null);
	}

	/**
	 * everything ok
	 */
	@Test
	public void load2() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		File loadedFile = fileDAO.load(file.getId());
		assertEquals(file, loadedFile);
	}

	/**
	 * non-existing file id
	 */
	@Test(expected=DAOException.class)
	public void load3() throws DAOException {
		fileDAO.load(testManager.getUnusedFileId());
	}

	/**
	 * file is null
	 */
	@Test(expected=DAOException.class)
	public void save() throws DAOException {
		fileDAO.save(null);
	}

	/**
	 * project is null
	 */
	@Test(expected=DAOException.class)
	public void save2() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		file.setProject(null);
		fileDAO.save(file);
	}

	/**
	 * content is null
	 */
	@Test
	public void save3() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		file.setContent(null);
		fileDAO.save(file);
	}

	/**
	 * content is too long
	 */
	@Test(expected=DAOException.class)
	public void save4() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		file.setContent(TestManager.generateOverMaxJunkFileContent());
		fileDAO.save(file);
	}

	/**
	 * name is null
	 */
	@Test(expected=DAOException.class)
	public void save5() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		file.setFileName(null);
		fileDAO.save(file);
	}

	/**
	 * name is too long
	 */
	@Test(expected=DAOException.class)
	public void save6() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		file.setFileName(TestManager.generateOverMaxJunkFileName());
		fileDAO.save(file);
	}

	/**
	 * type is null
	 */
	@Test(expected=DAOException.class)
	public void save7() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		file.setFileType(null);
		fileDAO.save(file);
	}

	/**
	 * type is too long
	 */
	@Test(expected=DAOException.class)
	public void save8() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		file.setFileType(TestManager.generateOverMaxJunkFileType());
		fileDAO.save(file);
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void save9() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		file.setContent("abc");
		fileDAO.save(file);
		assertEquals(file, fileDAO.load(file.getId()));
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void save10() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		file.setFileType("abc");
		fileDAO.save(file);
		assertEquals(file, fileDAO.load(file.getId()));
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void save11() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		file.setFileName("abc");
		fileDAO.save(file);
		assertEquals(file, fileDAO.load(file.getId()));
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void save12() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		file.setProject(testManager.pickRandomEmptyProject(userId));
		fileDAO.save(file);
		assertEquals(file, fileDAO.load(file.getId()));
	}
	
	/**
	 * test deleting a file then creating it
	 */
	@Test
	public void save13() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		Project project = file.getProject();
		fileDAO.delete(file);
		file.setId(null);
		file.setProject(project);
		fileDAO.save(file);
		assertEquals(true, fileDAO.exists(file.getId()));
		assertEquals(file, fileDAO.load(file.getId()));
	}
	
	/**
	 * test file name casing
	 */
	@Test
	public void save14() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		String fileName = file.getFileName().toUpperCase();
		file.setFileName(fileName);
		fileDAO.save(file);
		assertEquals(fileName, fileDAO.load(file.getId()).getFileName());
	}

	/**
	 * file is null
	 */
	@Test(expected=DAOException.class)
	public void delete() throws DAOException {
		fileDAO.delete(null);
	}

	/**
	 * everything ok
	 */
	@Test
	public void delete2() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		fileDAO.delete(file);
		assertEquals(false, fileDAO.exists(file.getId()));
	}

	/**
	 * non-existing file
	 */
	@Test(expected=DAOException.class)
	public void delete3() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		file.setId(testManager.getUnusedFileId());
		fileDAO.delete(file);
	}

	/**
	 * file id is null
	 */
	@Test(expected=DAOException.class)
	public void exists() throws DAOException {
		fileDAO.exists(null);
	}

	/**
	 * non-existing file id
	 */
	@Test
	public void exists2() throws DAOException {
		assertEquals(false, fileDAO.exists(testManager.getUnusedFileId()));
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists3() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		assertEquals(true, fileDAO.exists(file.getId()));
	}

	/**
	 * project id is null
	 */
	@Test(expected=DAOException.class)
	public void exists4() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		fileDAO.exists(null, file.getFileName());
	}

	/**
	 * file name is null
	 */
	@Test(expected=DAOException.class)
	public void exists5() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		fileDAO.exists(file.getProject().getId(), null);
	}

	/**
	 * non-existing file id
	 */
	@Test
	public void exists6() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		assertEquals(false, fileDAO.exists(testManager.getUnusedFileId(), file.getFileName()));
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists7() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		assertEquals(true, fileDAO.exists(file.getProject().getId(), file.getFileName()));
	}

	/**
	 * non-existing file name
	 */
	@Test
	public void exists8() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		assertEquals(false, fileDAO.exists(file.getProject().getId(), testManager.getUnusedFileName(file.getProject())));
	}

	/**
	 * test file name casing
	 */
	@Test
	public void exists9() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		assertEquals(true, fileDAO.exists(file.getProject().getId(), file.getFileName().toUpperCase()));
	}

	/**
	 * non-existing project id
	 */
	@Test
	public void exists10() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		assertEquals(false, fileDAO.exists(testManager.getUnusedProjectId(), file.getFileName()));
	}
	
	/**
	 * tests existance after project is deleted
	 */
	@Test
	public void exists11() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		Project project = file.getProject();
		projectDAO.delete(project);
		for(File f : project.getFiles()) {
			assertEquals(false, fileDAO.exists(f.getId()));
		}
	}

	/**
	 * project id is null
	 */
	@Test(expected=DAOException.class)
	public void findByName() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		fileDAO.findByName(null, file.getFileName());
	}

	/**
	 * file name is null
	 */
	@Test(expected=DAOException.class)
	public void findByName2() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		fileDAO.findByName(file.getProject().getId(), null);
	}

	/**
	 * everything ok
	 */
	@Test
	public void findByName3() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		assertEquals(file, fileDAO.findByName(file.getProject().getId(), file.getFileName()));
	}

	/**
	 * non-existing file name
	 */
	@Test
	public void findByName4() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		Long projectId = file.getProject().getId();
		String name = testManager.getUnusedFileName(file.getProject());
		assertEquals(null, fileDAO.findByName(projectId, name));
	}

	/**
	 * non-existing project id
	 */
	@Test
	public void findByName5() throws DAOException {
		File file = testManager.pickRandomFile(userId);
		assertEquals(null, fileDAO.findByName(testManager.getUnusedProjectId(), file.getFileName()));
	}

}
