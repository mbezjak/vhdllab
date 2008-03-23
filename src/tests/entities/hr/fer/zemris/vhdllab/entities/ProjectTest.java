package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

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
	private static final Long NEW_ID = Long.valueOf(654321);
	private static final String NEW_NAME = "new.project.name";
	private static final String NEW_USER_ID = "new.user.identifier";
	
	private Project project;
	private Project project2;
	private File file;
	private File file2;
	private Set<File> files;
	
	@Before
	public void initEachTest() {
		file = new File();
		file.setId(Long.valueOf(987));
		file.setName("file.name");
		file.setType("file.type");
		file.setContent("file.content");
		project = new Project();
		project.setId(ID);
		project.setName(NAME);
		project.setUserId(USER_ID);
		project.addFile(new File(file));
		project2 = new Project(project);
		project.addFile(file);
		files = new HashSet<File>(2);
		files.add(file);
		file2 = new File();
		file2.setId(Long.valueOf(778899));
		file2.setName("new.file.name");
		file2.setType("new.file.type");
		file2.setContent("new.file.content");
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor() {
		assertTrue(project != project2);
		assertEquals(project, project2);
		assertEquals(project.hashCode(), project2.hashCode());
		assertEquals(project.getFiles(), project2.getFiles());
	}
	
	/**
	 * Projects' files is null
	 */
	@Test
	public void copyConstructor2() {
		project.setFiles(null);
		project2 = new Project(project);
		assertTrue(project != project2);
		assertEquals(project, project2);
		assertEquals(project.hashCode(), project.hashCode());
	}
	
	/**
	 * Projects' files is empty
	 */
	@Test
	public void copyConstructor3() {
		project.setFiles(new HashSet<File>(0));
		project2 = new Project(project);
		assertTrue(project != project2);
		assertEquals(project, project2);
		assertEquals(project.hashCode(), project.hashCode());
	}
	
	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() {
		assertEquals(ID, project.getId());
		assertEquals(NAME, project.getName());
		assertEquals(USER_ID, project.getUserId());
		assertEquals(files, project.getFiles());
	}
	
	/**
	 * Test equals with self, null, and non-project object
	 */
	@Test
	public void equals() {
		assertEquals(project, project);
		assertNotSame(project, null);
		assertNotSame(project, NAME);
	}
	
	/**
	 * Only ids (if set) are important in equals, hashcode and compareTo
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo() {
		project2.setName(NEW_NAME);
		project2.setUserId(NEW_USER_ID);
		files.clear();
		files.add(file2);
		project2.addFile(file2);
		assertEquals(project, project2);
		assertEquals(project.hashCode(), project.hashCode());
		assertEquals(0, project.compareTo(project2));
	}
	
	/**
	 * Ids are different
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo2() {
		project2.setId(NEW_ID);
		assertNotSame(project, project2);
		assertNotSame(project.hashCode(), project2.hashCode());
		assertNotSame(0, project.compareTo(project2));
	}
	
	/**
	 * Ids are null, then user id and name is important
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo3() {
		project.setId(null);
		project2.setId(null);
		assertEquals(project, project2);
		assertEquals(project.hashCode(), project2.hashCode());
		assertEquals(0, project.compareTo(project2));
	}
	
	/**
	 * Project name is case insensitive
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo4() {
		project.setId(null);
		project2.setId(null);
		project2.setName(NAME.toUpperCase());
		assertEquals(project, project2);
		assertEquals(project.hashCode(), project2.hashCode());
		assertEquals(0, project.compareTo(project2));
	}
	
	/**
	 * User id is case insensitive
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo5() {
		project.setId(null);
		project2.setId(null);
		project2.setUserId(USER_ID.toUpperCase());
		assertEquals(project, project2);
		assertEquals(project.hashCode(), project2.hashCode());
		assertEquals(0, project.compareTo(project2));
	}
	
	/**
	 * Ids are null and names are not equal
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo6() {
		project.setId(null);
		project2.setId(null);
		project2.setName(NEW_NAME);
		assertNotSame(project, project2);
		assertNotSame(project.hashCode(), project2.hashCode());
		assertNotSame(0, project.compareTo(project2));
	}
	
	/**
	 * Ids are null and user ids are not equal
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo7() {
		project.setId(null);
		project.setId(null);
		project.setUserId(NEW_USER_ID);
		assertNotSame(project, project2);
		assertNotSame(project.hashCode(), project2.hashCode());
		assertNotSame(0, project.compareTo(project2));
	}
	
	/**
	 * Ids and names are null, then user id is important
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo8() {
		project.setId(null);
		project2.setId(null);
		project.setName(null);
		project2.setName(null);
		assertEquals(project, project2);
		assertEquals(project.hashCode(), project2.hashCode());
		assertEquals(0, project.compareTo(project2));
	}
	
	/**
	 * Ids and user ids are null then name is important
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo9() {
		project.setId(null);
		project2.setId(null);
		project.setUserId(null);
		project2.setUserId(null);
		assertEquals(project, project2);
		assertEquals(project.hashCode(), project2.hashCode());
		assertEquals(0, project.compareTo(project2));
	}
	
	/**
	 * Ids, user ids and names are null
	 */
	@Test
	public void equalsAndHashCodeAndCompareTo10() {
		project.setId(null);
		project2.setId(null);
		project.setName(null);
		project2.setName(null);
		project.setUserId(null);
		project2.setUserId(null);
		assertEquals(project, project2);
		assertEquals(project.hashCode(), project2.hashCode());
		assertEquals(0, project.compareTo(project2));
	}
	
	/**
	 * File is null
	 */
	@Test(expected=NullPointerException.class)
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
		assertTrue(project.getFiles().contains(file2));
		assertEquals(files, project.getFiles());
		assertEquals(project, file2.getProject());
	}
	
	/**
	 * Add a file that is already in another project
	 */
	@Test
	public void addFile3() {
		Project newProject = new Project();
		newProject.setId(NEW_ID);
		newProject.setName(NEW_NAME);
		newProject.setUserId(NEW_USER_ID);
		Project previousProject = file.getProject();
		newProject.addFile(file);
		assertEquals(files, newProject.getFiles());
		assertEquals(newProject, file.getProject());
		assertEquals(false, previousProject.getFiles().contains(file));
	}
	
	/**
	 * Add a file that is already in that project
	 */
	@Test
	public void addFile4() {
		project.addFile(file);
		assertEquals(files, project.getFiles());
		assertEquals(1, project.getFiles().size());
		assertEquals(project, file.getProject());
	}
	
	/**
	 * File is null
	 */
	@Test(expected=NullPointerException.class)
	public void removeFile() {
		project.removeFile(null);
	}
	
	/**
	 * Remove a file
	 */
	@Test
	public void removeFile2() {
		project.removeFile(file);
		assertEquals(new HashSet<File>(0), project.getFiles());
		assertEquals(null, file.getProject());
	}
	
	/**
	 * Remove a file that does not exists in a project
	 */
	@Test
	public void removeFile3() {
		project.removeFile(file2);
		assertEquals(files, project.getFiles());
		assertEquals(null, file2.getProject());
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(project);
	}

}
