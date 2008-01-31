package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link LibraryFile} class.
 * 
 * @author Miro Bezjak
 */
public class LibraryFileTest {

	private static final Long ID = Long.valueOf(123456);
	private static final String NAME = "file.name";
	private static final String TYPE = "file.type";
	private static final String CONTENT = "...file content...";
	private static final Date CREATED;
	private static final String NEW_NAME = "new." + NAME;
	private static final String NEW_TYPE = "new." + TYPE;
	private static final String NEW_CONTENT = "new." + CONTENT;
	private static final Date NEW_CREATED;
	private static final Library NEW_LIBRARY;

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
		NEW_LIBRARY = new Library();
		NEW_LIBRARY.setId(Long.valueOf(5555));
		NEW_LIBRARY.setName("new.library.name");
	}

	private Library lib;
	private Library lib2;
	private LibraryFile file;
	private LibraryFile file2;

	@Before
	public void initEachTest() {
		file = new LibraryFile();
		file.setId(ID);
		file.setName(NAME);
		file.setType(TYPE);
		file.setContent(CONTENT);
		file.setCreated(CREATED);
		file2 = new LibraryFile(file);

		lib = new Library();
		lib.setId(Long.valueOf(10));
		lib.setName("library1.name");
		lib.setCreated(Calendar.getInstance().getTime());
		lib2 = new Library(lib);

		lib.addLibraryFile(file);
		lib2.addLibraryFile(file2);
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor() {
		assertTrue("same reference.", file != file2);
		assertEquals("not equal.", file, file2);
		assertEquals("hashCode not same.", file.hashCode(), file2.hashCode());
		assertEquals("not equal by compareTo.", 0, file.compareTo(file2));
	}

	/**
	 * Test references to library
	 */
	@Test
	public void copyConstructor2() {
		file2 = new LibraryFile(file);
		assertNull("reference to library is copied.", file2.getLibrary());
	}

	/**
	 * Test equals with self, null, and non-library-file object
	 */
	@Test
	public void equals() {
		assertEquals("not equal.", file, file);
		assertNotSame("file is equal to null.", file, null);
		assertNotSame("can compare with string object.", file,
				"a string object");
		assertNotSame("can compare with resource object.", file, new Resource());
	}

	/**
	 * Null object as parameter to compareTo method
	 */
	@Test(expected = NullPointerException.class)
	public void compareTo() {
		file.compareTo(null);
	}

	/**
	 * Non-library-file type
	 */
	@Test(expected = ClassCastException.class)
	public void compareTo2() {
		file.compareTo(new Resource());
	}

	/**
	 * Only ids (if set) are important in equals, hashCode and compareTo
	 */
	@Test
	public void equalsHashCodeAndCompareTo() {
		file2.setName(NEW_NAME);
		file2.setType(NEW_TYPE);
		file2.setContent(NEW_CONTENT);
		file2.setCreated(NEW_CREATED);
		file2.setLibrary(NEW_LIBRARY);
		assertEquals("not equal.", file, file2);
		assertEquals("hashCode not same.", file.hashCode(), file2.hashCode());
		assertEquals("not equal by compareTo.", 0, file.compareTo(file2));
	}

	/**
	 * Ids are null, then name and library is important
	 */
	@Test
	public void equalsHashCodeAndCompareTo2() {
		file.setId(null);
		file2.setId(null);
		file2.setType(NEW_TYPE);
		file2.setContent(NEW_CONTENT);
		file2.setCreated(NEW_CREATED);
		assertEquals("not equal.", file, file2);
		assertEquals("hashCode not same.", file.hashCode(), file2.hashCode());
		assertEquals("not equal by compareTo.", 0, file.compareTo(file2));
	}

	/**
	 * Ids are null and libraries are not equal
	 */
	@Test
	public void equalsHashCodeAndCompareTo3() {
		file.setId(null);
		file2.setId(null);
		file2.setLibrary(NEW_LIBRARY);
		assertNotSame("equal.", file, file2);
		assertNotSame("hashCode same.", file.hashCode(), file2.hashCode());
		assertEquals("not compared by library.", file.getLibrary().compareTo(
				file2.getLibrary()) < 0 ? -1 : 1, file.compareTo(file2));
	}

	/**
	 * Ids and names are null, then library is important
	 */
	@Test
	public void equalsHashCodeAndCompareTo4() {
		file.setId(null);
		file2.setId(null);
		file.setName(null);
		file2.setName(null);
		assertEquals("not equal.", file, file2);
		assertEquals("hashCode not same.", file.hashCode(), file2.hashCode());
		assertEquals("not equal by compareTo.", 0, file.compareTo(file2));
	}

	/**
	 * Ids, names and libraries are null
	 */
	@Test
	public void equalsHashCodeAndCompareTo5() {
		file.setId(null);
		file2.setId(null);
		file.setName(null);
		file2.setName(null);
		file.setLibrary(null);
		file2.setLibrary(null);
		assertEquals("not equal.", file, file2);
		assertEquals("hashCode not same.", file.hashCode(), file2.hashCode());
		assertEquals("not equal by compareTo.", 0, file.compareTo(file2));
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(file.toString());
	}

}
