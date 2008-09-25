package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link FileHistory}.
 * 
 * @author Miro Bezjak
 */
public class FileHistoryTest {

    private FileHistory history;

    @Before
    public void initEachTest() throws Exception {
        history = StubFactory.create(FileHistory.class, 1);
    }

    /**
     * FileInfo is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        History h = StubFactory.getStubValue("history", 1);
        new FileHistory(null, h);
    }

    /**
     * History is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        FileInfo fileInfo = StubFactory.getStubValue("fileInfo", 1);
        new FileHistory(fileInfo, null);
    }

    /**
     * Test equals to FileInfoHistory since FileHistory doesn't override equals
     * and hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        FileHistory another = StubFactory.create(FileHistory.class, 1);
        assertTrue("not same.", another.equals(history));
        assertTrue("not same.", history.equals(another));
        assertEquals("hashcode not same.", another.hashCode(), history
                .hashCode());
    }

    /**
     * Entities are same after deserialization.
     */
    @Test
    public void serialization() throws Exception {
        Object deserialized = SerializationUtils.clone(history);
        assertEquals("not same.", history, deserialized);
    }

    @Test
    public void testToString() {
        System.out.println(history);
    }

}
