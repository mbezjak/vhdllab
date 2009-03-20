package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.FileDao;
import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class ServiceSupport {

    @Autowired
    private FileDao fileDao;
    @Autowired
    private ProjectDao projectDao;

    protected File loadFile(Integer id) {
        return fileDao.load(id);
    }

    protected Project loadProject(Integer id) {
        return projectDao.load(id);
    }

}
