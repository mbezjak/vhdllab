package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.workspace.Workspace;

public interface ProjectService {

    Project persist(Project project);

    void delete(Project project);

    Hierarchy extractHierarchy(Project project);

    Workspace getWorkspace();

}
