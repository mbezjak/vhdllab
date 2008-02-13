package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.LibraryDAO;
import hr.fer.zemris.vhdllab.dao.LibraryFileDAO;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.server.FileTypes;
import hr.fer.zemris.vhdllab.server.api.StatusCodes;

import java.io.BufferedWriter;
import java.io.FileWriter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link LibraryFileDAOImpl}.
 * 
 * @author Miro Bezjak
 */
public class LibraryFileDAOImplTest {

	private static final String NAME = "AND";
	private static final Long NEW_LIBRARY_ID = Long.MAX_VALUE;
	private static final String NEW_NAME = "new." + NAME;

	private static final java.io.File basedir;
	static {
		String tempDir = System.getProperty("java.io.tmpdir");
		tempDir += "/vhdllab";
		basedir = new java.io.File(tempDir);
	}

	private static LibraryFileDAO dao;
	private static Library library;
	private static LibraryFile file;

	@BeforeClass
	public static void initTestCase() throws Exception {
		basedir.mkdir();
		initConfFile();
		initLibrary();
		dao = new LibraryFileDAOImpl(basedir);
	}

	private static void initConfFile() throws Exception {
		String path = basedir.getPath();
		path += "/libraries.xml";
		BufferedWriter writer = new BufferedWriter(new FileWriter(path, false));
		writer.append("<libraries>\n\t");
		writer.append("<library name=\"predefined\" extension=\"vhdl\" ");
		writer.append("mappedTo=\"").append(FileTypes.VHDL_SOURCE);
		writer.append("\" />\n\t");
		writer.append("<library name=\"preferences\" extension=\"txt\" ");
		writer.append("mappedTo=\"").append(FileTypes.PREFERENCES_GLOBAL);
		writer.append("\" />\n");
		writer.append("</libraries>\n");
		writer.close();
	}

	private static void initLibrary() throws Exception {
		String lib = basedir.getPath() + "/" + "predefined";
		new java.io.File(lib).mkdir();
		String filePath = lib + "/" + NAME + ".vhdl";

		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,
				false));
		writer.append("library ieee; entity andCircuit...");
		writer.close();

		LibraryDAO libraryDAO = new LibraryDAOImpl(basedir);
		library = libraryDAO.findByName("predefined");
		file = library.iterator().next(); // first file
	}

	@AfterClass
	public static void destroyEachTest() {
		deleteDir(basedir);
		basedir.delete();
	}

	private static void deleteDir(java.io.File dir) {
		for (java.io.File f : dir.listFiles()) {
			if (f.isDirectory()) {
				deleteDir(f);
			}
			f.delete();
		}
	}

	/**
	 * Save operation is not permitted
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void save() throws DAOException {
		dao.save(file);
	}

	/**
	 * Delete operation is not permitted
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void delete() throws Exception {
		dao.delete(file.getId());
	}

	/**
	 * Id is null
	 */
	@Test(expected = NullPointerException.class)
	public void load() throws Exception {
		dao.load(null);
	}

	/**
	 * Everything ok
	 */
	@Test
	public void load2() throws Exception {
		LibraryFile loadedFile = dao.load(file.getId());
		assertEquals("files not equal.", file, loadedFile);
		assertNotNull("created date is null.", loadedFile.getCreated());
		assertNotNull("content is null.", loadedFile.getContent());
		assertEquals("types not equal.", FileTypes.VHDL_SOURCE, loadedFile
				.getType());
		assertEquals("libraries not equal.", library, loadedFile.getLibrary());
	}

	/**
	 * id is null
	 */
	@Test(expected = NullPointerException.class)
	public void exists() throws DAOException {
		dao.exists((Long) null);
	}

	/**
	 * non-existing id
	 */
	@Test
	public void exists2() throws DAOException {
		assertFalse("file exists.", dao.exists(Long.MAX_VALUE));
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists3() throws DAOException {
		assertTrue("file doesn't exist.", dao.exists(file.getId()));
	}

	/**
	 * library id is null
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
		dao.exists(file.getLibrary().getId(), null);
	}

	/**
	 * non-existing library id and name
	 */
	@Test
	public void exists6() throws Exception {
		assertFalse("file with unused library id exists.", dao.exists(
				NEW_LIBRARY_ID, NAME));
		assertFalse("file with unused name exists.", dao.exists(
				library.getId(), NEW_NAME));
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists7() throws Exception {
		assertTrue("file doesn't exists after creation.", dao.exists(file
				.getId()));
		assertTrue("file doesn't exists after creation.", dao.exists(library
				.getId(), file.getName()));
		assertTrue("file name is not case insensitive.", dao.exists(library
				.getId(), file.getName().toUpperCase()));
	}

	/**
	 * library id is null
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
		dao.findByName(file.getLibrary().getId(), null);
	}

	/**
	 * non-existing library id
	 */
	@Test
	public void findByName3() throws DAOException {
		try {
			dao.findByName(NEW_LIBRARY_ID, NAME);
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
			dao.findByName(file.getLibrary().getId(), NEW_NAME);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_DOESNT_EXIST) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * File has different extension then one provided in libraries.xml file.
	 */
	@Test(expected = DAOException.class)
	public void findByName5() throws Exception {
		String path = basedir.getPath() + "/" + library.getName() + "/"
				+ "example.txt";
		new java.io.File(path).createNewFile();
		// wrong. expected '.vhdl' not '.txt'!
		dao.findByName(file.getLibrary().getId(), "example");
	}

	/**
	 * everything ok
	 */
	@Test
	public void findByName6() throws Exception {
		assertEquals("files are not same.", file, dao.findByName(library
				.getId(), file.getName()));
		assertEquals("file name is not case insensitive.", file, dao
				.findByName(library.getId(), file.getName().toUpperCase()));
	}

}
