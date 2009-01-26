package hr.fer.zemris.vhdllab.platform.manager.project;

import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;

public interface ProjectManager extends EventPublisher<ProjectListener> {

    void create(ProjectInfo project) throws ProjectAlreadyExistsException;

    void delete(ProjectInfo project);

}
