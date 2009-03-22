package hr.fer.zemris.vhdllab.platform.preference;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.service.WorkspaceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultPreferencesManager implements PreferencesManager {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(DefaultPreferencesManager.class);

    @Autowired
    private WorkspaceService service;
    @Autowired
    private WorkspaceManager manager;
    private Map<String, File> files;

    @Override
    public File getFile(String name) {
        return getFiles().get(name);
    }

    @Override
    public void setFile(File file) {
        getFiles().put(file.getName(), file);
    }

    @Override
    public void saveFiles() {
        LOG.debug("Saving " + getFiles().size() + " user files");
        service.savePreferences(new ArrayList<File>(getFiles().values()));
    }

    private Map<String, File> getFiles() {
        if (files == null) {
            Set<File> allFiles = manager.getWorkspace().getPreferencesFiles();
            LOG.debug("Loaded " + allFiles.size() + " user files");
            files = new HashMap<String, File>(allFiles.size());
            for (File f : allFiles) {
                files.put(f.getName().toString(), f);
            }
        }
        return files;
    }

}
