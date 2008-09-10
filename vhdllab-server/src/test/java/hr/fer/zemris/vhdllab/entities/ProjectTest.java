package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.EntitiesUtil.generateJunkString;
import static hr.fer.zemris.vhdllab.entities.EntitiesUtil.injectValueToPrivateField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link Project} entity.
 *
 * @author Miro Bezjak
 */
public class ProjectTest {

    private static final Long ID = Long.valueOf(123456);
    private static final String NAME = "project.name";
    private static final String USER_ID = "user.identifier";
    private static final Date CREATED;
    private static final String NEW_NAME = "new.project.name";
    private static final String NEW_USER_ID = "new.user.identifier";
    private static final Date NEW_CREATED;

    static {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        try {
            CREATED = df.parse("2008-01-02 13-45");
            NEW_CREATED = df.parse("2000-12-31 07-13");
        } catch (ParseException e) {
            // should never happen. but if pattern should change report it by
            // throwing exception.
            throw new IllegalStateException(e);
        }
    }

    private Project project;
    private Project project2;
    private File file;

    @Before
    public void initEachTest() throws Exception {
        project = new Project(USER_ID, NAME);
        injectValueToPrivateField(project, "id", ID);
        injectValueToPrivateField(project, "created", CREATED);
        project2 = new Project(project);

        file = new File(project, "file.name", "file.type", "file.content");
        new File(file, project2);
    }

    /**
     * User id is null
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new Project(null, NAME);
    }

    /**
     * User id is too long
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor2() throws Exception {
        new Project(generateJunkString(Project.USER_ID_LENGTH + 1), NAME);
    }

    /**
     * User id is not null
     */
    @Test
    public void constructor3() throws Exception {
        Project newProject = new Project(USER_ID, NAME);
        assertEquals("user id not set.", USER_ID, newProject.getUserId());
    }

    /**
     * Test copy constructor
     */
    @Test
    public void copyConstructor() throws Exception {
        assertTrue("same reference.", project != project2);
        assertEquals("not equal.", project, project2);
        assertEquals("hashCode not same.", project.hashCode(), project2
                .hashCode());
        assertEquals("not equal by compareTo.", 0, project.compareTo(project2));
        assertEquals("files not same.", project.getChildren(), project2
                .getChildren());
    }

    /**
     * Test getters and setters
     */
    @Test
    public void gettersAndSetters() throws Exception {
        /*
         * Setters are tested indirectly. @Before method uses setters.
         */
        assertEquals("getUserId.", USER_ID, project.getUserId());
    }

    /**
     * Test equals with self, null, and non-project object
     */
    @Test
    public void equals() throws Exception {
        assertEquals("not equal.", project, project);
        assertFalse("project is equal to null.", project.equals(null));
        assertFalse("can compare with string object.", project
                .equals("a string object"));
        assertFalse("can compare with resource object.", project
                .equals(new Resource()));
    }

    /**
     * Null object as parameter to compareTo method
     */
    @Test(expected = NullPointerException.class)
    public void compareTo() throws Exception {
        project.compareTo(null);
    }

    /**
     * Non-project type
     */
    @Test(expected = ClassCastException.class)
    public void compareTo2() throws Exception {
        project.compareTo(new Container<File, Project>());
    }

    /**
     * Only user ids and names are important in equals, hashCode and compareTo
     */
    @Test
    public void equalsHashCodeAndCompareTo() throws Exception {
        project2 = new Project(USER_ID, NAME);
        injectValueToPrivateField(project2, "created", NEW_CREATED);
        injectValueToPrivateField(project2, "children", Collections.emptySet());
        assertEquals("not equal.", project, project2);
        assertEquals("hashCode not same.", project.hashCode(), project2
                .hashCode());
        assertEquals("not equal by compareTo.", 0, project.compareTo(project2));
    }

    /**
     * Names are different
     */
    @Test
    public void equalsHashCodeAndCompareTo2() throws Exception {
        injectValueToPrivateField(project2, "name", NEW_NAME);
        assertFalse("equal.", project.equals(project2));
        assertFalse("hashCode same.", project.hashCode() == project2.hashCode());
        assertEquals("not compared by name.", NAME.compareTo(NEW_NAME) < 0 ? -1
                : 1, project.compareTo(project2));
    }

    /**
     * User ids are different
     */
    @Test
    public void equalsHashCodeAndCompareTo3() throws Exception {
        injectValueToPrivateField(project2, "userId", NEW_USER_ID);
        assertFalse("equal.", project.equals(project2));
        assertFalse("hashCode same.", project.hashCode() == project2.hashCode());
        assertEquals("not compared by user id.",
                USER_ID.compareTo(NEW_USER_ID) < 0 ? -1 : 1, project
                        .compareTo(project2));
    }

    /**
     * User id is case insensitive
     */
    @Test
    public void equalsHashCodeAndCompareTo4() throws Exception {
        injectValueToPrivateField(project2, "userId", USER_ID.toUpperCase());
        assertEquals("not equal.", project, project2);
        assertEquals("hashCode not same.", project.hashCode(), project2
                .hashCode());
        assertEquals("not equal by compareTo.", 0, project.compareTo(project2));
    }

    @Ignore("must be tested by a user and this has already been tested")
    @Test
    public void asString() {
        System.out.println(project);
    }

}
