package hr.fer.zemris.vhdllab.dao.impl;

import static hr.fer.zemris.vhdllab.dao.impl.StringGenerationUtil.generateJunkString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.GlobalFileDAO;
import hr.fer.zemris.vhdllab.entities.GlobalFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for {@link GlobalFileDAOImpl}.
 * 
 * @author Miro Bezjak
 */
public class GlobalFileDAOImplTest {

	private static final String NAME = "simple.file.name";
	private static final Long UNUSED_ID = Long.valueOf(Long.MAX_VALUE);
	private static final String UNUSED_NAME = "unused.name";
	private static final int MAX_NAME_LENGTH = 255;
	private static final int MAX_CONTENT_LENGTH = 65535;

	private static GlobalFileDAO dao;
	private GlobalFile file;

	@BeforeClass
	public static void initTestCase() throws DAOException {
		dao = new GlobalFileDAOImpl();
		EntityManagerUtil.createEntityManagerFactory();
		destroyFiles();
	}

	@Before
	public void initEachTest() throws DAOException {
		initFiles();
		EntityManagerUtil.currentEntityManager();
	}

	private void initFiles() throws DAOException {
		file = new GlobalFile();
		file.setName(NAME);
		file.setContent("<pref><value>schematic</value></pref>");
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
		for (GlobalFile f : dao.getAll()) {
			dao.delete(f.getId());
		}
		EntityManagerUtil.closeEntityManager();
	}

	/**
	 * save a file then load it and see it they are the same
	 */
	@Test
	public void saveAndLoad() throws DAOException {
		dao.save(file);
		GlobalFile loadedFile = dao.load(file.getId());
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
		assertEquals(false, dao.exists(file.getName()));
	}

	/**
	 * name is null
	 */
	@Test(expected = NullPointerException.class)
	public void exists() throws DAOException {
		dao.exists((String) null);
	}

	/**
	 * non-existing name
	 */
	@Test
	public void exists2() throws DAOException {
		assertEquals(false, dao.exists(UNUSED_NAME));
	}

	/**
	 * everything ok
	 */
	@Test
	public void exists3() throws DAOException {
		dao.save(file);
		assertEquals(true, dao.exists(file.getId()));
		assertEquals(true, dao.exists(file.getName()));
		assertEquals(true, dao.exists(file.getName().toUpperCase()));
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
	 * name is unique
	 */
	@Test(expected = DAOException.class)
	public void save5() throws DAOException {
		dao.save(file);
		GlobalFile clone = new GlobalFile(file);
		clone.setId(null);
		clone.setContent("abc");
		dao.save(clone);
	}

	/**
	 * name is of max length
	 */
	@Test()
	public void save6() throws DAOException {
		file.setName(generateJunkString(MAX_NAME_LENGTH));
		dao.save(file);
	}

	/**
	 * name is too long
	 */
	@Test(expected = DAOException.class)
	public void save7() throws DAOException {
		file.setName(generateJunkString(MAX_NAME_LENGTH + 1));
		dao.save(file);
	}

	/**
	 * content is null
	 */
	@Test
	public void save8() throws DAOException {
		file.setContent(null);
		dao.save(file);
	}

	/**
	 * content is of max length
	 */
	@Test()
	public void save9() throws DAOException {
		file.setContent(generateJunkString(MAX_CONTENT_LENGTH));
		dao.save(file);
	}

	/**
	 * content is too long
	 */
	@Test(expected = DAOException.class)
	public void save10() throws DAOException {
		file.setContent(generateJunkString(MAX_CONTENT_LENGTH + 1));
		dao.save(file);
	}

	/**
	 * save a file then update it
	 */
	@Test
	public void save11() throws DAOException {
		dao.save(file);
		file.setName(UNUSED_NAME);
		file.setContent("abc");
		dao.save(file);
		assertEquals(true, dao.exists(file.getId()));
		assertEquals(file, dao.load(file.getId()));
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
	@Test(expected = DAOException.class)
	public void findByName2() throws DAOException {
		dao.findByName(UNUSED_NAME);
	}

	/**
	 * everything ok
	 */
	@Test
	public void findByName3() throws DAOException {
		dao.save(file);
		assertEquals(file, dao.findByName(file.getName()));
		assertEquals(file, dao.findByName(file.getName().toUpperCase()));
	}

	/**
	 * database is empty
	 */
	@Test
	public void getAll() throws DAOException {
		assertEquals(Collections.emptyList(), dao.getAll());
	}

	/**
	 * save two files then load them all with getAll
	 */
	@Test
	public void getAll2() throws DAOException {
		GlobalFile file2 = new GlobalFile();
		file2.setName("a new global file");
		file2.setContent("content for global file 2");
		dao.save(file);
		dao.save(file2);

		List<GlobalFile> expectedFiles = new ArrayList<GlobalFile>(2);
		expectedFiles.add(file);
		expectedFiles.add(file2);
		List<GlobalFile> all = dao.getAll();

		assertEquals(expectedFiles, all);
	}

	/**
	 * Test to see if hibernate second level cache is working
	 */
	@Ignore("already tested")
	@Test
	public void cacheTest() throws Exception {
		// prepair test by storing 500 files in database
		System.out.print("Prepairing GlobalFile cache test...");
		EntityManagerUtil.currentEntityManager();
		for (int i = 0; i < 500; i++) {
			GlobalFile f = new GlobalFile();
			f.setName("name" + (i + 1));
			f.setContent("abcdef");
			dao.save(f);
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
		 * One reason for this is because above test preparation method
		 * indirectly populates caches. On the other hand by simply viewing
		 * statistics of persistence provider user can be sure that caches are
		 * working. jconsole tool will help with that.
		 */
		for (int i = 0; i < 5; i++) {
			EntityManagerUtil.currentEntityManager();
			long start = System.currentTimeMillis();
			List<GlobalFile> files = dao.getAll();
			EntityManagerUtil.closeEntityManager();
			long end = System.currentTimeMillis();
			System.out.println("GlobalFile.getAll() - cache test: "
					+ (end - start) + "ms");
			assertNotSame(null, files);
		}
		/*
		 * Pause so user can view statistics in jconsole
		 */
		Thread.sleep(Long.MAX_VALUE);
	}

}
