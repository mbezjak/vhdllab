package hr.fer.zemris.vhdllab.entities;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Helps creating entity stub objects used by tests.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class StubFactory {

    private static final Set<String> properties;
    private static final Map<String, Object> stubValues;
    static {
        properties = new HashSet<String>();
        stubValues = new HashMap<String, Object>();
        /*
         * Stub version 1: contains all correct values
         */
        putStubValue("id", 1, Integer.valueOf(123456));
        putStubValue("version", 1, Integer.valueOf(123654));
        putStubValue("name", 1, new Caseless("entity_name"));
        putStubValue("data", 1, "a resource data...");
        putStubValue("userId", 1, new Caseless("user.identifier"));
        putStubValue("type", 1, FileType.SOURCE);
        putStubValue("projectId", 1, Integer.valueOf(112233));
        putStubValue("libraryId", 1, Integer.valueOf(332211));
        putStubValue("insertVersion", 1, Integer.valueOf(2));
        putStubValue("updateVersion", 1, Integer.valueOf(7));
        putStubValue("createdOn", 1, getDate("2003-01-02 13-45"));
        putStubValue("deletedOn", 1, getDate("2005-05-07 15-31"));
        putStubValue("history", 1, createPrivate(History.class, 1));
        putStubValue("fileResource", 1, createPrivate(FileResource.class, 1));
        putStubValue("fileInfo", 1, createPrivate(FileInfo.class, 1));
        putStubValue("projectInfo", 1, createPrivate(ProjectInfo.class, 1));
        /*
         * Stub version 2: contains all correct values but different then stub
         * version 1
         */
        putStubValue("id", 2, Integer.valueOf(654321));
        putStubValue("version", 2, Integer.valueOf(654123));
        putStubValue("name", 2, new Caseless("new_entity_name"));
        putStubValue("data", 2, "...another resource data...");
        putStubValue("userId", 2, new Caseless("new.user.identifier"));
        putStubValue("type", 2, FileType.AUTOMATON);
        putStubValue("projectId", 2, Integer.valueOf(445566));
        putStubValue("libraryId", 2, Integer.valueOf(665544));
        putStubValue("insertVersion", 2, Integer.valueOf(5));
        putStubValue("updateVersion", 2, Integer.valueOf(9));
        putStubValue("createdOn", 2, getDate("2001-11-21 07-11"));
        putStubValue("deletedOn", 2, getDate("2007-03-18 21-56"));
        putStubValue("history", 2, createPrivate(History.class, 2));
        putStubValue("fileResource", 2, createPrivate(FileResource.class, 2));
        putStubValue("fileInfo", 2, createPrivate(FileInfo.class, 2));
        putStubValue("projectInfo", 2, createPrivate(ProjectInfo.class, 2));
        /*
         * Stub version 300: contains null values
         */
        putStubValue("id", 300, null);
        putStubValue("version", 300, null);
        putStubValue("name", 300, null);
        putStubValue("data", 300, null);
        putStubValue("userId", 300, null);
        putStubValue("type", 300, null);
        putStubValue("projectId", 300, null);
        putStubValue("libraryId", 300, null);
        putStubValue("insertVersion", 300, null);
        putStubValue("updateVersion", 300, null);
        putStubValue("createdOn", 300, null);
        putStubValue("files", 300, null);
        /*
         * Stub version 301: contains too big properties
         */
        putStubValue("name", 301, new Caseless(RandomStringUtils
                .randomAlphabetic(EntityObject.NAME_LENGTH + 1)));
        putStubValue("data", 301, RandomStringUtils
                .randomAlphabetic(Resource.DATA_LENGTH + 1));
        putStubValue("userId", 301, new Caseless(RandomStringUtils
                .randomAlphabetic(UserFileInfo.USER_ID_LENGTH + 1)));
        /*
         * Stub version 302: contains incorrectly formatted name
         */
        putStubValue("name", 302, new Caseless("_wrong format.for?entity-name"));
        /*
         * Stub version 303: contains negative versions
         */
        putStubValue("insertVersion", 303, Integer.valueOf(-1));
        putStubValue("updateVersion", 303, Integer.valueOf(-1));
        /*
         * Stub version 304: contains deletedOn that is before now (new Date())
         */
        putStubValue("deletedOn", 304, getDate("1980-02-05 23-21"));
        /*
         * Stub version 400: contains all correct values but without id and
         * version
         */
        putStubValue("name", 400, new Caseless("entity_name"));
        putStubValue("data", 400, "a resource data...");
        putStubValue("userId", 400, new Caseless("user.identifier"));
        putStubValue("type", 400, FileType.SOURCE);
        putStubValue("projectId", 400, Integer.valueOf(112233));
        putStubValue("libraryId", 400, Integer.valueOf(332211));
        putStubValue("insertVersion", 400, Integer.valueOf(2));
        putStubValue("updateVersion", 400, Integer.valueOf(7));
        putStubValue("createdOn", 400, getDate("2003-01-02 13-45"));
        putStubValue("deletedOn", 400, getDate("2005-05-07 15-31"));
        putStubValue("history", 400, createPrivate(History.class, 1));
    }

    private static void putStubValue(String property, int stubVersion,
            Object value) {
        String key = composeKey(property, stubVersion);
        if (stubValues.containsKey(key)) { // for copy/paste errors
            throw new IllegalArgumentException("Key " + key
                    + " already exists!");
        }
        stubValues.put(key, value);
        properties.add(property);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getStubValue(String property, int stubVersion) {
        /*
         * Parameterized to reduce repeating test code. Although
         * ClassCastException can happen, it is usually badly written stub
         * initialization (in this class).
         */
        String key = composeKey(property, stubVersion);
        if (!stubValues.containsKey(key)) {
            throw new NoSuchElementException(key
                    + " key doesn't exist in stubValues");
        }
        return (T) stubValues.get(composeKey(property, stubVersion));
    }

    private static String composeKey(String property, int stubVersion) {
        return property + "-" + stubVersion;
    }

    private static Date getDate(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        try {
            return df.parse(date);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Injects a specified value to <code>object</code>'s private field.
     * 
     * @param object
     *            an object whose private field should be altered
     * @param fieldName
     *            a name of a private field to set
     * @param value
     *            a value to set private field to
     * @throws Exception
     *             if exceptional condition occurs
     */
    @SuppressWarnings("null")
    private static void injectValue(Object object, String fieldName,
            Object value) throws Exception {
        Class<?> clazz = object.getClass();
        Field field = null;
        while (true) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
                if (clazz == Object.class) {
                    throw new NoSuchFieldException("Field " + fieldName
                            + " not found in class "
                            + object.getClass().getCanonicalName());
                }
                continue;
            }
            break;
        }
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalArgumentException e) {
            /*
             * Some tests might expect IllegalArgumentException because its a
             * common exception so rebrand IllegalArgumentException as
             * RuntimeException to fail a test.
             */
            throw new RuntimeException(e);
        }
    }

    private static <T> void injectPropertyValues(T instance, int stubVersion)
            throws Exception {
        for (String property : properties) {
            try {
                setProperty(instance, property, stubVersion);
            } catch (NoSuchFieldException e) {
                continue;
            }
        }
    }

    public static void setProperty(Object instance, String property,
            int stubVersion) throws Exception {
        Object value;
        if (property.equals("history")) { // to prevent aliasing problems
            // can't use create method cause that would cause StackOverflowError
            value = new History();
            Object stubValue = getStubValue("insertVersion", stubVersion);
            injectValue(value, "insertVersion", stubValue);
            stubValue = getStubValue("updateVersion", stubVersion);
            injectValue(value, "updateVersion", stubValue);
            stubValue = getStubValue("createdOn", stubVersion);
            injectValue(value, "createdOn", stubValue);
            stubValue = getStubValue("deletedOn", stubVersion);
            injectValue(value, "deletedOn", stubValue);
        } else {
            try {
                value = getStubValue(property, stubVersion);
            } catch (NoSuchElementException e) {
                // don't do anything if value doesn't exist
                return;
            }
        }
        injectValue(instance, property, value);
    }

    public static <T> T create(Class<T> clazz, int stubVersion)
            throws Exception {
        T instance = clazz.newInstance();
        injectPropertyValues(instance, stubVersion);
        return instance;
    }

    private static <T> T createPrivate(Class<T> clazz, int stubVersion) {
        try {
            return create(clazz, stubVersion);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private StubFactory() {
    }

}
