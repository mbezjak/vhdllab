package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSource;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.MutableWorkspace;
import hr.fer.zemris.vhdllab.service.WorkspaceService;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.workspace.FileReport;

import java.util.ArrayList;
import java.util.List;

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
    private IdentifierToInfoObjectMapper mapper;
    @Autowired
    private EditorManagerFactory editorManagerFactory;
    @Resource(name = "standaloneLocalizationSource")
    private LocalizationSource localizationSource;

    private MutableWorkspace workspace;

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
        fireFileCreated(report);
        openEditor(report.getFile());
        Project project = mapper.getProject(report.getFile().getProjectId());
        log(report, project, FILE_CREATED_MESSAGE);
    }

    @Override
    public void save(File file) {
        checkIfNull(file);
        FileReport report = workspaceService.save(file);
        fireFileSaved(report);
        Project project = mapper.getProject(report.getFile().getProjectId());
        log(report, project, FILE_SAVED_MESSAGE);
    }

    @Override
    public void delete(File file) {
        checkIfNull(file);
        if (!file.getType().equals(FileType.PREDEFINED)) {
            closeEditor(file);
            Project project = mapper.getProject(file.getProjectId());
            FileReport report = workspaceService.deleteFile(file.getId());
            fireFileDeleted(report);
            log(report, project, FILE_DELETED_MESSAGE);
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

    private void log(FileReport report, Project project, String code) {
        logger.info(localizationSource.getMessage(code, new Object[] {
                report.getFile().getName(), project.getName() }));
    }

    private void checkIfNull(File file) {
        Validate.notNull(file, "File can't be null");
    }
    
    @Override
    public void create(Project project)
            throws ProjectAlreadyExistsException {
        checkIfNull(project);
        if (exist(project)) {
            throw new ProjectAlreadyExistsException(project.toString());
        }
        Project created = workspaceService.persist(project.getName());
        fireProjectCreated(created);
        log(project, PROJECT_CREATED_MESSAGE);
    }

    @Override
    public void delete(Project project) {
        checkIfNull(project);
        workspaceService.deleteProject(project.getId());
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
        List<ProjectMetadata> projectMetadata = getWorkspace()
                .getProjectMetadata();
        List<Project> projects = new ArrayList<Project>(projectMetadata.size());
        for (ProjectMetadata pm : projectMetadata) {
            projects.add(pm.getProject());
        }
        return projects;
    }

    @Override
    public List<File> getFilesForProject(Project project) {
        Validate.notNull(project, "Project can't be null");
        return getWorkspace().getProjectMetadata(project).getFiles();
    }

    @Override
    public Hierarchy getHierarchy(Project project) {
        return getWorkspace().getProjectMetadata(project).getHierarchy();
    }

    @Override
    public boolean exist(Project project) {
        return getWorkspace().contains(project);
    }

    @Override
    public boolean exist(File file) {
        Project project = mapper.getProject(file.getProjectId());
        ProjectMetadata metadata = getWorkspace().getProjectMetadata(project);
        return metadata.getFiles().contains(file);
    }

    @Override
    public MutableWorkspace getWorkspace() {
        if (workspace == null) {
            workspace = new MutableWorkspace(workspaceService.getWorkspace());
        }
        return workspace;
    }

}
