package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.api.workspace.Workspace;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;

import java.util.List;

public interface WorkspaceManager {

    List<Project> getProjects();

    List<File> getFilesForProject(Project project);

    Hierarchy getHierarchy(Project project);

    boolean exist(Project project);

    boolean exist(File file);

    Workspace getWorkspace();

}
