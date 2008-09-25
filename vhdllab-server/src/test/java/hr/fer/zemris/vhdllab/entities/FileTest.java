package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link File} entity.
 * 
 * @author Miro Bezjak
 */
public class FileTest {

    private File file;

    @Before
    public void initEachTest() throws Exception {
        file = StubFactory.create(File.class, 1);
    }

    /**
     * Test that properties are correctly set in constructor.
     */
    @Test
    public void constructor() throws Exception {
        Caseless name = StubFactory.getStubValue("name", 1);
        FileType type = StubFactory.getStubValue("type", 1);
        String data = StubFactory.getStubValue("data", 1);
        assertEquals("names not same.", name, file.getName());
        assertEquals("types not same.", type, file.getType());
        assertEquals("data not same.", data, file.getData());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor() throws Exception {
        Project project = StubFactory.create(Project.class, 1);
        project.addFile(file);
        File another = new File(file);
        assertTrue("same reference.", file != another);
        assertFalse("equal.", file.equals(another));
        assertNotSame("hashCode same.", file.hashCode(), another.hashCode());
        assertNull("project reference copied.", another.getProject());
        assertTrue("original file's project is modified.", project == file
                .getProject());
        assertFalse("original file's project's files is modified.", project
                .getFiles().contains(another));
    }

    /**
     * Post remove project reference is only set once
     * {@link Project#removeFile(File)} method has been called and {@link File}
     * retains that reference until File#getPostRemoveProjectReference() has
     * been called.
     */
    @Test
    public void getPostRemoveProjectReference() throws Exception {
        assertNull("not null.", file.getPostRemoveProjectReference());
        file.setProject(null);
        assertNull("not null.", file.getProject());
        assertNull("not null.", file.getPostRemoveProjectReference());
        Project project = StubFactory.create(Project.class, 1);
        project.addFile(file);
        assertNull("not null.", file.getPostRemoveProjectReference());
        project.removeFile(file);
        assertTrue("not same reference.", project == file
                .getPostRemoveProjectReference());
        assertNull("reference was not self-destructed.", file
                .getPostRemoveProjectReference());
    }

    /**
     * Test equals with self, null, and non-File.
     */
    @Test
    public void equals() throws Exception {
        assertEquals("not equal.", file, file);
        assertFalse("file is equal to null.", file.equals(null));
        assertFalse("can compare with string object.", file
                .equals("string object"));
    }

    /**
     * Only projects and names are important in equals and hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        Project project = StubFactory.create(Project.class, 1);
        Project anotherProject = new Project(project);
        File anotherFile = StubFactory.create(File.class, 2);
        project.addFile(file);
        anotherProject.addFile(anotherFile);
        StubFactory.setProperty(anotherFile, "name", 1);
        assertEquals("not equal.", file, anotherFile);
        assertEquals("hashCode not same.", file.hashCode(), anotherFile
                .hashCode());
    }

    /**
     * Projects are different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        Project project = StubFactory.create(Project.class, 1);
        Project anotherProject = StubFactory.create(Project.class, 2);
        File anotherFile = new File(file);
        project.addFile(file);
        anotherProject.addFile(anotherFile);
        assertFalse("equal.", file.equals(anotherFile));
        assertNotSame("same hashCode.", file.hashCode(), anotherFile.hashCode());
    }

    /**
     * Projects are different (one is null).
     */
    @Test
    public void equalsAndHashCode3() throws Exception {
        Project project = StubFactory.create(Project.class, 1);
        File anotherFile = new File(file);
        project.addFile(file);
        assertFalse("equal.", file.equals(anotherFile));
        assertNotSame("same hashCode.", file.hashCode(), anotherFile.hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode4() throws Exception {
        Project project = StubFactory.create(Project.class, 1);
        Project anotherProject = new Project(project);
        File anotherFile = StubFactory.create(File.class, 1);
        project.addFile(file);
        anotherProject.addFile(anotherFile);
        StubFactory.setProperty(anotherFile, "name", 2);
        assertFalse("equal.", file.equals(anotherFile));
        assertNotSame("same hashCode.", file.hashCode(), anotherFile.hashCode());
    }

    /**
     * Entities are same after deserialization.
     */
    @Test
    public void serialization() throws Exception {
        Object deserialized = SerializationUtils.clone(file);
        assertEquals("not same.", file, deserialized);
    }

    @Test
    public void asString() {
        System.out.println(file);
    }

    @Test
    public void asStringInProject() throws Exception {
        Project project = StubFactory.create(Project.class, 1);
        project.addFile(file);
        System.out.println(file);
    }

}
