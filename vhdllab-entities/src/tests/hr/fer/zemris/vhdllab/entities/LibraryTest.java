package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.EntitiesUtil.injectValueToPrivateField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	static {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm");
		try {
			CREATED = df.parse("2008-01-02 13-45");
		} catch (ParseException e) {
			// should never happen. but if pattern should change report it by
			// throwing exception.
			throw new IllegalStateException(e);
		}
	}

	private Library lib;
	private Library lib2;
	private LibraryFile file;

	@Before
	public void initEachTest() throws Exception {
		lib = new Library(NAME);
		injectValueToPrivateField(lib, "id", ID);
		injectValueToPrivateField(lib, "created", CREATED);
		lib2 = new Library(lib);

		file = new LibraryFile(lib, "file.name", "file.type", "file.content");
		new LibraryFile(file, lib2);
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor() throws Exception {
		assertTrue("same reference.", lib != lib2);
		assertEquals("not equal.", lib, lib2);
		assertEquals("hashCode not same.", lib.hashCode(), lib2.hashCode());
		assertEquals("not equal by compareTo.", 0, lib.compareTo(lib2));
		assertEquals("files not same.", lib.getChildren(), lib2.getChildren());
	}

	/**
	 * Test if returned set is modifiable
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void getLibraryFiles() throws Exception {
		Set<LibraryFile> files = lib.getLibraryFiles();
		Library newLibrary = new Library("new.library.name");
		LibraryFile newFile = new LibraryFile(newLibrary, "new.file.name",
				"new.file.type");
		files.add(newFile);
	}

	/**
	 * Test equals with self, null, and non-library object
	 */
	@Test
	public void equals() throws Exception {
		assertEquals("not equal.", lib, lib);
		assertNotSame("file is equal to null.", lib, null);
		assertNotSame("can compare with string object.", lib, "a string object");
		assertNotSame("can compare with resource object.", lib, new Resource());
	}

	/**
	 * Null object as parameter to compareTo method
	 */
	@Test(expected = NullPointerException.class)
	public void compareTo() throws Exception {
		lib.compareTo(null);
	}

	/**
	 * Non-library type
	 */
	@Test(expected = ClassCastException.class)
	public void compareTo2() throws Exception {
		lib.compareTo(new Container<LibraryFile, Library>());
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(lib.toString());
	}

}
