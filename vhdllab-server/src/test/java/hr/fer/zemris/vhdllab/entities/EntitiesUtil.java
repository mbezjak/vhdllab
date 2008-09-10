package hr.fer.zemris.vhdllab.entities;

import java.lang.reflect.Field;

/**
 * A helper class for all entities test cases.
 * 
 * @author Miro Bezjak
 */
public class EntitiesUtil {

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
    public static void injectValueToPrivateField(Object object,
            String fieldName, Object value) throws Exception {
        Class<?> clazz = object.getClass();
        Field field = null;
        while (true) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
                if (clazz == Object.class) {
                    throw new IllegalStateException("Field " + fieldName
                            + " not found in class "
                            + object.getClass().getCanonicalName());
                }
                continue;
            }
            break;
        }
        field.setAccessible(true);
        field.set(object, value);
    }

    /**
     * Generates junk string.
     * 
     * @param length
     *            a length of a string
     * @return a junk string
     * @throws IllegalArgumentException
     *             if <code>length</code> &lt; 0
     */
    public static String generateJunkString(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length must be >= 0 but was "
                    + length);
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append("a");
        }
        return sb.toString();
    }

}
