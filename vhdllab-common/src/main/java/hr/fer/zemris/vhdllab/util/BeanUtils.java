package hr.fer.zemris.vhdllab.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.UnhandledException;

public abstract class BeanUtils {

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
