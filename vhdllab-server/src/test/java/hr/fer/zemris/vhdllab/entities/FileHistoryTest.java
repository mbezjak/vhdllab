package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.FileHistoryStub;
import hr.fer.zemris.vhdllab.entities.stub.FileInfoStub;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link FileHistory}.
 * 
 * @author Miro Bezjak
 */
public class FileHistoryTest {

    private FileHistoryStub history;

    @Before
    public void initEachTest() throws Exception {
        history = new FileHistoryStub();
    }

    /**
     * FileInfo is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new FileHistory(null, new History());
    }

    /**
     * History is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        new FileHistory(new FileInfoStub(), null);
    }

    /**
     * Test equals to FileInfoHistory since FileHistory doesn't override equals
     * and hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        FileHistory another = new FileHistoryStub();
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
