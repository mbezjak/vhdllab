package hr.fer.zemris.vhdllab.platform.manager.project;

import hr.fer.zemris.vhdllab.entities.ProjectInfo;

public interface ProjectManager {

    void create(ProjectInfo project) throws ProjectAlreadyExistsException;

    void delete(ProjectInfo project);

}
