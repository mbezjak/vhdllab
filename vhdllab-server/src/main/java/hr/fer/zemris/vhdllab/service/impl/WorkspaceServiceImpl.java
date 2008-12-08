package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.workspace.ProjectMetadata;
import hr.fer.zemris.vhdllab.api.workspace.Workspace;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.service.FileService;
import hr.fer.zemris.vhdllab.service.HierarchyExtractor;
import hr.fer.zemris.vhdllab.service.ProjectService;
import hr.fer.zemris.vhdllab.service.WorkspaceService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private FileService fileService;
    @Autowired
    private HierarchyExtractor hierarchyExtractorService;

    @Override
    public Workspace getWorkspace() {
        List<ProjectInfo> projects = projectService.findByUser();
        List<ProjectMetadata> metadata = new ArrayList<ProjectMetadata>(
                projects.size());
        for (ProjectInfo p : projects) {
            Hierarchy hierarchy = hierarchyExtractorService.extract(p);
            List<FileInfo> files = fileService.findByProject(p.getId());
            metadata.add(new ProjectMetadata(p, files, hierarchy));
        }
        return new Workspace(metadata);
    }

}
