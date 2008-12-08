package hr.fer.zemris.vhdllab.platform.workspace;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.workspace.ProjectMetadata;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.workspace.model.MutableWorkspace;
import hr.fer.zemris.vhdllab.platform.workspace.model.ProjectIdentifier;
import hr.fer.zemris.vhdllab.service.WorkspaceService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultWorkspaceManager implements WorkspaceManager {

    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    private MutableWorkspace workspace;

    @Override
    public List<ProjectInfo> getProjects() {
        List<ProjectMetadata> projectMetadata = getWorkspace()
                .getProjectMetadata();
        List<ProjectInfo> projects = new ArrayList<ProjectInfo>(projectMetadata
                .size());
        for (ProjectMetadata pm : projectMetadata) {
            projects.add(pm.getProject());
        }
        return projects;
    }

    @Override
    public List<FileInfo> getFilesForProject(ProjectInfo project) {
        Validate.notNull(project, "Project can't be null");
        return getWorkspace().getProjectMetadata(project).getFiles();
    }

    @Override
    public Hierarchy getHierarchy(ProjectIdentifier project) {
        Validate.notNull(project, "Project identifier can't be null");
        return getWorkspace().getProjectMetadata(mapper.getProject(project))
                .getHierarchy();
    }

    @Override
    public MutableWorkspace getWorkspace() {
        if (workspace == null) {
            workspace = new MutableWorkspace(workspaceService.getWorkspace());
        }
        return workspace;
    }

}
