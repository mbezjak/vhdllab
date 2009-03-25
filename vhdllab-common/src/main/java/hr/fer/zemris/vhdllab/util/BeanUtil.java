package hr.fer.zemris.vhdllab.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

public abstract class BeanUtil {

    public static String getBeanName(Class<?> clazz) {
        Validate.notNull(clazz, "Bean class can't be null");
        return StringUtils.uncapitalize(clazz.getSimpleName());
    }

}
