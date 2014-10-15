/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
