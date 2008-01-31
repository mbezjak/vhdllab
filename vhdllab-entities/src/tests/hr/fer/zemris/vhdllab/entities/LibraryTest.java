package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link Library} class.
 * 
 * @author Miro Bezjak
 */
public class LibraryTest {

	private static final Long ID = Long.valueOf(123456);
	private static final String NAME = "library.name";
	private static final Date CREATED;
	private static final Long NEW_ID = Long.valueOf(654321);
	private static final String NEW_NAME = "new." + NAME;
	private static final Date NEW_CREATED;

	static {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm");
		try {
			CREATED = df.parse("2008-01-02 13-45");
			NEW_CREATED = df.parse("2000-12-31 07-13");
		} catch (ParseException e) {
			// should never happen. but if pattern should change report it by
			// throwing exception.
			throw new IllegalStateException(e);
		}
	}

	private Library lib;
	private Library lib2;
	private LibraryFile file;
	private LibraryFile file2;
	private Set<LibraryFile> files;

	@Before
	public void initEachTest() {
		lib = new Library();
		lib.setId(ID);
		lib.setName(NAME);
		lib.setCreated(CREATED);
		lib2 = new Library(lib);

		file = new LibraryFile();
		file.setId(Long.valueOf(10));
		file.setName("file1.name");
		file.setType("file1.type");
		file.setContent("file1.content");
		file.setCreated(Calendar.getInstance().getTime());
		file2 = new LibraryFile(); // not added immediately to library
		file2.setId(Long.valueOf(20));
		file2.setName("file2.name");
		file2.setType("file2.type");
		file2.setContent("file2.content");
		file2.setCreated(Calendar.getInstance().getTime());
		LibraryFile fileDuplicate = new LibraryFile(file);

		lib.addLibraryFile(file);
		lib2.addLibraryFile(fileDuplicate);
		files = new HashSet<LibraryFile>();
		files.add(file);
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor() {
		assertTrue("same reference.", lib != lib2);
		assertEquals("not equal.", lib, lib2);
		assertEquals("hashCode not same.", lib.hashCode(), lib2.hashCode());
		assertEquals("not equal by compareTo.", 0, lib.compareTo(lib2));
		assertEquals("files not same.", lib.getLibraryFiles(), lib2
				.getLibraryFiles());
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() {
		/*
		 * Setters are tested indirectly. @Before method uses setters.
		 */
		assertEquals("getLibraryFiles.", files, lib.getLibraryFiles());
	}
	
	/**
	 * Test equals with self, null, and non-library object
	 */
	@Test
	public void equals() {
		assertEquals("not equal.", lib, lib);
		assertNotSame("file is equal to null.", lib, null);
		assertNotSame("can compare with string object.", lib, "a string object");
		assertNotSame("can compare with resource object.", lib, new Resource());
	}

	/**
	 * Null object as parameter to compareTo method
	 */
	@Test(expected = NullPointerException.class)
	public void compareTo() {
		lib.compareTo(null);
	}

	/**
	 * Non-library type
	 */
	@Test(expected = ClassCastException.class)
	public void compareTo2() {
		lib.compareTo(new Container<LibraryFile, Library>());
	}

	/**
	 * Library file is null
	 */
	@Test(expected = NullPointerException.class)
	public void addLibraryFile() {
		lib.addLibraryFile(null);
	}

	/**
	 * Add a library file
	 */
	@Test
	public void addLibraryFile2() {
		lib.addLibraryFile(file2);
		files.add(file2);
		assertTrue("file not added.", lib.getLibraryFiles().contains(file2));
		assertEquals("files not same.", files, lib.getLibraryFiles());
		assertEquals("library not set.", lib, file2.getLibrary());
	}

	/**
	 * Add a library file that is already in another library
	 */
	@Test
	public void addLibraryFile3() {
		Library newLibrary = new Library();
		newLibrary.setId(NEW_ID);
		newLibrary.setName(NEW_NAME);
		newLibrary.setCreated(NEW_CREATED);
		Library previousLibrary = file.getLibrary();
		newLibrary.addLibraryFile(file);
		assertEquals("file not added.", files, newLibrary.getLibraryFiles());
		assertEquals("library not reset.", newLibrary, file.getLibrary());
		assertFalse("previous library still contains resource.",
				previousLibrary.getLibraryFiles().contains(file));
	}

	/**
	 * Add a library file that is already in that library
	 */
	@Test
	public void addLibraryFile4() {
		lib.addLibraryFile(file);
		assertEquals("files not same.", files, lib.getLibraryFiles());
		assertEquals("files is changed.", 1, lib.getLibraryFiles().size());
		assertEquals("library is reset.", lib, file.getLibrary());
	}

	/**
	 * Library file is null
	 */
	@Test(expected = NullPointerException.class)
	public void removeLibraryFile() {
		lib.removeLibraryFile(null);
	}

	/**
	 * Remove a library file
	 */
	@Test
	public void removeLibraryFile2() {
		lib.removeLibraryFile(file);
		assertEquals("file not empty.", new HashSet<LibraryFile>(0), lib
				.getLibraryFiles());
		assertNull("library is not set to null.", file.getLibrary());
	}

	/**
	 * Remove a library file that does not exists in a library
	 */
	@Test
	public void removeLibraryFile3() {
		lib2.getLibraryFiles().clear();
		lib2.addLibraryFile(file2);
		lib.removeLibraryFile(file2);
		assertEquals("library is changed.", files, lib.getLibraryFiles());
		assertEquals("library is reset.", lib2, file2.getLibrary());
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(lib.toString());
	}

}
