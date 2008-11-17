package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.VERSION_2;
import static hr.fer.zemris.vhdllab.entities.stub.IHistoryStub.CREATED_ON_2;
import static hr.fer.zemris.vhdllab.entities.stub.IHistoryStub.DELETED_ON_2;
import static hr.fer.zemris.vhdllab.entities.stub.IHistoryStub.INSERT_VERSION_2;
import static hr.fer.zemris.vhdllab.entities.stub.IHistoryStub.UPDATE_VERSION_2;
import static hr.fer.zemris.vhdllab.entities.stub.IOwnableStub.USER_ID_2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.HistoryStub;
import hr.fer.zemris.vhdllab.entities.stub.ProjectInfoHistoryStub;
import hr.fer.zemris.vhdllab.entities.stub.ProjectInfoStub;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link ProjectInfoHistory}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ProjectInfoHistoryTest {

    private ProjectInfoHistoryStub project;

    @Before
    public void initEachTest() throws Exception {
        project = new ProjectInfoHistoryStub();
    }

    /**
     * ProjectInfo is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new ProjectInfoHistory(null, new HistoryStub());
    }

    /**
     * History is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        new ProjectInfoHistory(new ProjectInfoStub(), null);
    }

    /**
     * ProjectInfoHistory is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new ProjectInfoHistory(null);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor2() throws Exception {
        ProjectInfoHistory another = new ProjectInfoHistory(project);
        assertTrue("same reference.", project != another);
        assertEquals("not equal.", project, another);
        assertEquals("hashCode not same.", project.hashCode(), another
                .hashCode());
        assertEquals("histories not identical.", project.getHistory(), another
                .getHistory());
    }

    /**
     * Test equals with self, null, and non-ProjectInfoHistory.
     */
    @Test
    public void equals() throws Exception {
        assertEquals("not equal.", project, project);
        assertFalse("project is equal to null.", project.equals(null));
        assertFalse("can compare with string object.", project
                .equals("string object"));
    }

    /**
     * Only userIds, names, insertVersions and updateVersions are important in
     * equals and hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        ProjectInfoHistoryStub another = new ProjectInfoHistoryStub();
        another.setId(ID_2);
        another.setVersion(VERSION_2);
        HistoryStub history = (HistoryStub) another.getHistory();
        history.setCreatedOn(CREATED_ON_2);
        history.setDeletedOn(DELETED_ON_2);
        assertEquals("not equal.", project, another);
        assertEquals("hashCode not same.", project.hashCode(), another
                .hashCode());
    }

    /**
     * Insert version is different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        ProjectInfoHistoryStub another = new ProjectInfoHistoryStub();
        ((HistoryStub) another.getHistory()).setInsertVersion(INSERT_VERSION_2);
        assertFalse("equal.", project.equals(another));
        assertNotSame("same hashCode.", project.hashCode(), another.hashCode());
    }

    /**
     * Update version is different.
     */
    @Test
    public void equalsAndHashCode3() throws Exception {
        ProjectInfoHistoryStub another = new ProjectInfoHistoryStub();
        ((HistoryStub) another.getHistory()).setUpdateVersion(UPDATE_VERSION_2);
        assertFalse("equal.", project.equals(another));
        assertNotSame("same hashCode.", project.hashCode(), another.hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode4() throws Exception {
        ProjectInfoHistoryStub another = new ProjectInfoHistoryStub(project);
        another.setName(NAME_2);
        assertFalse("equal.", project.equals(another));
        assertNotSame("same hashCode.", project.hashCode(), another.hashCode());
    }

    /**
     * UserIds are different.
     */
    @Test
    public void equalsAndHashCode5() throws Exception {
        ProjectInfoHistoryStub another = new ProjectInfoHistoryStub(project);
        another.setUserId(USER_ID_2);
        assertFalse("equal.", project.equals(another));
        assertNotSame("same hashCode.", project.hashCode(), another.hashCode());
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
     * Simulate data tempering - userId is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization2() throws Exception {
        project.setUserId(null);
        SerializationUtils.clone(project);
    }

    @Test
    public void testToString() {
        System.out.println(project);
    }

}
