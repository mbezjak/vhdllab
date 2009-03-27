package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.FileDao;
import hr.fer.zemris.vhdllab.dao.PredefinedFileDao;
import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class ServiceSupport {

    @Autowired
    private FileDao fileDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private PredefinedFileDao predefinedFilesDao;

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
            file.setProject(loadProject(projectId));
        }
        return file;
    }

}
