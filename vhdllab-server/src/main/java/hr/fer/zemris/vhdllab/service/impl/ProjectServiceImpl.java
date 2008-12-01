package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.service.ProjectService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao dao;

    @Override
    public ProjectInfo save(ProjectInfo project) {
        Project entity;
        if(project.getId() == null) {
            // creating new project
            entity = new Project(project.getUserId(), project.getName());
        } else {
            entity = wrapToEntity(project);
        }
        dao.save(entity);
        return wrapToInfoObject(entity);
    }

    @Override
    public void delete(ProjectInfo project) {
        dao.delete(wrapToEntity(project));
    }

    @Override
    public List<ProjectInfo> findByUser() {
        List<Project> userProjects = dao.findByUser(UserHolder.getUser());
        List<ProjectInfo> infoProjects = new ArrayList<ProjectInfo>(
                userProjects.size());
        for (Project project : userProjects) {
            infoProjects.add(wrapToInfoObject(project));
        }
        return infoProjects;
    }

    private Project wrapToEntity(ProjectInfo info) {
        return dao.load(info.getId());
    }

    private ProjectInfo wrapToInfoObject(Project project) {
        return project == null ? null : new ProjectInfo(project);
    }
}
