package hr.fer.zemris.vhdllab.platform.manager.project;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSource;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.service.WorkspaceService;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultProjectManager extends
        AbstractEventPublisher<ProjectListener> implements ProjectManager {

    private static final String PROJECT_CREATED_MESSAGE = "notification.project.created";
    private static final String PROJECT_DELETED_MESSAGE = "notification.project.deleted";

    @Autowired
    private WorkspaceService service;
    @Autowired
    private WorkspaceManager workspaceManager;
    @Resource(name = "standaloneLocalizationSource")
    private LocalizationSource localizationSource;

    public DefaultProjectManager() {
        super(ProjectListener.class);
    }

    @Override
    public void create(Project project)
            throws ProjectAlreadyExistsException {
        checkIfNull(project);
        if (workspaceManager.exist(project)) {
            throw new ProjectAlreadyExistsException(project.toString());
        }
        Project created = service.save(project);
        fireProjectCreated(created);
        log(project, PROJECT_CREATED_MESSAGE);
    }

    @Override
    public void delete(Project project) {
        checkIfNull(project);
        service.delete(project);
        fireProjectDeleted(project);
        log(project, PROJECT_DELETED_MESSAGE);
    }

    private void fireProjectCreated(Project project) {
        for (ProjectListener l : getListeners()) {
            l.projectCreated(project);
        }
    }

    private void fireProjectDeleted(Project project) {
        for (ProjectListener l : getListeners()) {
            l.projectDeleted(project);
        }
    }

    private void log(Project project, String code) {
        logger.info(localizationSource.getMessage(code, new Object[] { project
                .getName() }));
    }

    private void checkIfNull(Project project) {
        Validate.notNull(project, "Project can't be null");
    }

}
