package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.LibraryDAO;
import hr.fer.zemris.vhdllab.dao.LibraryFileDAO;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
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
 * Tests for {@link LibraryDAOImpl}.
 * 
 * @author Miro Bezjak
 */
public class LibraryDAOImplTest {

	private static final String NAME = "simple.library.name";
	private static final String NEW_NAME = "new" + NAME;

	private static LibraryFileDAO fileDAO;
	private static LibraryDAO dao;
	private Library library;
	private LibraryFile file;

	@BeforeClass
	public static void initTestCase() throws DAOException {
		fileDAO = new LibraryFileDAOImpl();
		dao = new LibraryDAOImpl();
		EntityManagerUtil.createEntityManagerFactory();
		destroyFiles();
	}

	@Before
	public void initEachTest() {
		initFiles();
		EntityManagerUtil.currentEntityManager();
	}

	private void initFiles() {
		library = new Library(NAME);
		file = new LibraryFile(library, "file.name", FileTypes.VHDL_SOURCE,
				"<file>int main() {}</file>");
	}

	@After
	public void destroyEachTest() throws DAOException {
		EntityManagerUtil.closeEntityManager();
		destroyFiles();
	}

	private static void destroyFiles() throws DAOException {
		/*
		 * Create a new entity manager for destroying libraries to isolate any
		 * errors in a test
		 */
		EntityManagerUtil.currentEntityManager();
		for (Library l : dao.getAll()) {
			dao.delete(l.getId());
		}
		EntityManagerUtil.closeEntityManager();
	}

	/**
	 * save a library then load it and see it they are the same
	 */
	@Test
	public void saveAndLoad() throws DAOException {
		dao.save(library);
		Library loadedLibrary = dao.load(library.getId());
		assertEquals("libraries not equal.", library, loadedLibrary);
		assertEquals("names not equal.", NAME, loadedLibrary.getName());
	}

	/**
	 * Library is null
	 */
	@Test(expected = NullPointerException.class)
	public void save() throws DAOException {
		dao.save(null);
	}

	/**
	 * Once library is persisted an ID is no longer null
	 */
	@Test
	public void save2() throws DAOException {
		assertNull("library has id set.", library.getId());
		dao.save(library);
		assertNotNull("library id wasn't set after creation.", library.getId());
	}

	/**
	 * Cascade to saving files when creating a library
	 */
	@Test
	public void save3() throws DAOException {
		dao.save(library);
		assertTrue("file not saved.", fileDAO.exists(file.getId()));
	}

	/**
	 * Library name is unique (i.e. form secondary key)
	 */
	@Test
	public void save4() throws Exception {
		dao.save(library);
		Library newLibrary = new Library(NAME);
		try {
			dao.save(newLibrary);
			fail("Expected DAOException");
		} catch (DAOException e) {
			if (e.getStatusCode() != StatusCodes.DAO_ALREADY_EXISTS) {
				fail("Invalid status code in DAOException");
			}
		}
	}

	/**
	 * Save a library with different name
	 */
	@Test
	public void save5() throws Exception {
		dao.save(library);
		Library newLibrary = new Library(NEW_NAME);
		dao.save(newLibrary);
		assertTrue("new library not saved.", dao.exists(newLibrary.getId()));
		assertEquals("libraries are not same.", newLibrary, dao.load(newLibrary
				.getId()));
	}

	/**
	 * add a file after a library is saved
	 */
	@Test
	public void save6() throws DAOException {
		dao.save(library);
		LibraryFile newFile = new LibraryFile(library, "new.file.name",
				"new.file.type");
		dao.save(library);

		Library loadedLibrary = dao.load(library.getId());
		assertTrue("file not save.", fileDAO.exists(newFile.getId()));
		assertTrue("file not save.", fileDAO.exists(loadedLibrary.getId(),
				newFile.getName()));
		assertEquals("libraries not equal.", library, loadedLibrary);
		assertTrue("library doesn't contain a new file.", loadedLibrary
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
	 * save a library then delete it
	 */
	@Test
	public void delete() throws DAOException {
		dao.save(library);
		assertTrue("library not saved.", dao.exists(library.getId()));
		dao.delete(library.getId());
		assertFalse("library still exists.", dao.exists(library.getId()));
		assertFalse("file still exists.", fileDAO.exists(file.getId()));
	}

	/**
	 * remove a file from a library then save library
	 */
	@Test
	public void delete2() throws DAOException {
		dao.save(library);
		assertTrue("file not saved.", fileDAO.exists(file.getId()));
		library.removeFile(file);
		assertNull("file not removed from collection.", file.getLibrary());
		assertFalse("file still in collection.", library.getFiles().contains(
				file));
		dao.save(library);
		assertFalse("file still exists.", fileDAO.exists(file.getId()));
		assertFalse("library still contains a file.", library.getFiles()
				.contains(file));
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
		dao.save(library);
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
		assertFalse("file exists when it shouldn't.", dao.exists(NEW_NAME));
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists6() throws DAOException {
		dao.save(library);
		assertTrue("library doesn't exist.", dao.exists(library.getId()));
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
	public void findByName2() {
		try {
			dao.findByName(NEW_NAME);
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
	public void getAll() throws Exception {
		dao.save(library);
		List<Library> expected = new ArrayList<Library>(1);
		expected.add(library);
		assertEquals("Lists not equal.", expected, dao.getAll());
	}

	/**
	 * no libraries
	 */
	@Test
	public void getAll2() throws Exception {
		assertEquals("List not empty.", Collections.emptyList(), dao.getAll());
	}

	/**
	 * everything ok
	 */
	@Test
	public void findByName3() throws DAOException {
		dao.save(library);
		assertEquals("library not found.", library, dao.findByName(library
				.getName()));
		assertEquals("library name is not case insensitive.", library, dao
				.findByName(library.getName().toUpperCase()));
	}

	/**
	 * Test to see if hibernate second level cache is working
	 */
	@Ignore("already tested")
	@Test
	public void cacheTest() throws Exception {
		// prepair test by storing 500 libraries in database
		System.out.print("Prepairing Library cache test...");
		EntityManagerUtil.currentEntityManager();
		for (int i = 0; i < 500; i++) {
			String name = "name" + (i + 1);
			Library p = new Library(name);
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
			List<Library> libraries = dao.getAll();
			EntityManagerUtil.closeEntityManager();
			long end = System.currentTimeMillis();
			System.out.println("LibraryDAO.getAll() - query cache test: "
					+ (end - start) + "ms");
			assertNotNull("libraries are null", libraries);
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
		System.out.print("Prepairing Library cache collection test...");
		for (int i = 0; i < 500; i++) {
			String name = "name" + (i + 1);
			new LibraryFile(library, name, FileTypes.VHDL_SOURCE, "abcdef");
		}
		EntityManagerUtil.currentEntityManager();
		dao.save(library);
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
			Library p = dao.load(library.getId());
			for (LibraryFile f : p) {
				assertNotNull("file id is null.", f.getId());
				assertNotNull("file name is null.", f.getName());
			}
			EntityManagerUtil.closeEntityManager();
			long end = System.currentTimeMillis();
			System.out.println("Library.files - cache test: " + (end - start)
					+ "ms");
			assertNotNull("library is null", p);
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
		System.out.print("Prepairing Library cache collection test...");
		for (int i = 0; i < 500; i++) {
			String name = "name" + (i + 1);
			new LibraryFile(library, name, FileTypes.VHDL_SOURCE, "abcdef");
		}
		EntityManagerUtil.currentEntityManager();
		dao.save(library);
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
			Library p = dao.load(library.getId());
			EntityManagerUtil.closeEntityManager();
			long end = System.currentTimeMillis();
			System.out.println("Library.files - cache test: " + (end - start)
					+ "ms");
			assertNotNull("library is null", p);
		}
		/*
		 * Pause so user can view statistics in jconsole
		 */
		Thread.sleep(Long.MAX_VALUE);
	}

}
