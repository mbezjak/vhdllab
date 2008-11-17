package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.HistoryStub;
import hr.fer.zemris.vhdllab.entities.stub.ProjectHistoryStub;
import hr.fer.zemris.vhdllab.entities.stub.ProjectInfoStub;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link ProjectHistory}.
 * 
 * @author Miro Bezjak
 */
public class ProjectHistoryTest {

    private ProjectHistoryStub history;

    @Before
    public void initEachTest() throws Exception {
        history = new ProjectHistoryStub();
    }

    /**
     * ProjectInfo is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new ProjectHistory(null, new HistoryStub());
    }

    /**
     * History is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        new ProjectHistory(new ProjectInfoStub(), null);
    }

    /**
     * Test equals to ProjectInfoHistory since ProjectHistory doesn't override
     * equals and hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        ProjectHistory another = new ProjectHistoryStub();
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
