package hr.fer.zemris.vhdllab.platform.preference;

import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

public class DatabaseBackedPreferencesFactory implements PreferencesFactory {

    private final Preferences userRoot = new DatabasePreferences(null, "");

    @Override
    public Preferences systemRoot() {
        throw new UnsupportedOperationException(
                "System preferences is not supported. Use user preferences instead.");
    }

    @Override
    public Preferences userRoot() {
        return userRoot;
    }

}
