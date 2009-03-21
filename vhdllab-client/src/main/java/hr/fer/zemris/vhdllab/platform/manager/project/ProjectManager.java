package hr.fer.zemris.vhdllab.platform.manager.project;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;

public interface ProjectManager extends EventPublisher<ProjectListener> {

    void create(Project project) throws ProjectAlreadyExistsException;

    void delete(Project project);

}
