package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.FileDao;
import hr.fer.zemris.vhdllab.dao.PredefinedFileDao;
import hr.fer.zemris.vhdllab.dao.PreferencesFileDao;
import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.extractor.MetadataExtractor;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class ServiceSupport {

    @Autowired
    protected FileDao fileDao;
    @Autowired
    protected ProjectDao projectDao;
    @Autowired
    protected PredefinedFileDao predefinedFilesDao;
    @Autowired
    protected PreferencesFileDao preferencesFileDao;
    @Resource(name = "fileTypeBasedMetadataExtractor")
    protected MetadataExtractor metadataExtractor;

    protected File loadFile(Integer id) {
        return fileDao.load(id);
    }

    protected Project loadProject(Integer id) {
        return projectDao.load(id);
    }

    protected File findProjectOrPredefinedFile(Integer projectId, String name) {
        File file = fileDao.findByName(projectId, name);
        if (file == null) {
            file = predefinedFilesDao.findByName(name);
            if (file != null) {
                file.setProject(loadProject(projectId));
            }
        }
        return file;
    }

}
