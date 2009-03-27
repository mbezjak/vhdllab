package hr.fer.zemris.vhdllab.platform.preference;

import hr.fer.zemris.vhdllab.entity.PreferencesFile;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.service.PreferencesFileService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private PreferencesFileService preferencesFileService;
    @Autowired
    private WorkspaceManager manager;
    private Map<String, PreferencesFile> files;

    @Override
    public PreferencesFile getFile(String name) {
        return getFiles().get(name);
    }

    @Override
    public void setFile(PreferencesFile file) {
        file.setUserId(ApplicationContextHolder.getContext().getUserId());
        getFiles().put(file.getName(), file);
    }

    @Override
    public void saveFiles() {
        LOG.debug("Saving " + getFiles().size() + " user files");
        preferencesFileService.save(new ArrayList<PreferencesFile>(getFiles()
                .values()));
    }

    private Map<String, PreferencesFile> getFiles() {
        if (files == null) {
            List<PreferencesFile> allFiles = manager.getWorkspace()
                    .getPreferencesFiles();
            LOG.debug("Loaded " + allFiles.size() + " preferences files");
            files = new HashMap<String, PreferencesFile>(allFiles.size());
            for (PreferencesFile f : allFiles) {
                files.put(f.getName(), f);
            }
        }
        return files;
    }

}
