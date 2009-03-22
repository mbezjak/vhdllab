package hr.fer.zemris.vhdllab.platform.manager.workspace.model;

import hr.fer.zemris.vhdllab.api.workspace.Workspace;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.workspace.ProjectMetadata;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

public class MutableWorkspace extends Workspace {

    private static final long serialVersionUID = 1L;

    public MutableWorkspace(Workspace workspace) {
        super(createProjects(workspace));
    }

    private static List<ProjectMetadata> createProjects(Workspace workspace) {
        List<ProjectMetadata> metadata = new ArrayList<ProjectMetadata>(
                workspace.getProjectCount());
        for (ProjectMetadata mp : workspace) {
            metadata.add(new MutableProjectMetadata(mp));
        }
        return metadata;
    }

    public void addProject(Project project) {
        Validate.notNull(project, "Project can't be null");
        ProjectMetadata projectMetadata = new MutableProjectMetadata(project);
        add(projectMetadata);
    }

    public void removeProject(Project project) {
        Validate.notNull(project, "Project can't be null");
        ProjectMetadata projectMetadata = getProjectMetadata(project);
        remove(projectMetadata);
    }

    private void add(ProjectMetadata projectMetadata) {
        projects.add(projectMetadata);
        map.put(projectMetadata.getProject(), projectMetadata);
    }

    private void remove(ProjectMetadata projectMetadata) {
        projects.remove(projectMetadata);
        map.remove(projectMetadata.getProject());
    }

    private void readObject(@SuppressWarnings("unused") ObjectInputStream in) {
        throw new UnsupportedOperationException(
                "This object is intended for client application only and doesn't accept serialization");
    }

}
