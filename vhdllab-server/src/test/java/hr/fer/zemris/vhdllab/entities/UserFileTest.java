package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.EntitiesUtil.generateJunkString;
import static hr.fer.zemris.vhdllab.entities.EntitiesUtil.injectValueToPrivateField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link UserFile} entity.
 *
 * @author Miro Bezjak
 */
public class UserFileTest {

    private static final Long ID = Long.valueOf(123456);
    private static final String USER_ID = "user.identifier";
    private static final String NAME = "file.name";
    private static final String TYPE = "file.type";
    private static final String CONTENT = "...file content...";
    private static final Date CREATED;
    private static final Long NEW_ID = Long.valueOf(654321);
    private static final String NEW_USER_ID = "new." + USER_ID;
    private static final String NEW_NAME = "new." + NAME;
    private static final String NEW_TYPE = "new." + TYPE;
    private static final String NEW_CONTENT = "new." + CONTENT;
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

    private UserFile file;
    private UserFile file2;

    @Before
    public void initEachTest() throws Exception {
        file = new UserFile(USER_ID, NAME, TYPE, CONTENT);
        injectValueToPrivateField(file, "id", ID);
        injectValueToPrivateField(file, "created", CREATED);
        file2 = new UserFile(file);
    }

    /**
     * User id is null
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new UserFile(null, NAME, TYPE);
    }

    /**
     * User id is too long
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor2() throws Exception {
        new UserFile(generateJunkString(UserFile.USER_ID_LENGTH + 1), NAME,
                TYPE);
    }

    /**
     * User id is null
     */
    @Test(expected = NullPointerException.class)
    public void constructor3() throws Exception {
        new UserFile(null, NAME, TYPE, CONTENT);
    }

    /**
     * User id is too long
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor4() throws Exception {
        new UserFile(generateJunkString(UserFile.USER_ID_LENGTH + 1), NAME,
                TYPE, CONTENT);
    }

    /**
     * User file is null
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new UserFile(null);
    }

    /**
     * Test copy constructor
     */
    @Test
    public void copyConstructor2() throws Exception {
        assertTrue("same reference.", file != file2);
        assertEquals("not equal.", file, file2);
        assertEquals("hashCode not same.", file.hashCode(), file2.hashCode());
        assertEquals("not equal by compareTo.", 0, file.compareTo(file2));
    }

    /**
     * Test getters and setters
     */
    @Test
    public void gettersAndSetters() throws Exception {
        /*
         * Setters are tested indirectly. @Before method uses setters.
         */
        assertEquals("getUserId.", USER_ID, file.getUserId());
    }

    /**
     * Test equals with self, null, and non-user-file object
     */
    @Test
    public void equals() throws Exception {
        assertEquals("not equal.", file, file);
        assertFalse("file is equal to null.", file.equals(null));
        assertFalse("can compare with string object.", file
                .equals(new Resource()));
        assertFalse("can compare with resource object.", file
                .equals(new Resource()));
    }

    /**
     * Null object as parameter to compareTo method
     */
    @Test(expected = NullPointerException.class)
    public void compareTo() throws Exception {
        file.compareTo(null);
    }

    /**
     * Non-user-file type
     */
    @Test(expected = ClassCastException.class)
    public void compareTo2() throws Exception {
        file.compareTo(new Resource());
    }

    /**
     * Only user ids and names are important in equals, hashCode and compareTo
     */
    @Test
    public void equalsHashCodeAndCompareTo() throws Exception {
        file2 = new UserFile(USER_ID, NAME, NEW_TYPE, NEW_CONTENT);
        injectValueToPrivateField(file2, "id", NEW_ID);
        injectValueToPrivateField(file2, "created", NEW_CREATED);
        assertEquals("not equal.", file, file2);
        assertEquals("hashCode not same.", file.hashCode(), file2.hashCode());
        assertEquals("not equal by compareTo.", 0, file.compareTo(file2));
    }

    /**
     * Names are different
     */
    @Test
    public void equalsHashCodeAndCompareTo2() throws Exception {
        injectValueToPrivateField(file2, "name", NEW_NAME);
        assertFalse("equal.", file.equals(file2));
        assertFalse("hashCode same.", file.hashCode() == file2.hashCode());
        assertEquals("not compared by name.", NAME.compareTo(NEW_NAME) < 0 ? -1
                : 1, file.compareTo(file2));
    }

    /**
     * User ids are different
     */
    @Test
    public void equalsHashCodeAndCompareTo3() throws Exception {
        injectValueToPrivateField(file2, "userId", NEW_USER_ID);
        assertFalse("equal.", file.equals(file2));
        assertFalse("hashCode same.", file.hashCode() == file2.hashCode());
        assertEquals("not compared by user id.",
                USER_ID.compareTo(NEW_USER_ID) < 0 ? -1 : 1, file
                        .compareTo(file2));
    }

    /**
     * User id is case insensitive
     */
    @Test
    public void equalsHashCodeAndCompareTo4() throws Exception {
        injectValueToPrivateField(file2, "userId", USER_ID.toUpperCase());
        assertEquals("not equal.", file, file2);
        assertEquals("hashCode not same.", file.hashCode(), file2.hashCode());
        assertEquals("not equal by compareTo.", 0, file.compareTo(file2));
    }

    @Ignore("must be tested by a user and this has already been tested")
    @Test
    public void asString() {
        System.out.println(file.toString());
    }

}
