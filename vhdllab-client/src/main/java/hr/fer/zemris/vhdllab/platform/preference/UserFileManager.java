package hr.fer.zemris.vhdllab.platform.preference;

import hr.fer.zemris.vhdllab.entities.UserFileInfo;

interface UserFileManager {

    UserFileInfo getFile(String name);

    void setFile(UserFileInfo file);

    void saveFiles();

}
