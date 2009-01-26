package hr.fer.zemris.vhdllab.platform.i18n;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public abstract class LocalizationSupport extends AbstractLocalizationSource {

    protected static final char PART_SEPARATOR = '.';

    private String cached;

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
