package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.MutableProjectMetadata;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.MutableWorkspace;
import hr.fer.zemris.vhdllab.service.workspace.FileReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifyWorkspaceOnResourceChangeListener implements
        WorkspaceListener {

    @Autowired
    private WorkspaceManager workspaceManager;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;

    @Override
    public void projectCreated(Project project) {
        getWorkspace().addProject(project);
    }

    @Override
    public void projectDeleted(Project project) {
        getWorkspace().removeProject(project);
    }

    @Override
    public void fileCreated(FileReport report) {
        Project project = mapper.getProject(report.getFile().getProjectId());
        MutableProjectMetadata projectMetadata = (MutableProjectMetadata) getWorkspace()
                .getProjectMetadata(project);
        projectMetadata.addFile(report.getFile());
        projectMetadata.setHierarchy(report.getHierarchy());
    }

    @Override
    public void fileSaved(FileReport report) {
        Project project = mapper.getProject(report.getFile().getProjectId());
        MutableProjectMetadata projectMetadata = (MutableProjectMetadata) getWorkspace()
                .getProjectMetadata(project);
        projectMetadata.addFile(report.getFile());
        projectMetadata.setHierarchy(report.getHierarchy());
    }

    @Override
    public void fileDeleted(FileReport report) {
        Project project = mapper.getProject(report.getFile().getProjectId());
        MutableProjectMetadata projectMetadata = (MutableProjectMetadata) getWorkspace()
                .getProjectMetadata(project);
        projectMetadata.removeFile(report.getFile());
        projectMetadata.setHierarchy(report.getHierarchy());
    }

    private MutableWorkspace getWorkspace() {
        return (MutableWorkspace) workspaceManager.getWorkspace();
    }

}
