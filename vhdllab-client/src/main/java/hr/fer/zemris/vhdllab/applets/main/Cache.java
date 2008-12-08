package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.UserFileInfo;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    private final Map<Caseless, UserFileInfo> userFileIdentifiers;

    public Cache() {
        userFileIdentifiers = new HashMap<Caseless, UserFileInfo>();
    }

    public UserFileInfo getUserFile(Caseless name) {
        return userFileIdentifiers.get(name);
    }

    public void cacheUserFileItem(Caseless name, UserFileInfo file) {
        if (name == null) {
            throw new NullPointerException("Name can not be null.");
        }
        if (file == null) {
            throw new NullPointerException("User file can not be null.");
        }
        userFileIdentifiers.put(name, file);
    }

    public void removeUserFileItem(FileType type) {
        if (type == null) {
            throw new NullPointerException("Type can not be null.");
        }
        userFileIdentifiers.remove(type);
    }

}