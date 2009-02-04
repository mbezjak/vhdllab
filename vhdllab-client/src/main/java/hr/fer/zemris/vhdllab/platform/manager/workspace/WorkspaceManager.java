package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.workspace.Workspace;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;

import java.util.List;

public interface WorkspaceManager {

    List<ProjectInfo> getProjects();

    List<FileInfo> getFilesForProject(ProjectInfo project);

    Hierarchy getHierarchy(ProjectInfo project);

    boolean exist(ProjectInfo project);

    boolean exist(FileInfo file);

    Workspace getWorkspace();

}
