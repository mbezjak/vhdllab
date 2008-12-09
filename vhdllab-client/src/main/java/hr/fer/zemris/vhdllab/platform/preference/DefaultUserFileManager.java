package hr.fer.zemris.vhdllab.platform.preference;

import hr.fer.zemris.vhdllab.entities.UserFileInfo;
import hr.fer.zemris.vhdllab.service.UserFileService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class DefaultUserFileManager implements UserFileManager {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(DefaultUserFileManager.class);

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
        LOG.debug("Saving " + files.size() + " user files");
        service.save(new ArrayList<UserFileInfo>(files.values()));
    }

    private Map<String, UserFileInfo> getFiles() {
        if (files == null) {
            List<UserFileInfo> allFiles = service.findByUser();
            LOG.debug("Loaded " + allFiles.size() + " user files");
            files = new HashMap<String, UserFileInfo>(allFiles.size());
            for (UserFileInfo f : allFiles) {
                files.put(f.getName().toString(), f);
            }
        }
        return files;
    }

}
