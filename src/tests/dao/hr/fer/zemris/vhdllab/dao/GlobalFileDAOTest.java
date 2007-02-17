package hr.fer.zemris.vhdllab.dao;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.init.TestManager;
import hr.fer.zemris.vhdllab.model.GlobalFile;

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
public class GlobalFileDAOTest {

	private TestManager man;
	private FileDAO fileDAO;
	private ProjectDAO projectDAO;
	private GlobalFileDAO globalFileDAO;
	private UserFileDAO userFileDAO;

	@Parameters
	public static Collection<Object[]> data() {
		return TestManager.getDAOParametars();
	}

	public GlobalFileDAOTest(FileDAO fileDAO, ProjectDAO projectDAO,
			GlobalFileDAO globalFileDAO, UserFileDAO userFileDAO) {

		this.fileDAO = fileDAO;
		this.projectDAO = projectDAO;
		this.globalFileDAO = globalFileDAO;
		this.userFileDAO = userFileDAO;
	}

	@Before
	public void initEachTest() {
		man = new TestManager(fileDAO, projectDAO, globalFileDAO, userFileDAO);
		man.initGlobalFiles();
	}

	@After
	public void cleanEachTest() {
		man.cleanGlobalFiles();
	}

	/**
	 * id is null
	 */
	@Test(expected=DAOException.class)
	public void load() throws DAOException {
		globalFileDAO.load(null);
	}

	/**
	 * non-existing global file id
	 */
	@Test(expected=DAOException.class)
	public void load2() throws DAOException {
		globalFileDAO.load(man.getUnusedGlobalFileId());
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void load3() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		GlobalFile loadedFile = globalFileDAO.load(file.getId());
		assertEquals(file, loadedFile);
	}
	
	/**
	 * id is null
	 */
	@Test(expected=DAOException.class)
	public void save() throws DAOException {
		globalFileDAO.save(null);
	}
	
	/**
	 * name is null
	 */
	@Test(expected=DAOException.class)
	public void save2() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		file.setName(null);
		globalFileDAO.save(file);
	}
	
	/**
	 * name is too long
	 */
	@Test(expected=DAOException.class)
	public void save3() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		file.setName(TestManager.generateOverMaxJunkGlobalFileName());
		globalFileDAO.save(file);
	}
	
	/**
	 * test if name is unique
	 */
	@Test(expected=DAOException.class)
	public void save4() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		GlobalFile newFile = new GlobalFile(file);
		newFile.setId(man.getUnusedGlobalFileId());
		globalFileDAO.save(newFile);
	}
	
	/**
	 * type is null
	 */
	@Test(expected=DAOException.class)
	public void save5() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		file.setType(null);
		globalFileDAO.save(file);
	}
	
	/**
	 * type is too long
	 */
	@Test(expected=DAOException.class)
	public void save6() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		file.setType(TestManager.generateOverMaxJunkGlobalFileType());
		globalFileDAO.save(file);
	}
	
	/**
	 * content is null
	 */
	@Test
	public void save7() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		file.setContent(null);
		globalFileDAO.save(file);
	}

	/**
	 * content is too long
	 */
	@Test(expected=DAOException.class)
	public void save8() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		file.setContent(TestManager.generateOverMaxJunkGlobalFileContent());
		globalFileDAO.save(file);
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void save9() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		file.setContent("abc");
		assertEquals(file, globalFileDAO.load(file.getId()));
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void save10() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		file.setName("abc");
		assertEquals(file, globalFileDAO.load(file.getId()));
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void save11() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		file.setType("abc");
		assertEquals(file, globalFileDAO.load(file.getId()));
	}
	
	/**
	 * test global file name casing
	 */
	@Test
	public void save12() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		String name = file.getName().toUpperCase();
		file.setName(name);
		globalFileDAO.save(file);
		assertEquals(name, globalFileDAO.load(file.getId()).getName());
	}
	
	/**
	 * global file is null
	 */
	@Test(expected=DAOException.class)
	public void delete() throws DAOException {
		globalFileDAO.delete(null);
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void delete2() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		globalFileDAO.delete(file);
		assertEquals(false, globalFileDAO.exists(file.getId()));
		assertEquals(false, globalFileDAO.exists(file.getName()));
	}
	
	/**
	 * non-existing global file
	 */
	@Test(expected=DAOException.class)
	public void delete3() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		file.setId(man.getUnusedGlobalFileId());
		globalFileDAO.delete(file);
	}

	/**
	 * id is null
	 */
	@Test(expected=DAOException.class)
	public void exists() throws DAOException {
		globalFileDAO.exists((Long)null);
	}
	
	/**
	 * non-existing id
	 */
	@Test
	public void exists2() throws DAOException {
		assertEquals(false, globalFileDAO.exists(man.getUnusedGlobalFileId()));
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void exists3() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		assertEquals(true, globalFileDAO.exists(file.getId()));
	}
	
	/**
	 * name is null
	 */
	@Test(expected=DAOException.class)
	public void exists4() throws DAOException {
		globalFileDAO.exists((String)null);
	}
	
	/**
	 * non-existing name
	 */
	@Test
	public void exists5() throws DAOException {
		assertEquals(false, globalFileDAO.exists(man.getUnusedGlobalFileName()));
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void exists6() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		assertEquals(true, globalFileDAO.exists(file.getName()));
	}
	
	/**
	 * test global file name casing
	 */
	@Test
	public void exists7() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		assertEquals(true, globalFileDAO.exists(file.getName().toUpperCase()));
	}

	/**
	 * type is null
	 */
	@Test(expected=DAOException.class)
	public void findByType() throws DAOException {
		globalFileDAO.findByType(null);
	}
	
	/**
	 * non-existing type
	 */
	@Test
	public void findByUser2() throws DAOException {
		List<GlobalFile> files = globalFileDAO.findByType(man.getUnusedGlobalFileType());
		assertEquals(new ArrayList<GlobalFile>(0), files);
	}
	
	/**
	 * everything ok
	 */
	@Test
	public void findByUser3() throws DAOException {
		GlobalFile file = man.pickRandomGlobalFile();
		List<GlobalFile> files = man.getGlobalFilesByType(file.getType());
		assertEquals(files, globalFileDAO.findByType(file.getType()));
	}

}
