package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static hr.fer.zemris.vhdllab.entities.stub.IOwnableStub.USER_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.FileStub;
import hr.fer.zemris.vhdllab.entities.stub.FileStub2;
import hr.fer.zemris.vhdllab.entities.stub.ProjectStub;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link Project} entity.
 * 
 * @author Miro Bezjak
 */
public class ProjectTest {

    private ProjectStub project;

    @Before
    public void initEachTest() throws Exception {
        project = new ProjectStub();
        project.addFile(new FileStub());
    }

    /**
     * User id is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new Project(null, NAME);
    }

    /**
     * Name is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        new Project(USER_ID, null);
    }

    /**
     * Files isn't null after creation.
     */
    @Test
    public void constructor3() throws Exception {
        Project another = new Project(USER_ID, NAME);
        assertNotNull("files is null.", another.getFiles());
        assertTrue("files not empty.", another.getFiles().isEmpty());
    }

    /**
     * Project is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new Project(null);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor2() throws Exception {
        Set<File> files = project.getFiles();
        Project another = new Project(project);
        assertTrue("same reference.", project != another);
        assertEquals("not equal.", project, another);
        assertEquals("hashCode not same.", project.hashCode(), another
                .hashCode());
        assertEquals("files are copied.", Collections.emptySet(), another
                .getFiles());
        assertNotNull("original file reference is missing.", project.getFiles());
        assertEquals("original files has been modified.", files, project
                .getFiles());
    }

    /**
     * Get files returns a modifiable version. (users are discouraged to use
     * direct files reference to add or remove a file)
     */
    @Test
    public void getFiles() throws Exception {
        Set<File> files = new HashSet<File>(project.getFiles());
        File anotherFile = new FileStub2();
        project.getFiles().add(anotherFile);
        files.add(anotherFile);
        assertTrue("file not added.", project.getFiles().contains(anotherFile));
        assertEquals("file size not same.", files.size(), project.getFiles()
                .size());
        assertEquals("files not same.", files, project.getFiles());
    }

    /**
     * File is null.
     */
    @Test(expected = NullPointerException.class)
    public void addFile() {
        project.addFile(null);
    }

    /**
     * Add a file.
     */
    @Test
    public void addFile2() throws Exception {
        Set<File> files = new HashSet<File>(project.getFiles());
        File anotherFile = new FileStub2();
        project.addFile(anotherFile);
        files.add(anotherFile);
        assertTrue("file not added.", project.getFiles().contains(anotherFile));
        assertEquals("file size not same.", files.size(), project.getFiles()
                .size());
        assertEquals("files not same.", files, project.getFiles());
    }

    /**
     * Add a file that is already in that project.
     */
    @Test
    public void addFile3() throws Exception {
        Set<File> files = new HashSet<File>(project.getFiles());
        File file = project.getFiles().iterator().next();
        project.addFile(file);
        assertEquals("files not same.", files, project.getFiles());
        assertEquals("files is changed.", 1, project.getFiles().size());
    }

    /**
     * Add a file that is already in another project.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addFile4() throws Exception {
        Project another = new Project(project);
        File file = project.getFiles().iterator().next();
        another.addFile(file);
    }

    /**
     * File is null.
     */
    @Test(expected = NullPointerException.class)
    public void removeFile() throws Exception {
        project.removeFile(null);
    }

    /**
     * Remove a file.
     */
    @Test
    public void removeFile2() throws Exception {
        File file = project.getFiles().iterator().next();
        project.removeFile(file);
        assertEquals("files not empty.", Collections.emptySet(), project
                .getFiles());
        assertNull("project isn't set to null.", file.getProject());
    }

    /**
     * Remove a file that doesn't belong to any project.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeFile3() throws Exception {
        File anotherFile = new FileStub2();
        project.removeFile(anotherFile);
    }

    /**
     * Remove a file that belongs to another project.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeFile4() throws Exception {
        File file = project.getFiles().iterator().next();
        Project another = new Project(project);
        another.removeFile(file);
    }

    /**
     * Test equals to ProjectInfo since Project doesn't override equals and
     * hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        ProjectInfo info = new ProjectInfo(project);
        assertTrue("not same.", info.equals(project));
        assertTrue("not same.", project.equals(info));
        assertEquals("hashcode not same.", info.hashCode(), project.hashCode());
    }

    /**
     * Entities are same after deserialization.
     */
    @Test
    public void serialization() throws Exception {
        Object deserialized = SerializationUtils.clone(project);
        assertEquals("not same.", project, deserialized);
    }

    /**
     * Simulate data tempering - files is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization2() throws Exception {
        project.setFiles(null);
        SerializationUtils.clone(project);
    }

    @Test
    public void asString() {
        System.out.println(project);
    }

}
