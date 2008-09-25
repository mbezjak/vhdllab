package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link ProjectInfoHistory}.
 * 
 * @author Miro Bezjak
 */
public class ProjectInfoHistoryTest {

    private ProjectInfoHistory project;

    @Before
    public void initEachTest() throws Exception {
        project = StubFactory.create(ProjectInfoHistory.class, 1);
    }

    /**
     * ProjectInfo is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        History history = StubFactory.getStubValue("history", 1);
        new ProjectInfoHistory(null, history);
    }

    /**
     * History is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        ProjectInfo projectInfo = StubFactory.getStubValue("projectInfo", 1);
        new ProjectInfoHistory(projectInfo, null);
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
        ProjectInfoHistory another = StubFactory.create(
                ProjectInfoHistory.class, 2);
        StubFactory.setProperty(another, "name", 1);
        StubFactory.setProperty(another, "userId", 1);
        StubFactory.setProperty(another.getHistory(), "insertVersion", 1);
        StubFactory.setProperty(another.getHistory(), "updateVersion", 1);
        assertEquals("not equal.", project, another);
        assertEquals("hashCode not same.", project.hashCode(), another
                .hashCode());
    }

    /**
     * Insert version is different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        ProjectInfoHistory another = StubFactory.create(
                ProjectInfoHistory.class, 1);
        StubFactory.setProperty(another.getHistory(), "insertVersion", 2);
        assertFalse("equal.", project.equals(another));
        assertNotSame("same hashCode.", project.hashCode(), another.hashCode());
    }

    /**
     * Update version is different.
     */
    @Test
    public void equalsAndHashCode3() throws Exception {
        ProjectInfoHistory another = StubFactory.create(
                ProjectInfoHistory.class, 1);
        StubFactory.setProperty(another.getHistory(), "updateVersion", 2);
        assertFalse("equal.", project.equals(another));
        assertNotSame("same hashCode.", project.hashCode(), another.hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode4() throws Exception {
        ProjectInfoHistory another = new ProjectInfoHistory(project);
        StubFactory.setProperty(another, "name", 2);
        assertFalse("equal.", project.equals(another));
        assertNotSame("same hashCode.", project.hashCode(), another.hashCode());
    }

    /**
     * UserIds are different.
     */
    @Test
    public void equalsAndHashCode5() throws Exception {
        ProjectInfoHistory another = new ProjectInfoHistory(project);
        StubFactory.setProperty(another, "userId", 2);
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

    @Test
    public void testToString() {
        System.out.println(project);
    }

}
