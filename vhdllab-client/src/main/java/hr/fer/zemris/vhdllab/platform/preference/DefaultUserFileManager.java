package hr.fer.zemris.vhdllab.platform.preference;

import hr.fer.zemris.vhdllab.entities.UserFileInfo;
import hr.fer.zemris.vhdllab.service.UserFileService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class DefaultUserFileManager implements UserFileManager {

    @Autowired
    private UserFileService service;
    private Map<String, UserFileInfo> files;

    @Override
    public UserFileInfo getFile(String name) {
        return getFiles().get(name);
    }

    @Override
    public void setFile(UserFileInfo file) {
        getFiles().put(file.getName().toString(), file);
    }

    @Override
    public void saveFiles() {
        service.save(new ArrayList<UserFileInfo>(files.values()));
    }

    private Map<String, UserFileInfo> getFiles() {
        if (files == null) {
            List<UserFileInfo> allFiles = service.findByUser();
            files = new HashMap<String, UserFileInfo>(allFiles.size());
            for (UserFileInfo f : allFiles) {
                files.put(f.getName().toString(), f);
            }
        }
        return files;
    }

}
