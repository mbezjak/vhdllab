package hr.fer.zemris.vhdllab.dao;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.init.TestManager;
import hr.fer.zemris.vhdllab.model.UserFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class UserFileDAOTest {

	private TestManager man;
	private FileDAO fileDAO;
	private ProjectDAO projectDAO;
	private GlobalFileDAO globalFileDAO;
	private UserFileDAO userFileDAO;
	private String userId;

	@Parameters
	public static Collection<Object[]> data() {
		return TestManager.getDAOParametars();
	}

	public UserFileDAOTest(FileDAO fileDAO, ProjectDAO projectDAO,
			GlobalFileDAO globalFileDAO, UserFileDAO userFileDAO) {

		this.fileDAO = fileDAO;
		this.projectDAO = projectDAO;
		this.globalFileDAO = globalFileDAO;
		this.userFileDAO = userFileDAO;
	}

	@Before
	public void initEachTest() {
		man = new TestManager(fileDAO, projectDAO, globalFileDAO, userFileDAO);
		userId = man.getUnusedUserId();
		man.initUserFiles(userId);
	}

	@After
	public void cleanEachTest() {
		man.cleanUserFiles(userId);
	}
	
	/**
	 * id is null
	 */
	@Test(expected=DAOException.class)
	public void load() throws DAOException {
		userFileDAO.load(null);
	}
	
	/**
	 * non-existing user id
	 */
	@Test(expected=DAOException.class)
	public void load2() throws DAOException {
		userFileDAO.load(man.getUnusedUserFileId());
	}
	
	@Test
	public void load3() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		UserFile loadedFile = userFileDAO.load(file.getId());
		assertEquals(file, loadedFile);
	}
	
	/**
	 * user file is null
	 */
	@Test(expected=DAOException.class)
	public void save() throws DAOException {
		userFileDAO.save(null);
	}
	
	/**
	 * owner id is null
	 */
	@Test(expected=DAOException.class)
	public void save2() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		file.setOwnerID(null);
		userFileDAO.save(file);
	}
	
	/**
	 * owner id is too long
	 */
	@Test(expected=DAOException.class)
	public void save3() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		file.setOwnerID(TestManager.generateOverMaxJunkOwnerId());
		userFileDAO.save(file);
	}
	
	/**
	 * name is null
	 */
	@Test(expected=DAOException.class)
	public void save4() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		file.setName(null);
		userFileDAO.save(file);
	}
	
	/**
	 * name is too long
	 */
	@Test(expected=DAOException.class)
	public void save5() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		file.setName(TestManager.generateOverMaxJunkUserFileName());
		userFileDAO.save(file);
	}
	
	/**
	 * type is null
	 */
	@Test(expected=DAOException.class)
	public void save6() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		file.setType(null);
		userFileDAO.save(file);
	}
	
	/**
	 * type is too long
	 */
	@Test(expected=DAOException.class)
	public void save7() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		file.setType(TestManager.generateOverMaxJunkUserFileType());
		userFileDAO.save(file);
	}
	
	/**
	 * content is null
	 */
	@Test
	public void save8() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		file.setContent(null);
		userFileDAO.save(file);
	}
	
	/**
	 * content is too long
	 */
	@Test(expected=DAOException.class)
	public void save9() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		file.setContent(TestManager.generateOverMaxJunkUserFileContent());
		userFileDAO.save(file);
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void save10() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		file.setName("abc");
		assertEquals(file, userFileDAO.load(file.getId()));
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void save11() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		file.setType("abc");
		assertEquals(file, userFileDAO.load(file.getId()));
	}
	
	/**
	 * test user file name casing
	 */
	@Test
	public void save12() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		String name = file.getName().toUpperCase();
		file.setName(name);
		userFileDAO.save(file);
		assertEquals(name, userFileDAO.load(file.getId()).getName());
	}
	
	@Test(expected=DAOException.class)
	public void delete() throws DAOException {
		userFileDAO.delete(null);
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void delete2() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		userFileDAO.delete(file);
		assertEquals(false, userFileDAO.exists(file.getId()));
		assertEquals(false, userFileDAO.exists(userId, file.getName()));
	}
	
	/**
	 * non-existing global file
	 */
	@Test(expected=DAOException.class)
	public void delete3() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		file.setId(man.getUnusedUserFileId());
		userFileDAO.delete(file);
	}
	
	/**
	 * id is null
	 */
	@Test(expected=DAOException.class)
	public void exists() throws DAOException {
		userFileDAO.exists(null);
	}
	
	/**
	 * non-existing id
	 */
	@Test
	public void exists2() throws DAOException {
		assertEquals(false, userFileDAO.exists(man.getUnusedUserFileId()));
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void exists3() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		assertEquals(true, userFileDAO.exists(file.getId()));
	}
	
	/**
	 * user id is null
	 */
	@Test(expected=DAOException.class)
	public void exists4() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		userFileDAO.exists(null, file.getName());
	}
	
	/**
	 * name is null
	 */
	@Test(expected=DAOException.class)
	public void exists5() throws DAOException {
		userFileDAO.exists(userId, null);
	}
	
	/**
	 * non-existing user id
	 */
	@Test
	public void exists6() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		assertEquals(false, userFileDAO.exists(man.getUnusedUserId(), file.getName()));
	}
	
	/**
	 * user id is null
	 */
	@Test
	public void exists7() throws DAOException {
		assertEquals(false, userFileDAO.exists(userId, man.getUnusedUserFileName(userId)));
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void exists8() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		assertEquals(true, userFileDAO.exists(userId, file.getName()));
	}
	
	/**
	 * test user file name casing
	 */
	@Test
	public void exists9() throws DAOException {
		UserFile file = man.pickRandomUserFile(userId);
		assertEquals(true, userFileDAO.exists(userId, file.getName().toUpperCase()));
	}

	/**
	 * user id is null
	 */
	@Test(expected=DAOException.class)
	public void findByUser() throws DAOException {
		userFileDAO.findByUser(null);
	}
	
	/**
	 * non-existing user id
	 */
	@Test
	public void findByUser2() throws DAOException {
		assertEquals(new ArrayList<UserFile>(0), userFileDAO.findByUser(man.getUnusedUserId()));
	}

	/**
	 * everything ok
	 */
	@Test
	public void findByUser3() throws DAOException {
		List<UserFile> expectedFiles = man.getUserFilesByUser(userId);
		assertEquals(expectedFiles, userFileDAO.findByUser(userId));
	}
	
	/**
	 * user id is null
	 */
	@Test(expected=DAOException.class)
	public void findByName() throws DAOException {
		userFileDAO.findByName(null, man.pickRandomUserFile(userId).getName());
	}
	
	/**
	 * user file name is null
	 */
	@Test(expected=DAOException.class)
	public void findByName2() throws DAOException {
		userFileDAO.findByName(userId, null);
	}
	
	/**
	 * non-existing user id
	 */
	@Test(expected=DAOException.class)
	public void findByName3() throws DAOException {
		userFileDAO.findByName(man.getUnusedUserId(), man.pickRandomUserFile(userId).getName());
	}
	
	/**
	 * non-existing user file name
	 */
	@Test(expected=DAOException.class)
	public void findByName4() throws DAOException {
		userFileDAO.findByName(userId, man.getUnusedUserFileName(userId));
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void findByName5() throws DAOException {
		String name = man.pickRandomUserFile(userId).getName();
		UserFile expectedFile = man.getUserFileByName(userId, name);
		assertEquals(expectedFile, userFileDAO.findByName(userId, name));
	}
	
}
