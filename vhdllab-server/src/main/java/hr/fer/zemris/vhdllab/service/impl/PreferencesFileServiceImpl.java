package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.PreferencesFileDao;
import hr.fer.zemris.vhdllab.entity.PreferencesFile;
import hr.fer.zemris.vhdllab.service.PreferencesFileService;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;

public class PreferencesFileServiceImpl extends ServiceSupport implements
        PreferencesFileService {

    @Autowired
    private PreferencesFileDao dao;

    @Override
    public void save(List<PreferencesFile> files) {
        Validate.notNull(files, "Files can't be null");
        for (PreferencesFile file : files) {
            if (file.isNew()) {
                dao.persist(file);
            } else {
                dao.merge(file);
            }
        }
    }
}
