package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.MutableWorkspace;
import hr.fer.zemris.vhdllab.service.WorkspaceService;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.workspace.ProjectMetadata;

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
    public List<Project> getProjects() {
        List<ProjectMetadata> projectMetadata = getWorkspace()
                .getProjectMetadata();
        List<Project> projects = new ArrayList<Project>(projectMetadata
                .size());
        for (ProjectMetadata pm : projectMetadata) {
            projects.add(pm.getProject());
        }
        return projects;
    }

    @Override
    public List<File> getFilesForProject(Project project) {
        Validate.notNull(project, "Project can't be null");
        return getWorkspace().getProjectMetadata(project).getFiles();
    }

    @Override
    public Hierarchy getHierarchy(Project project) {
        return getWorkspace().getProjectMetadata(project).getHierarchy();
    }

    @Override
    public boolean exist(Project project) {
        return getWorkspace().contains(project);
    }

    @Override
    public boolean exist(File file) {
        Project project = mapper.getProject(file.getProjectId());
        ProjectMetadata metadata = getWorkspace().getProjectMetadata(project);
        return metadata.getFiles().contains(file);
    }

    @Override
    public MutableWorkspace getWorkspace() {
        if (workspace == null) {
            workspace = new MutableWorkspace(workspaceService.getWorkspace());
        }
        return workspace;
    }

}
