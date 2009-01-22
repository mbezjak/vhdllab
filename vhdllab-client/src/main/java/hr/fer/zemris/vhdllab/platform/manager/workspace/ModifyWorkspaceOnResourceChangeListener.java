package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.api.workspace.FileReport;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.manager.file.FileListener;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectListener;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.MutableProjectMetadata;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.MutableWorkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifyWorkspaceOnResourceChangeListener implements
        ProjectListener, FileListener {

    @Autowired
    private WorkspaceManager workspaceManager;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;

    @Override
    public void projectCreated(ProjectInfo project) {
        getWorkspace().addProject(project);
    }

    @Override
    public void projectDeleted(ProjectInfo project) {
        getWorkspace().removeProject(project);
    }

    @Override
    public void fileCreated(FileReport report) {
        ProjectInfo project = mapper
                .getProject(report.getFile().getProjectId());
        MutableProjectMetadata projectMetadata = (MutableProjectMetadata) getWorkspace()
                .getProjectMetadata(project);
        projectMetadata.addFile(report.getFile());
        projectMetadata.setHierarchy(report.getHierarchy());
    }

    @Override
    public void fileSaved(FileReport report) {
        ProjectInfo project = mapper
                .getProject(report.getFile().getProjectId());
        MutableProjectMetadata projectMetadata = (MutableProjectMetadata) getWorkspace()
                .getProjectMetadata(project);
        projectMetadata.addFile(report.getFile());
        projectMetadata.setHierarchy(report.getHierarchy());
    }

    @Override
    public void fileDeleted(FileReport report) {
        ProjectInfo project = mapper
                .getProject(report.getFile().getProjectId());
        MutableProjectMetadata projectMetadata = (MutableProjectMetadata) getWorkspace()
                .getProjectMetadata(project);
        projectMetadata.removeFile(report.getFile());
        projectMetadata.setHierarchy(report.getHierarchy());
    }

    private MutableWorkspace getWorkspace() {
        return (MutableWorkspace) workspaceManager.getWorkspace();
    }

}
