package hr.fer.zemris.vhdllab.entities.stub;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Helper class for test cases.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class TestsUtil {
    /*
     * Note: RuntimeException is thrown to fail a test. Any other common
     * exception like IllegalStateException or IllegalArgumentException might be
     * caught in expected block of a test and we don't want that.
     */

    /**
     * Injects a specified value to <code>object</code>'s private field.
     * 
     * @param object
     *            an object whose private field should be altered
     * @param fieldName
     *            a name of a private field to set
     * @param value
     *            a value to set private field to
     * @throws RuntimeException
     *             if exceptional condition occurs
     */
    public static void setField(Object object, String fieldName, Object value) {
        Class<?> clazz = object.getClass();
        Field field = null;
        while (true) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
                if (clazz == Object.class) {
                    throw new RuntimeException("Field " + fieldName
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Date getDate(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        try {
            return df.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
