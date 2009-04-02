package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.workspace.Workspace;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

public interface WorkspaceManager extends EventPublisher<WorkspaceListener> {

    void create(File file) throws FileAlreadyExistsException;

    void save(File file);

    void delete(File file);

    void create(Project project) throws ProjectAlreadyExistsException;

    void delete(Project project);

    List<Project> getProjects();

    List<Project> getProjects(Predicate filter, Transformer transformer);

    Set<File> getFilesForProject(Project project);

    Set<File> getFilesForProject(Project project, Predicate filter,
            Transformer transformer);

    boolean isEmpty(Project project, Predicate filter);

    Hierarchy getHierarchy(Project project);

    boolean exist(Project project);

    boolean exist(File file);

    Workspace getWorkspace();

}
