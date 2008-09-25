package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link ProjectInfo}.
 * 
 * @author Miro Bezjak
 */
public class ProjectInfoTest {

    private static final Caseless NAME = StubFactory.getStubValue("name", 1);

    private ProjectInfo project;

    @Before
    public void initEachTest() throws Exception {
        project = StubFactory.create(ProjectInfo.class, 1);
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
        Caseless userId = StubFactory.getStubValue("userId", 301);
        new ProjectInfo(userId, NAME);
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
        ProjectInfo another = StubFactory.create(ProjectInfo.class, 2);
        StubFactory.setProperty(another, "name", 1);
        StubFactory.setProperty(another, "userId", 1);
        assertEquals("not equal.", project, another);
        assertEquals("hashCode not same.", project.hashCode(), another
                .hashCode());
    }

    /**
     * UserIds are different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        ProjectInfo another = new ProjectInfo(project);
        StubFactory.setProperty(another, "userId", 2);
        assertFalse("equal.", project.equals(another));
        assertNotSame("same hashCode.", project.hashCode(), another.hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode3() throws Exception {
        ProjectInfo another = new ProjectInfo(project);
        StubFactory.setProperty(another, "name", 2);
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
        StubFactory.setProperty(project, "userId", 300);
        SerializationUtils.clone(project);
    }

    /**
     * Simulate data tempering - userId is too long.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization3() throws Exception {
        StubFactory.setProperty(project, "userId", 301);
        SerializationUtils.clone(project);
    }

    @Test
    public void testToString() {
        System.out.println(project);
    }

}
