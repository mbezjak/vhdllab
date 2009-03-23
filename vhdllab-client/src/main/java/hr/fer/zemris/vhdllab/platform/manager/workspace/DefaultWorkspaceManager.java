package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSource;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.service.WorkspaceService;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.workspace.FileReport;
import hr.fer.zemris.vhdllab.service.workspace.Workspace;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultWorkspaceManager extends
        AbstractEventPublisher<WorkspaceListener> implements WorkspaceManager {

    private static final String FILE_CREATED_MESSAGE = "notification.file.created";
    private static final String FILE_SAVED_MESSAGE = "notification.file.saved";
    private static final String FILE_DELETED_MESSAGE = "notification.file.deleted";
    private static final String PROJECT_CREATED_MESSAGE = "notification.project.created";
    private static final String PROJECT_DELETED_MESSAGE = "notification.project.deleted";

    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private EditorManagerFactory editorManagerFactory;
    @Resource(name = "standaloneLocalizationSource")
    private LocalizationSource localizationSource;

    private Workspace workspace;

    public DefaultWorkspaceManager() {
        super(WorkspaceListener.class);
    }

    @Override
    public void create(File file) throws FileAlreadyExistsException {
        checkIfNull(file);
        if (exist(file)) {
            throw new FileAlreadyExistsException(file.toString());
        }
        FileReport report = workspaceService.save(file);
        getWorkspace().addFile(report.getFile(), report.getHierarchy());
        fireFileCreated(report);
        openEditor(report.getFile());
        log(report, FILE_CREATED_MESSAGE);
    }

    @Override
    public void save(File file) {
        checkIfNull(file);
        FileReport report = workspaceService.save(file);
        getWorkspace().addFile(report.getFile(), report.getHierarchy());
        fireFileSaved(report);
        log(report, FILE_SAVED_MESSAGE);
    }

    @Override
    public void delete(File file) {
        checkIfNull(file);
        if (!file.getType().equals(FileType.PREDEFINED)) {
            closeEditor(file);
            FileReport report = workspaceService.deleteFile(file.getId());
            getWorkspace().removeFile(report.getFile(), report.getHierarchy());
            fireFileDeleted(report);
            log(report, FILE_DELETED_MESSAGE);
        }
    }

    private void fireFileCreated(FileReport report) {
        for (WorkspaceListener l : getListeners()) {
            l.fileCreated(report);
        }
    }

    private void fireFileSaved(FileReport report) {
        for (WorkspaceListener l : getListeners()) {
            l.fileSaved(report);
        }
    }

    private void fireFileDeleted(FileReport report) {
        for (WorkspaceListener l : getListeners()) {
            l.fileDeleted(report);
        }
    }

    private void openEditor(File file) {
        editorManagerFactory.get(file).open();
    }

    private void closeEditor(File file) {
        EditorManager em = editorManagerFactory.get(file);
        if (em.isOpened()) {
            em.close(false);
        }
    }

    private void log(FileReport report, String code) {
        File file = report.getFile();
        String fileName = file.getName();
        String projectName = file.getProject().getName();
        logger.info(localizationSource.getMessage(code, new Object[] {
                fileName, projectName }));
    }

    private void checkIfNull(File file) {
        Validate.notNull(file, "File can't be null");
    }

    @Override
    public void create(Project project) throws ProjectAlreadyExistsException {
        checkIfNull(project);
        if (exist(project)) {
            throw new ProjectAlreadyExistsException(project.toString());
        }
        Project created = workspaceService.persist(project.getName());
        getWorkspace().addProject(project);
        fireProjectCreated(created);
        log(project, PROJECT_CREATED_MESSAGE);
    }

    @Override
    public void delete(Project project) {
        checkIfNull(project);
        workspaceService.deleteProject(project.getId());
        getWorkspace().removeProject(project);
        fireProjectDeleted(project);
        log(project, PROJECT_DELETED_MESSAGE);
    }

    private void fireProjectCreated(Project project) {
        for (WorkspaceListener l : getListeners()) {
            l.projectCreated(project);
        }
    }

    private void fireProjectDeleted(Project project) {
        for (WorkspaceListener l : getListeners()) {
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

    @Override
    public List<Project> getProjects() {
        return getWorkspace().getProjects();
    }

    @Override
    public Set<File> getFilesForProject(Project project) {
        return getWorkspace().getFiles(project);
    }

    @Override
    public Hierarchy getHierarchy(Project project) {
        return getWorkspace().getHierarchy(project);
    }

    @Override
    public boolean exist(Project project) {
        return getProjects().contains(project);
    }

    @Override
    public boolean exist(File file) {
        return getFilesForProject(file.getProject()).contains(file);
    }

    @Override
    public Workspace getWorkspace() {
        if (workspace == null) {
            workspace = workspaceService.getWorkspace();
        }
        return workspace;
    }

}
