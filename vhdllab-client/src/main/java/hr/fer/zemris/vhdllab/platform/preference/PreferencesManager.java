package hr.fer.zemris.vhdllab.platform.preference;

import hr.fer.zemris.vhdllab.entity.PreferencesFile;

public interface PreferencesManager {

    PreferencesFile getFile(String name);

    void setFile(PreferencesFile file);

    void saveFiles();

}
