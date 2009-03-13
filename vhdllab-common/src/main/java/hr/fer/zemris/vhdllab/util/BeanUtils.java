package hr.fer.zemris.vhdllab.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.UnhandledException;
import org.apache.commons.lang.Validate;

public abstract class BeanUtils {

    public static String getBeanName(Class<?> clazz) {
        Validate.notNull(clazz, "Bean class can't be null");
        return StringUtils.uncapitalize(clazz.getSimpleName());
    }

    public static void copyProperties(Object dest, Object orig) {
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
        } catch (IllegalAccessException e) {
            throw new UnhandledException(e);
        } catch (InvocationTargetException e) {
            throw new UnhandledException(e);
        }
    }

}
