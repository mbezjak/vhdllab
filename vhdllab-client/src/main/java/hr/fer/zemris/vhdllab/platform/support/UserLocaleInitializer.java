/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
