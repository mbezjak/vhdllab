package hr.fer.zemris.vhdllab.platform.i18n;

import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;

import java.awt.Frame;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public abstract class LocalizationSupport {

    protected static final char PART_SEPARATOR = '.';

    @Autowired
    private MessageSource messageSource;
    private String cached;

    protected String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, null);
    }

    protected Frame getFrame() {
        return ApplicationContextHolder.getContext().getFrame();
    }

    protected final String getMessageCodeFromSimpleClassName() {
        if (cached == null) {
            String className = getClass().getSimpleName();
            String altered = alterSimpleClassName(className);
            StringBuilder code = new StringBuilder(altered.length() + 30);
            String[] split = StringUtils.splitByCharacterTypeCamelCase(altered);
            for (String part : split) {
                part = part.toLowerCase(Locale.ENGLISH);
                code.append(part).append(PART_SEPARATOR);
            }
            if (split.length > 0) {
                code.deleteCharAt(code.length() - 1);
            }
            cached = alterMessageCode(code.toString());
        }
        return cached;
    }

    protected String alterSimpleClassName(String simpleClassName) {
        return simpleClassName;
    }

    protected String alterMessageCode(String code) {
        return code;
    }

}
