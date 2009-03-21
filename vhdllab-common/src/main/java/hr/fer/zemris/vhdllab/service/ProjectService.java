package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.workspace.Workspace;

import java.util.List;

public interface ProjectService {

    Project persist(Project project);

    void delete(Integer projectId);

    Hierarchy extractHierarchy(Integer projectId);

    Workspace getWorkspace();

    void savePreferences(List<File> files);

}
