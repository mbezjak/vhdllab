package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.ProjectIdentifier;
import hr.fer.zemris.vhdllab.service.workspace.ProjectMetadata;
import hr.fer.zemris.vhdllab.service.workspace.Workspace;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultIdentifierToInfoObjectMapper implements
        IdentifierToInfoObjectMapper {

    @Autowired
    private WorkspaceManager workspaceManager;
    @Autowired
    private LibraryFileService predefinedService;

    private Map<Integer, Project> projectIds;
    private Map<ProjectIdentifier, Project> projectIdentifiers;
    private Map<FileIdentifier, File> fileIdentifiers;

    void addProject(Project project) {
        ProjectIdentifier identifier = asIdentifier(project);
        getProjectIds().put(project.getId(), project);
        getProjectIdentifiers().put(identifier, project);
    }

    void addFile(File file) {
        Project project = getProjectIds().get(file.getProjectId());
        FileIdentifier identifier = asIdentifier(project, file);
        getFileIdentifiers().put(identifier, file);
    }

    void removeProject(Project project) {
        getProjectIds().remove(project.getId());
        getProjectIdentifiers().remove(asIdentifier(project));
    }

    void removeFile(File file) {
        Project project = getProjectIds().get(file.getProjectId());
        getFileIdentifiers().remove(asIdentifier(project, file));
    }

    @Override
    public Project getProject(ProjectIdentifier project) {
        Validate.notNull(project, "Project identifier can't be null");
        return getProjectIdentifiers().get(project);
    }

    @Override
    public Project getProject(Integer projectId) {
        Validate.notNull(projectId, "Project id can't be null");
        return getProjectIds().get(projectId);
    }

    @Override
    public File getFile(FileIdentifier file) {
        Validate.notNull(file, "File identifier can't be null");
        if (getFileIdentifiers().containsKey(file)) {
            return getFileIdentifiers().get(file);
        }
        LibraryFileInfo predefined = predefinedService
                .findPredefinedByName(file.getFileName());
        if (predefined == null) {
            return null;
        }
        Integer projectId = getProject(
                new ProjectIdentifier(file.getProjectName())).getId();
        return new File(FileType.PREDEFINED, file.getFileName(), predefined
                .getData(), projectId);
    }

    private ProjectIdentifier asIdentifier(Project project) {
        Validate.notNull(project, "Project can't be null");
        return new ProjectIdentifier(project.getName());
    }

    private FileIdentifier asIdentifier(Project project, File file) {
        Validate.notNull(project, "Project can't be null");
        Validate.notNull(file, "File can't be null");
        return new FileIdentifier(project.getName(), file.getName());
    }

    private Map<Integer, Project> getProjectIds() {
        if (projectIds == null) {
            initializeIdentifiers();
        }
        return projectIds;
    }

    private Map<ProjectIdentifier, Project> getProjectIdentifiers() {
        if (projectIdentifiers == null) {
            initializeIdentifiers();
        }
        return projectIdentifiers;
    }

    private Map<FileIdentifier, File> getFileIdentifiers() {
        if (fileIdentifiers == null) {
            initializeIdentifiers();
        }
        return fileIdentifiers;
    }

    private void initializeIdentifiers() {
        Workspace workspace = workspaceManager.getWorkspace();
        int projectCount = workspace.getProjectCount();
        projectIds = new HashMap<Integer, Project>(projectCount);
        projectIdentifiers = new HashMap<ProjectIdentifier, Project>(
                projectCount);
        fileIdentifiers = new HashMap<FileIdentifier, File>(projectCount);
        for (ProjectMetadata metadata : workspace) {
            Project project = metadata.getProject();
            addProject(project);
            for (File file : metadata.getFiles()) {
                addFile(file);
            }
        }
    }

}
