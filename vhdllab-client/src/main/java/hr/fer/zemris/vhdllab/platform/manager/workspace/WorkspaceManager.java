package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.workspace.Workspace;

import java.util.List;

public interface WorkspaceManager extends EventPublisher<WorkspaceListener> {

    void create(File file) throws FileAlreadyExistsException;

    void save(File file);

    void delete(File file);

    void create(Project project) throws ProjectAlreadyExistsException;

    void delete(Project project);

    List<Project> getProjects();

    List<File> getFilesForProject(Project project);

    Hierarchy getHierarchy(Project project);

    boolean exist(Project project);

    boolean exist(File file);

    Workspace getWorkspace();

}
