package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link FileInfo}.
 * 
 * @author Miro Bezjak
 */
public class FileInfoTest {

    private FileInfo file;

    @Before
    public void initEachTest() throws Exception {
        file = StubFactory.create(FileInfo.class, 1);
    }

    /**
     * FileResource is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        Integer projectId = StubFactory.getStubValue("projectId", 1);
        new FileInfo(null, projectId);
    }

    /**
     * ProjectId is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        FileResource resource = StubFactory.getStubValue("fileResource", 1);
        new FileInfo(resource, null);
    }

    /**
     * FileInfo is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new FileInfo(null);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor2() throws Exception {
        FileInfo another = new FileInfo(file);
        assertTrue("same reference.", file != another);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
        assertEquals("projectIds not same.", file.getProjectId(), another
                .getProjectId());
    }

    /**
     * Test equals with self, null, and non-FileInfo.
     */
    @Test
    public void equals() throws Exception {
        assertEquals("not equal.", file, file);
        assertFalse("file is equal to null.", file.equals(null));
        assertFalse("can compare with string object.", file
                .equals("string object"));
    }

    /**
     * Only projectIds and names are important in equals and hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        FileInfo another = StubFactory.create(FileInfo.class, 2);
        StubFactory.setProperty(another, "name", 1);
        StubFactory.setProperty(another, "projectId", 1);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
    }

    /**
     * ProjectIds are different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        FileInfo another = new FileInfo(file);
        StubFactory.setProperty(another, "projectId", 2);
        assertFalse("equal.", file.equals(another));
        assertNotSame("same hashCode.", file.hashCode(), another.hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode3() throws Exception {
        FileInfo another = new FileInfo(file);
        StubFactory.setProperty(another, "name", 2);
        assertFalse("equal.", file.equals(another));
        assertNotSame("same hashCode.", file.hashCode(), another.hashCode());
    }

    /**
     * Entities are same after deserialization.
     */
    @Test
    public void serialization() throws Exception {
        Object deserialized = SerializationUtils.clone(file);
        assertEquals("not same.", file, deserialized);
    }

    /**
     * Simulate data tempering - projectId is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization2() throws Exception {
        StubFactory.setProperty(file, "projectId", 300);
        SerializationUtils.clone(file);
    }

    @Test
    public void testToString() {
        System.out.println(file);
    }

}
