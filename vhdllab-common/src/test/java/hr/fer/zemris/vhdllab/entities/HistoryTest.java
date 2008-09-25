package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link History}.
 * 
 * @author Miro Bezjak
 */
public class HistoryTest {

    private History history;

    @Before
    public void initEachTest() throws Exception {
        history = StubFactory.create(History.class, 1);
    }

    /**
     * Test that properties are correctly set in default constructor.
     */
    @Test
    public void constructorDefault() throws Exception {
        history = new History();
        assertEquals("insertVersion not 0.", Integer.valueOf(0), history
                .getInsertVersion());
        assertEquals("updateVersion not 0.", Integer.valueOf(0), history
                .getUpdateVersion());
        assertNotNull("createdOn is null.", history.getCreatedOn());
        assertNull("deletedOn is not null.", history.getDeletedOn());
    }

    /**
     * Test that properties are correctly set in a constructor.
     */
    @Test
    public void constructor() throws Exception {
        Integer insertVersion = 3;
        Integer updateVersion = 7;
        history = new History(insertVersion, updateVersion);
        assertEquals("insertVersion is wrong.", insertVersion, history
                .getInsertVersion());
        assertEquals("updateVersion is wrong.", updateVersion, history
                .getUpdateVersion());
        assertNotNull("createdOn is null.", history.getCreatedOn());
        assertNull("deletedOn is not null.", history.getDeletedOn());
    }

    /**
     * Insert version is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        new History(null, 1);
    }

    /**
     * Update version is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor3() throws Exception {
        new History(1, null);
    }

    /**
     * Insert version is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor4() throws Exception {
        new History(-1, 1);
    }

    /**
     * Update version is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor5() throws Exception {
        new History(1, -1);
    }

    /**
     * Set deleted on to null.
     */
    @Test
    public void setDeletedOn() {
        assertNotNull("deleted on is null.", history.getDeletedOn());
        history.setDeletedOn(null);
        assertNull("delete on not set to null.", history.getDeletedOn());
    }

    /**
     * Set deleted on to new timestamp.
     */
    @Test
    public void setDeletedOn2() {
        Date timestamp = new Date();
        history.setDeletedOn(timestamp);
        assertEquals("timestamps not same.", timestamp, history.getDeletedOn());
    }

    /**
     * Set deleted on to before created on.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setDeletedOn3() throws Exception {
        Date deletedOn = StubFactory.getStubValue("deletedOn", 304);
        history.setDeletedOn(deletedOn);
    }
    
    /**
     * Entities are same after deserialization.
     */
    @Test
    public void serialization() throws Exception {
        History deserialized = (History) SerializationUtils.clone(history);
        assertEquals("insertVersions not same", history.getInsertVersion(),
                deserialized.getInsertVersion());
        assertEquals("updateVersions not same", history.getUpdateVersion(),
                deserialized.getUpdateVersion());
        assertEquals("createdOn not same.", history.getCreatedOn(),
                deserialized.getCreatedOn());
        assertEquals("deletedOn not same.", history.getDeletedOn(),
                deserialized.getDeletedOn());
    }

    /**
     * Simulate data tempering - insertVersion is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization2() throws Exception {
        StubFactory.setProperty(history, "insertVersion", 300);
        SerializationUtils.clone(history);
    }

    /**
     * Simulate data tempering - updateVersion is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization3() throws Exception {
        StubFactory.setProperty(history, "updateVersion", 300);
        SerializationUtils.clone(history);
    }

    /**
     * Simulate data tempering - createdOn is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization4() throws Exception {
        StubFactory.setProperty(history, "createdOn", 300);
        SerializationUtils.clone(history);
    }

    /**
     * Simulate data tempering - insertVersion is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization5() throws Exception {
        StubFactory.setProperty(history, "insertVersion", 303);
        SerializationUtils.clone(history);
    }

    /**
     * Simulate data tempering - updateVersion is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization6() throws Exception {
        StubFactory.setProperty(history, "updateVersion", 303);
        SerializationUtils.clone(history);
    }
    
    /**
     * Simulate data tempering - deleted on is before created on.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization7() throws Exception {
        history = new History(); // ensures that createdOn is new Date()
        StubFactory.setProperty(history, "deletedOn", 304);
        SerializationUtils.clone(history);
    }
    
    @Test
    public void testToString() {
        System.out.println(history);
    }

}
