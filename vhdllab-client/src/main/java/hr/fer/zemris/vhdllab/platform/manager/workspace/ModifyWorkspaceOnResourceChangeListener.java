package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.api.workspace.FileSaveReport;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.manager.file.FileAdapter;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectListener;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.MutableProjectMetadata;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.MutableWorkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifyWorkspaceOnResourceChangeListener extends FileAdapter
        implements ProjectListener {

    @Autowired
    private WorkspaceManager workspaceManager;

    @Override
    public void projectCreated(ProjectInfo project) {
        getWorkspace().addProject(project);
    }

    @Override
    public void projectDeleted(ProjectInfo project) {
    }

    @Override
    public void fileCreated(FileSaveReport report) {
        MutableProjectMetadata projectMetadata = (MutableProjectMetadata) getWorkspace()
                .getProjectMetadata(report.getProject());
        projectMetadata.addFile(report.getFile());
    }

    @Override
    public void fileSaved(FileSaveReport report) {
        MutableProjectMetadata projectMetadata = (MutableProjectMetadata) getWorkspace()
                .getProjectMetadata(report.getProject());
        projectMetadata.setHierarchy(report.getHierarchy());
    }

    private MutableWorkspace getWorkspace() {
        return (MutableWorkspace) workspaceManager.getWorkspace();
    }

}
