package hr.fer.zemris.vhdllab.platform.workspace;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.workspace.Workspace;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.workspace.model.ProjectIdentifier;

import java.util.List;

public interface WorkspaceManager {

    List<ProjectInfo> getProjects();

    List<FileInfo> getFilesForProject(ProjectInfo project);

    Hierarchy getHierarchy(ProjectIdentifier project);

    Workspace getWorkspace();

}
