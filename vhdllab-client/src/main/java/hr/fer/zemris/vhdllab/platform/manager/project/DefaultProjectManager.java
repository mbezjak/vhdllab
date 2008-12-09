package hr.fer.zemris.vhdllab.platform.manager.project;

import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.service.ProjectService;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultProjectManager extends
        AbstractEventPublisher<ProjectListener> implements ProjectManager {

    @Autowired
    private ProjectService service;
    @Autowired
    private WorkspaceManager workspaceManager;

    public DefaultProjectManager() {
        super(ProjectListener.class);
    }

    @Override
    public void create(ProjectInfo project)
            throws ProjectAlreadyExistsException {
        checkIfNull(project);
        if (workspaceManager.exist(project)) {
            throw new ProjectAlreadyExistsException(project.toString());
        }
        ProjectInfo created = service.save(project);
        fireProjectCreated(created);
    }

    @Override
    public void delete(ProjectInfo project) {
        checkIfNull(project);
        service.delete(project);
        fireProjectDeleted(project);
    }

    private void fireProjectCreated(ProjectInfo project) {
        for (ProjectListener l : getListeners()) {
            l.projectCreated(project);
        }
    }

    private void fireProjectDeleted(ProjectInfo project) {
        for (ProjectListener l : getListeners()) {
            l.projectDeleted(project);
        }
    }

    private void checkIfNull(ProjectInfo project) {
        Validate.notNull(project, "Project can't be null");
    }

}
