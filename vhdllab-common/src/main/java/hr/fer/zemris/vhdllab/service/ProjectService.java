package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.workspace.ProjectMetadata;
import hr.fer.zemris.vhdllab.api.workspace.Workspace;
import hr.fer.zemris.vhdllab.entity.Project;

public interface ProjectService {

    Project save(Project project);

    void delete(Project project);

    ProjectMetadata getProjectMetadata(Project project);

    Workspace getWorkspace();

}
