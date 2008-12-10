package hr.fer.zemris.vhdllab.platform.support;

import java.util.Locale;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;

public final class UserLocaleInitializer {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(UserLocaleInitializer.class);

    private static final String USER_LANGUAGE = "user.language";
    private static final String USER_COUNTRY = "user.country";
    private static final String USER_VARIANT = "user.variant";

    private static final String DEFAULT_USER_LANGUAGE = "en";
    private static final String DEFAULT_USER_COUNTRY = "US";
    private static final String DEFAULT_USER_VARIANT = "";

    public void initLocale() {
        Preferences preferences = Preferences
                .userNodeForPackage(UserLocaleInitializer.class);
        String language = preferences.get(USER_LANGUAGE, DEFAULT_USER_LANGUAGE);
        String country = preferences.get(USER_COUNTRY, DEFAULT_USER_COUNTRY);
        String variant = preferences.get(USER_VARIANT, DEFAULT_USER_VARIANT);
        language = language != null ? language : "";
        country = country != null ? country : "";
        variant = variant != null ? variant : "";
        Locale locale = new Locale(language, country, variant);
        Locale.setDefault(locale);
        LOG.debug("Default locale is set to '" + locale + "'");
    }
}
