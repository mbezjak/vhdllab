package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.ID;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.VERSION;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.VERSION_2;
import static hr.fer.zemris.vhdllab.entities.stub.IOwnableStub.USER_ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IOwnableStub.USER_ID_TOO_LONG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.ProjectInfoStub;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link ProjectInfo}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ProjectInfoTest {

    private ProjectInfoStub project;

    @Before
    public void initEachTest() throws Exception {
        project = new ProjectInfoStub();
    }

    /**
     * UserId is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new ProjectInfo(null, NAME);
    }

    /**
     * UserId is too long.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor2() throws Exception {
        new ProjectInfo(USER_ID_TOO_LONG, NAME);
    }

    /**
     * ProjectInfo is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new ProjectInfo((ProjectInfo) null);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor2() throws Exception {
        ProjectInfo another = new ProjectInfo(project);
        assertTrue("same reference.", project != another);
        assertEquals("not equal.", project, another);
        assertEquals("hashCode not same.", project.hashCode(), another
                .hashCode());
        assertEquals("userIds not same.", project.getUserId(), another
                .getUserId());
        assertEquals("id not copied.", ID, another.getId());
        assertEquals("version not copied.", VERSION, another.getVersion());
    }

    /**
     * Test equals with self, null, and non-ProjectInfo.
     */
    @Test
    public void equals() throws Exception {
        assertEquals("not equal.", project, project);
        assertFalse("file is equal to null.", project.equals(null));
        assertFalse("can compare with string object.", project
                .equals("string object"));
    }

    /**
     * Only userIds and names are important in equals and hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        ProjectInfoStub another = new ProjectInfoStub();
        another.setId(ID_2);
        another.setVersion(VERSION_2);
        assertEquals("not equal.", project, another);
        assertEquals("hashCode not same.", project.hashCode(), another
                .hashCode());
    }

    /**
     * UserIds are different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        ProjectInfoStub another = new ProjectInfoStub(project);
        another.setUserId(USER_ID_2);
        assertFalse("equal.", project.equals(another));
        assertNotSame("same hashCode.", project.hashCode(), another.hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode3() throws Exception {
        ProjectInfoStub another = new ProjectInfoStub(project);
        another.setName(NAME_2);
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

    /**
     * Simulate data tempering - userId is too long.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization3() throws Exception {
        project.setUserId(USER_ID_TOO_LONG);
        SerializationUtils.clone(project);
    }

    @Test
    public void testToString() {
        System.out.println(project);
    }

}
