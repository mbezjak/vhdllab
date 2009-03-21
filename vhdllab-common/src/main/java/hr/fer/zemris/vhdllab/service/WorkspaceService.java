package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.workspace.FileReport;
import hr.fer.zemris.vhdllab.service.workspace.Workspace;

import java.util.List;

public interface WorkspaceService {

    FileReport save(File file);

    FileReport deleteFile(Integer fileId);

    File findByName(Integer projectId, String name);

    Project persist(String name);

    void deleteProject(Integer projectId);

    Hierarchy extractHierarchy(Integer projectId);

    Workspace getWorkspace();

    void savePreferences(List<File> files);

}
