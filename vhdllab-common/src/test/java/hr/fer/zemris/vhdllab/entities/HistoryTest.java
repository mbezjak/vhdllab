package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IHistoryStub.DELETED_ON_BEFORE_ANYTHING;
import static hr.fer.zemris.vhdllab.entities.stub.IHistoryStub.INSERT_VERSION;
import static hr.fer.zemris.vhdllab.entities.stub.IHistoryStub.INSERT_VERSION_NEGATIVE;
import static hr.fer.zemris.vhdllab.entities.stub.IHistoryStub.UPDATE_VERSION;
import static hr.fer.zemris.vhdllab.entities.stub.IHistoryStub.UPDATE_VERSION_NEGATIVE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entities.stub.HistoryStub;

import java.util.Date;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link History}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class HistoryTest {

    private HistoryStub history;

    @Before
    public void initEachTest() throws Exception {
        history = new HistoryStub();
    }

    /**
     * Test that properties are correctly set in default constructor.
     */
    @Test
    public void constructorDefault() throws Exception {
        History another = new History();
        assertEquals("insertVersion not 0.", Integer.valueOf(0), another
                .getInsertVersion());
        assertEquals("updateVersion not 0.", Integer.valueOf(0), another
                .getUpdateVersion());
        assertNotNull("createdOn is null.", another.getCreatedOn());
        assertNull("deletedOn is not null.", another.getDeletedOn());
    }

    /**
     * Test that properties are correctly set in a constructor.
     */
    @Test
    public void constructor() throws Exception {
        History another = new History(INSERT_VERSION, UPDATE_VERSION);
        assertEquals("insertVersion is wrong.", INSERT_VERSION, another
                .getInsertVersion());
        assertEquals("updateVersion is wrong.", UPDATE_VERSION, another
                .getUpdateVersion());
        assertNotNull("createdOn is null.", another.getCreatedOn());
        assertNull("deletedOn is not null.", another.getDeletedOn());
    }

    /**
     * Insert version is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        new History(null, UPDATE_VERSION);
    }

    /**
     * Update version is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor3() throws Exception {
        new History(INSERT_VERSION, null);
    }

    /**
     * Insert version is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor4() throws Exception {
        new History(INSERT_VERSION_NEGATIVE, UPDATE_VERSION);
    }

    /**
     * Update version is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor5() throws Exception {
        new History(INSERT_VERSION, UPDATE_VERSION_NEGATIVE);
    }

    /**
     * Set deleted on.
     */
    @Test
    public void setDeletedOn() {
        assertNotNull("deleted on is null.", history.getDeletedOn());
        history.setDeletedOn(null);
        assertNull("delete on not set to null.", history.getDeletedOn());
        Date timestamp = new Date();
        history.setDeletedOn(timestamp);
        assertEquals("timestamps not same.", timestamp, history.getDeletedOn());
    }

    /**
     * Set deleted on to before created on.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setDeletedOn2() throws Exception {
        history.setDeletedOn(DELETED_ON_BEFORE_ANYTHING);
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
        history.setInsertVersion(null);
        SerializationUtils.clone(history);
    }

    /**
     * Simulate data tempering - updateVersion is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization3() throws Exception {
        history.setUpdateVersion(null);
        SerializationUtils.clone(history);
    }

    /**
     * Simulate data tempering - createdOn is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization4() throws Exception {
        history.setCreatedOn(null);
        SerializationUtils.clone(history);
    }

    /**
     * Simulate data tempering - insertVersion is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization5() throws Exception {
        history.setInsertVersion(INSERT_VERSION_NEGATIVE);
        SerializationUtils.clone(history);
    }

    /**
     * Simulate data tempering - updateVersion is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization6() throws Exception {
        history.setUpdateVersion(UPDATE_VERSION_NEGATIVE);
        SerializationUtils.clone(history);
    }

    /**
     * Simulate data tempering - deleted on is before created on.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization7() throws Exception {
        history.setDeletedOnField(DELETED_ON_BEFORE_ANYTHING);
        SerializationUtils.clone(history);
    }

    @Test
    public void testToString() {
        System.out.println(history);
    }

}
