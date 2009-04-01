package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.entity.PreferencesFile;
import hr.fer.zemris.vhdllab.service.PreferencesFileService;

import java.util.List;

import org.apache.commons.lang.Validate;

public class PreferencesFileServiceImpl extends ServiceSupport implements
        PreferencesFileService {

    @Override
    public void save(List<PreferencesFile> files) {
        Validate.notNull(files, "Files can't be null");
        for (PreferencesFile file : files) {
            if (file.isNew()) {
                preferencesFileDao.persist(file);
            } else {
                preferencesFileDao.merge(file);
            }
        }
    }

}
