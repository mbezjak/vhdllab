package hr.fer.zemris.vhdllab.platform.preference;

import hr.fer.zemris.vhdllab.entity.File;

public interface UserFileManager {

    File getFile(String name);

    void setFile(File file);

    void saveFiles();

}
