package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.LibraryDAO;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.server.FileTypes;
import hr.fer.zemris.vhdllab.server.api.StatusCodes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link LibraryDAOImpl}.
 * 
 * @author Miro Bezjak
 */
public class LibraryDAOImplTest {

	private static final String UNUSED_NAME = "non.existing.library";

	private static final java.io.File basedir;
	static {
		String tempDir = System.getProperty("java.io.tmpdir");
		tempDir += "/vhdllab";
		basedir = new java.io.File(tempDir);
	}

	private static LibraryDAO dao;
	private static Library library;
	private static LibraryFile file;

	@BeforeClass
	public static void initTestCase() throws Exception {
		basedir.mkdir();
		initConfFile();
		initLibrary();
		dao = new LibraryDAOImpl(basedir);
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
		String filePath = lib + "/" + "AND.vhdl";

		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,
				false));
		writer.append("library ieee; entity andCircuit...");
		writer.close();

		library = dao.findByName("predefined");
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
		dao.save(library);
	}

	/**
	 * Delete operation is not permitted
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void delete() throws Exception {
		dao.delete(library.getId());
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
		Library loadedLibrary = dao.load(library.getId());
		assertEquals("libraries not equal.", library, loadedLibrary);
		assertNotNull("created date is null.", loadedLibrary.getCreated());
		assertNotNull("collection is null", loadedLibrary.getFiles());
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
		assertFalse("library exists.", dao.exists(Long.MAX_VALUE));
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists3() throws DAOException {
		assertTrue("library doesn't exist.", dao.exists(library.getId()));
	}

	/**
	 * name is null
	 */
	@Test(expected = NullPointerException.class)
	public void exists4() throws DAOException {
		dao.exists((String) null);
	}

	/**
	 * non-existing name
	 */
	@Test
	public void exists5() throws DAOException {
		assertFalse("file exists when it shouldn't.", dao.exists(UNUSED_NAME));
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists6() throws DAOException {
		assertTrue("library doesn't exist.", dao.exists(library.getId()));
		assertTrue("library doesn't exist.", dao.exists(library.getName()));
		assertTrue("library name is not case insensitive.", dao.exists(library
				.getName().toUpperCase()));
	}

	/**
	 * name is null
	 */
	@Test(expected = NullPointerException.class)
	public void findByName() throws DAOException {
		dao.findByName(null);
	}

	/**
	 * non-existing name
	 */
	@Test
	public void findByName2() throws DAOException {
		try {
			dao.findByName(UNUSED_NAME);
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
	public void findByName3() throws DAOException {
		assertEquals("library not found.", library, dao.findByName(library
				.getName()));
		assertEquals("library name is not case insensitive.", library, dao
				.findByName(library.getName().toUpperCase()));
	}

	/**
	 * Empty and one element in collection
	 */
	@Test
	public void getAll() throws Exception {
		Set<LibraryFile> collection = new HashSet<LibraryFile>(2);
		collection.add(file);
		assertEquals("collections not same.", collection, dao.getAll());

		deleteDir(basedir);
		assertEquals("collection not empty.", Collections.emptySet(), dao
				.getAll());
	}

}
