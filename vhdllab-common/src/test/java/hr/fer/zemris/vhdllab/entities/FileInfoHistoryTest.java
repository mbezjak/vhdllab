package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.ID;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.VERSION;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.VERSION_2;
import static hr.fer.zemris.vhdllab.entities.stub.IFileInfoStub.PROJECT_ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IFileResourceStub.TYPE_2;
import static hr.fer.zemris.vhdllab.entities.stub.IHistoryStub.CREATED_ON_2;
import static hr.fer.zemris.vhdllab.entities.stub.IHistoryStub.DELETED_ON_2;
import static hr.fer.zemris.vhdllab.entities.stub.IHistoryStub.INSERT_VERSION_2;
import static hr.fer.zemris.vhdllab.entities.stub.IHistoryStub.UPDATE_VERSION_2;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA_2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.FileInfoHistoryStub;
import hr.fer.zemris.vhdllab.entities.stub.FileInfoStub;
import hr.fer.zemris.vhdllab.entities.stub.HistoryStub;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link FileInfoHistory}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class FileInfoHistoryTest {

    private FileInfoHistoryStub file;

    @Before
    public void initEachTest() throws Exception {
        file = new FileInfoHistoryStub();
    }

    /**
     * FileInfo is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new FileInfoHistory(null, new HistoryStub());
    }

    /**
     * History is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        new FileInfoHistory(new FileInfoStub(), null);
    }

    /**
     * By default info objects copy id and version fields.
     */
    @Test
    public void constructor3() throws Exception {
        FileInfoHistory another = new FileInfoHistory(
                new FileInfoStub(), new HistoryStub());
        assertEquals("id not copied.", ID, another.getId());
        assertEquals("version not copied.", VERSION, another.getVersion());
    }

    /**
     * FileInfoHistory is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new FileInfoHistory(null, true);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor2() throws Exception {
        FileInfoHistory another = new FileInfoHistory(file, true);
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
        FileInfoHistoryStub another = new FileInfoHistoryStub();
        another.setId(ID_2);
        another.setVersion(VERSION_2);
        another.setData(DATA_2);
        another.setType(TYPE_2);
        HistoryStub history = (HistoryStub) another.getHistory();
        history.setCreatedOn(CREATED_ON_2);
        history.setDeletedOn(DELETED_ON_2);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
    }

    /**
     * Insert version is different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        FileInfoHistoryStub another = new FileInfoHistoryStub();
        ((HistoryStub) another.getHistory()).setInsertVersion(INSERT_VERSION_2);
        assertFalse("equal.", file.equals(another));
        assertNotSame("same hashCode.", file.hashCode(), another.hashCode());
    }

    /**
     * Update version is different.
     */
    @Test
    public void equalsAndHashCode3() throws Exception {
        FileInfoHistoryStub another = new FileInfoHistoryStub();
        ((HistoryStub) another.getHistory()).setInsertVersion(UPDATE_VERSION_2);
        assertFalse("equal.", file.equals(another));
        assertNotSame("same hashCode.", file.hashCode(), another.hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode4() throws Exception {
        FileInfoHistoryStub another = new FileInfoHistoryStub(file);
        another.setName(NAME_2);
        assertFalse("equal.", file.equals(another));
        assertNotSame("same hashCode.", file.hashCode(), another.hashCode());
    }

    /**
     * ProjectIds are different.
     */
    @Test
    public void equalsAndHashCode5() throws Exception {
        FileInfoHistoryStub another = new FileInfoHistoryStub(file);
        another.setProjectId(PROJECT_ID_2);
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
        file.setProjectId(null);
        SerializationUtils.clone(file);
    }

    @Test
    public void testToString() {
        System.out.println(file);
    }

}
