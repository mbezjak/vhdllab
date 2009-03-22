package hr.fer.zemris.vhdllab.platform.preference;

import hr.fer.zemris.vhdllab.entity.File;

public interface PreferencesManager {

    File getFile(String name);

    void setFile(File file);

    void saveFiles();

}
