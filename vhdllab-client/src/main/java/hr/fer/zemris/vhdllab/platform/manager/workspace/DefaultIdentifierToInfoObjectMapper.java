package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.api.workspace.ProjectMetadata;
import hr.fer.zemris.vhdllab.api.workspace.Workspace;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.LibraryFileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.ProjectIdentifier;
import hr.fer.zemris.vhdllab.service.LibraryFileService;

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

    private Map<Integer, ProjectInfo> projectIds;
    private Map<ProjectIdentifier, ProjectInfo> projectIdentifiers;
    private Map<FileIdentifier, FileInfo> fileIdentifiers;

    void addProject(ProjectInfo project) {
        ProjectIdentifier identifier = asIdentifier(project);
        getProjectIds().put(project.getId(), project);
        getProjectIdentifiers().put(identifier, project);
    }

    void addFile(FileInfo file) {
        ProjectInfo project = getProjectIds().get(file.getProjectId());
        FileIdentifier identifier = asIdentifier(project, file);
        getFileIdentifiers().put(identifier, file);
    }

    void removeProject(ProjectInfo project) {
        getProjectIds().remove(project.getId());
        getProjectIdentifiers().remove(asIdentifier(project));
    }

    void removeFile(FileInfo file) {
        ProjectInfo project = getProjectIds().get(file.getProjectId());
        getFileIdentifiers().remove(asIdentifier(project, file));
    }

    @Override
    public ProjectInfo getProject(ProjectIdentifier project) {
        Validate.notNull(project, "Project identifier can't be null");
        return getProjectIdentifiers().get(project);
    }

    @Override
    public ProjectInfo getProject(Integer projectId) {
        Validate.notNull(projectId, "Project id can't be null");
        return getProjectIds().get(projectId);
    }

    @Override
    public FileInfo getFile(FileIdentifier file) {
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
        return new FileInfo(FileType.PREDEFINED, file.getFileName(), predefined
                .getData(), projectId);
    }

    @Override
    public ProjectIdentifier asIdentifier(ProjectInfo project) {
        Validate.notNull(project, "Project can't be null");
        return new ProjectIdentifier(project.getName());
    }

    @Override
    public FileIdentifier asIdentifier(ProjectInfo project, FileInfo file) {
        Validate.notNull(project, "Project can't be null");
        Validate.notNull(file, "File can't be null");
        return new FileIdentifier(project.getName(), file.getName());
    }

    private Map<Integer, ProjectInfo> getProjectIds() {
        if (projectIds == null) {
            initializeIdentifiers();
        }
        return projectIds;
    }

    private Map<ProjectIdentifier, ProjectInfo> getProjectIdentifiers() {
        if (projectIdentifiers == null) {
            initializeIdentifiers();
        }
        return projectIdentifiers;
    }

    private Map<FileIdentifier, FileInfo> getFileIdentifiers() {
        if (fileIdentifiers == null) {
            initializeIdentifiers();
        }
        return fileIdentifiers;
    }

    private void initializeIdentifiers() {
        Workspace workspace = workspaceManager.getWorkspace();
        int projectCount = workspace.getProjectCount();
        projectIds = new HashMap<Integer, ProjectInfo>(projectCount);
        projectIdentifiers = new HashMap<ProjectIdentifier, ProjectInfo>(
                projectCount);
        fileIdentifiers = new HashMap<FileIdentifier, FileInfo>(projectCount);
        for (ProjectMetadata metadata : workspace) {
            ProjectInfo project = metadata.getProject();
            addProject(project);
            for (FileInfo file : metadata.getFiles()) {
                addFile(file);
            }
        }
    }

}
