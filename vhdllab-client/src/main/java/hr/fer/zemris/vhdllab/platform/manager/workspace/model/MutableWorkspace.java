package hr.fer.zemris.vhdllab.platform.manager.workspace.model;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.hierarchy.HierarchyNode;
import hr.fer.zemris.vhdllab.api.workspace.ProjectMetadata;
import hr.fer.zemris.vhdllab.api.workspace.Workspace;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.lang.Validate;

public class MutableWorkspace extends Workspace {

    private static final long serialVersionUID = 1L;

    public MutableWorkspace(Workspace workspace) {
        super();
        this.projects = new ArrayList<ProjectMetadata>(workspace
                .getProjectCount());
        for (ProjectMetadata mp : workspace) {
            projects.add(new MutableProjectMetadata(mp));
        }
        createMap();
    }

    public void addProject(ProjectInfo project) {
        Validate.notNull(project, "Project can't be null");
        Hierarchy hierarchy = new Hierarchy(project.getName(),
                new HashSet<HierarchyNode>(0));
        ProjectMetadata projectMetadata = new ProjectMetadata(project,
                new ArrayList<FileInfo>(), hierarchy);
        add(projectMetadata);
    }

    private void add(ProjectMetadata projectMetadata) {
        projects.add(projectMetadata);
        map.put(projectMetadata.getProject(), projectMetadata);
    }

    private void readObject(@SuppressWarnings("unused") ObjectInputStream in) {
        throw new UnsupportedOperationException(
                "This object is intended for client application only and doesn't accept serialization");
    }

}
