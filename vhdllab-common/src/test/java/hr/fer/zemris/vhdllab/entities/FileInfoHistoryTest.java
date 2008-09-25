package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link FileInfoHistory}.
 * 
 * @author Miro Bezjak
 */
public class FileInfoHistoryTest {

    private FileInfoHistory file;

    @Before
    public void initEachTest() throws Exception {
        file = StubFactory.create(FileInfoHistory.class, 1);
    }

    /**
     * FileInfo is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        History history = StubFactory.getStubValue("history", 1);
        new FileInfoHistory(null, history);
    }

    /**
     * History is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        FileInfo fileInfo = StubFactory.getStubValue("fileInfo", 1);
        new FileInfoHistory(fileInfo, null);
    }

    /**
     * FileInfoHistory is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new FileInfoHistory(null);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor2() throws Exception {
        FileInfoHistory another = new FileInfoHistory(file);
        assertTrue("same reference.", file != another);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
        assertEquals("histories not identical.", file.getHistory(), another
                .getHistory());
    }

    /**
     * Test equals with self, null, and non-FileInfoHistory.
     */
    @Test
    public void equals() throws Exception {
        assertEquals("not equal.", file, file);
        assertFalse("file is equal to null.", file.equals(null));
        assertFalse("can compare with string object.", file
                .equals("string object"));
    }

    /**
     * Only projectIds, names, insertVersions and updateVersions are important
     * in equals and hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        FileInfoHistory another = StubFactory.create(FileInfoHistory.class, 2);
        StubFactory.setProperty(another, "name", 1);
        StubFactory.setProperty(another, "projectId", 1);
        StubFactory.setProperty(another.getHistory(), "insertVersion", 1);
        StubFactory.setProperty(another.getHistory(), "updateVersion", 1);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
    }

    /**
     * Insert version is different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        FileInfoHistory another = StubFactory.create(FileInfoHistory.class, 1);
        StubFactory.setProperty(another.getHistory(), "insertVersion", 2);
        assertFalse("equal.", file.equals(another));
        assertNotSame("same hashCode.", file.hashCode(), another.hashCode());
    }

    /**
     * Update version is different.
     */
    @Test
    public void equalsAndHashCode3() throws Exception {
        FileInfoHistory another = StubFactory.create(FileInfoHistory.class, 1);
        StubFactory.setProperty(another.getHistory(), "updateVersion", 2);
        assertFalse("equal.", file.equals(another));
        assertNotSame("same hashCode.", file.hashCode(), another.hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode4() throws Exception {
        FileInfoHistory another = new FileInfoHistory(file);
        StubFactory.setProperty(another, "name", 2);
        assertFalse("equal.", file.equals(another));
        assertNotSame("same hashCode.", file.hashCode(), another.hashCode());
    }

    /**
     * ProjectIds are different.
     */
    @Test
    public void equalsAndHashCode5() throws Exception {
        FileInfoHistory another = new FileInfoHistory(file);
        StubFactory.setProperty(another, "projectId", 2);
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
