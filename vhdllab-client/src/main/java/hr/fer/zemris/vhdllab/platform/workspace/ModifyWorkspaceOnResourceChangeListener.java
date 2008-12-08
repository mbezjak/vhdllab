package hr.fer.zemris.vhdllab.platform.workspace;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.applets.main.CommunicatorResourceListener;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.workspace.model.MutableProjectMetadata;
import hr.fer.zemris.vhdllab.platform.workspace.model.MutableWorkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifyWorkspaceOnResourceChangeListener implements
        CommunicatorResourceListener {

    @Autowired
    private WorkspaceManager workspaceManager;

    @Override
    public void projectCreated(ProjectInfo project) {
        getWorkspace().addProject(project);
    }

    @Override
    public void fileCreated(ProjectInfo project, FileInfo file,
            Hierarchy hierarchy) {
        MutableProjectMetadata projectMetadata = (MutableProjectMetadata) getWorkspace()
                .getProjectMetadata(project);
        projectMetadata.addFile(file);
    }

    @Override
    public void fileSaved(ProjectInfo project, FileInfo file,
            Hierarchy hierarchy) {
        MutableProjectMetadata projectMetadata = (MutableProjectMetadata) getWorkspace()
                .getProjectMetadata(project);
        projectMetadata.setHierarchy(hierarchy);
    }

    private MutableWorkspace getWorkspace() {
        return (MutableWorkspace) workspaceManager.getWorkspace();
    }

}
