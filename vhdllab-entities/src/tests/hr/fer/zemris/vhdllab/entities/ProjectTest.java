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
 * A test case for {@link Project} entity.
 * 
 * @author Miro Bezjak
 */
public class ProjectTest {

	private static final Long ID = Long.valueOf(123456);
	private static final String NAME = "project.name";
	private static final String USER_ID = "user.identifier";
	private static final Date CREATED;
	private static final Long NEW_ID = Long.valueOf(654321);
	private static final String NEW_NAME = "new.project.name";
	private static final String NEW_USER_ID = "new.user.identifier";
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

	private Project project;
	private Project project2;
	private File file;
	private File file2;
	private Set<File> files;

	@Before
	public void initEachTest() {
		project = new Project();
		project.setId(ID);
		project.setName(NAME);
		project.setUserId(USER_ID);
		project.setCreated(CREATED);
		project2 = new Project(project);

		file = new File();
		file.setId(Long.valueOf(10));
		file.setName("file1.name");
		file.setType("file1.type");
		file.setContent("file1.content");
		file.setCreated(Calendar.getInstance().getTime());
		file2 = new File(); // not added immediately to library
		file2.setId(Long.valueOf(20));
		file2.setName("file2.name");
		file2.setType("file2.type");
		file2.setContent("file2.content");
		file2.setCreated(Calendar.getInstance().getTime());
		File fileDuplicate = new File(file);

		project.addFile(file);
		project2.addFile(fileDuplicate);
		files = new HashSet<File>();
		files.add(file);
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor() {
		assertTrue("same reference.", project != project2);
		assertEquals("not equal.", project, project2);
		assertEquals("hashCode not same.", project.hashCode(), project2
				.hashCode());
		assertEquals("not equal by compareTo.", 0, project.compareTo(project2));
		assertEquals("files not same.", project.getFiles(), project2.getFiles());
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() {
		/*
		 * Setters are tested indirectly. @Before method uses setters.
		 */
		assertEquals("getFiles.", files, project.getFiles());
	}

	/**
	 * Test equals with self, null, and non-project object
	 */
	@Test
	public void equals() {
		assertEquals("not equal.", project, project);
		assertNotSame("file is equal to null.", project, null);
		assertNotSame("can compare with string object.", project,
				"a string object");
		assertNotSame("can compare with resource object.", project,
				new Resource());
	}

	/**
	 * Null object as parameter to compareTo method
	 */
	@Test(expected = NullPointerException.class)
	public void compareTo() {
		project.compareTo(null);
	}

	/**
	 * Non-project type
	 */
	@Test(expected = ClassCastException.class)
	public void compareTo2() {
		project.compareTo(new Container<File, Project>());
	}

	/**
	 * Only ids (if set) are important in equals, hashCode and compareTo
	 */
	@Test
	public void equalsHashCodeAndCompareTo() {
		project2.setUserId(NEW_USER_ID);
		project2.setName(NEW_NAME);
		project2.setCreated(NEW_CREATED);
		project2.setFiles(new HashSet<File>(0));
		assertEquals("not equal.", project, project2);
		assertEquals("hashCode not same.", project.hashCode(), project2
				.hashCode());
		assertEquals("not equal by compareTo.", 0, project.compareTo(project2));
	}

	/**
	 * Ids are null, then name and userId is important
	 */
	@Test
	public void equalsHashCodeAndCompareTo2() {
		project.setId(null);
		project2.setId(null);
		project2.setCreated(NEW_CREATED);
		project2.setFiles(new HashSet<File>(0));
		assertEquals("not equal.", project, project2);
		assertEquals("hashCode not same.", project.hashCode(), project2
				.hashCode());
		assertEquals("not equal by compareTo.", 0, project.compareTo(project2));
	}

	/**
	 * User id is case insensitive
	 */
	@Test
	public void equalsHashCodeAndCompareTo3() {
		project.setId(null);
		project2.setId(null);
		project2.setUserId(USER_ID.toUpperCase());
		assertEquals("not equal.", project, project2);
		assertEquals("hashCode not same.", project.hashCode(), project2
				.hashCode());
		assertEquals("not equal by compareTo.", 0, project.compareTo(project2));
	}

	/**
	 * Ids are null and user ids are not equal
	 */
	@Test
	public void equalsHashCodeAndCompareTo4() {
		project.setId(null);
		project2.setId(null);
		project2.setUserId(NEW_USER_ID);
		assertNotSame("equal.", project, project2);
		assertNotSame("hashCode same.", project.hashCode(), project2.hashCode());
		assertEquals("not compared by user id.",
				USER_ID.compareTo(NEW_USER_ID) < 0 ? -1 : 1, project
						.compareTo(project2));
	}

	/**
	 * Ids and names are null, then user id is important
	 */
	@Test
	public void equalsHashCodeAndCompareTo5() {
		project.setId(null);
		project2.setId(null);
		project.setName(null);
		project2.setName(null);
		assertEquals("not equal.", project, project2);
		assertEquals("hashCode not same.", project.hashCode(), project2
				.hashCode());
		assertEquals("not equal by compareTo.", 0, project.compareTo(project2));
	}

	/**
	 * Ids, names and user ids are null
	 */
	@Test
	public void equalsHashCodeAndCompareTo6() {
		project.setId(null);
		project2.setId(null);
		project.setName(null);
		project2.setName(null);
		project.setUserId(null);
		project2.setUserId(null);
		assertEquals("not equal.", project, project2);
		assertEquals("hashCode not same.", project.hashCode(), project2
				.hashCode());
		assertEquals("not equal by compareTo.", 0, project.compareTo(project2));
	}

	/**
	 * File is null
	 */
	@Test(expected = NullPointerException.class)
	public void addFile() {
		project.addFile(null);
	}

	/**
	 * Add a file
	 */
	@Test
	public void addFile2() {
		project.addFile(file2);
		files.add(file2);
		assertTrue("file not added.", project.getFiles().contains(file2));
		assertEquals("files not same.", files, project.getFiles());
		assertEquals("project not set.", project, file2.getProject());
	}

	/**
	 * Add a file that is already in another project
	 */
	@Test
	public void addFile3() {
		Project newProject = new Project();
		newProject.setId(NEW_ID);
		newProject.setName(NEW_NAME);
		newProject.setCreated(NEW_CREATED);
		Project previousProject = file.getProject();
		newProject.addFile(file);
		assertEquals("file not added.", files, newProject.getFiles());
		assertEquals("library not reset.", newProject, file.getProject());
		assertFalse("previous project still contains resource.",
				previousProject.getFiles().contains(file));
	}

	/**
	 * Add a file that is already in that project
	 */
	@Test
	public void addFile4() {
		project.addFile(file);
		assertEquals("files not same.", files, project.getFiles());
		assertEquals("files is changed.", 1, project.getFiles().size());
		assertEquals("project is reset.", project, file.getProject());
	}

	/**
	 * File is null
	 */
	@Test(expected = NullPointerException.class)
	public void removeFile() {
		project.removeFile(null);
	}

	/**
	 * Remove a file
	 */
	@Test
	public void removeFile2() {
		project.removeFile(file);
		assertEquals("file not empty.", new HashSet<LibraryFile>(0), project
				.getFiles());
		assertNull("library is not set to null.", file.getProject());
	}

	/**
	 * Remove a file that does not exists in a project
	 */
	@Test
	public void removeFile3() {
		project2.getFiles().clear();
		project2.addFile(file2);
		project.removeFile(file2);
		assertEquals("project is changed.", files, project.getFiles());
		assertEquals("project is reset.", project2, file2.getProject());
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(project);
	}

}
