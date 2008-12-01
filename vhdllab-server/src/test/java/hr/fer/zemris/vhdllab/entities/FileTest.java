package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_2;
import static hr.fer.zemris.vhdllab.entities.stub.IFileResourceStub.TYPE;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.FileStub;
import hr.fer.zemris.vhdllab.entities.stub.FileStub2;
import hr.fer.zemris.vhdllab.entities.stub.ProjectStub;
import hr.fer.zemris.vhdllab.entities.stub.ProjectStub2;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link File} entity.
 * 
 * @author Miro Bezjak
 */
public class FileTest {

    private FileStub file;

    @Before
    public void initEachTest() throws Exception {
        file = new FileStub();
    }

    /**
     * Test that properties are correctly set in constructor.
     */
    @Test
    public void constructor() throws Exception {
        assertEquals("names not same.", NAME, file.getName());
        assertEquals("types not same.", TYPE, file.getType());
        assertEquals("data not same.", DATA, file.getData());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor() throws Exception {
        Project project = new Project();
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
        assertNull("id not null.", another.getId());
        assertNull("version not null.", another.getVersion());
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
        Project project = new ProjectStub();
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
        Project project = new ProjectStub();
        Project anotherProject = new Project(project);
        FileStub anotherFile = new FileStub2();
        project.addFile(file);
        anotherProject.addFile(anotherFile);
        anotherFile.setName(NAME);
        assertEquals("not equal.", file, anotherFile);
        assertEquals("hashCode not same.", file.hashCode(), anotherFile
                .hashCode());
    }

    /**
     * Projects are different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        Project project = new ProjectStub();
        Project anotherProject = new ProjectStub2();
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
        Project project = new ProjectStub();
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
        Project project = new ProjectStub();
        Project anotherProject = new Project(project);
        FileStub anotherFile = new FileStub();
        project.addFile(file);
        anotherProject.addFile(anotherFile);
        anotherFile.setName(NAME_2);
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
        Project project = new ProjectStub();
        project.addFile(file);
        System.out.println(file);
    }

}
