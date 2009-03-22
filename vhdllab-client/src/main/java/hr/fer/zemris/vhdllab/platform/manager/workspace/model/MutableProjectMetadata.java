package hr.fer.zemris.vhdllab.platform.manager.workspace.model;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.hierarchy.HierarchyNode;
import hr.fer.zemris.vhdllab.service.workspace.ProjectMetadata;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.lang.Validate;

public class MutableProjectMetadata extends ProjectMetadata {

    private static final long serialVersionUID = 1L;

    public MutableProjectMetadata(Project project) {
        super(project, new ArrayList<File>(), emptyHierarchy(project));
    }

    public MutableProjectMetadata(ProjectMetadata project) {
        super(project);
    }

    private static Hierarchy emptyHierarchy(Project project) {
        return new Hierarchy(project.getName(), new HashSet<HierarchyNode>(0));
    }

    @Override
    public void addFile(File file) {
        Validate.notNull(file, "File can't be null");
        int index = files.indexOf(file);
        if(index != -1) {
            files.set(index, file);
        } else {
            files.add(file);
        }
    }

    @Override
    public void removeFile(File file) {
        Validate.notNull(file, "File can't be null");
        files.remove(file);
    }

    public void setHierarchy(Hierarchy h) {
        Validate.notNull(h, "Hierarchy can't be null");
        if (!project.getName().equals(h.getProjectName())) {
            throw new IllegalArgumentException("Project " + project
                    + " is not described by hierarchy " + hierarchy);
        }
        this.hierarchy = h;
    }

    private void readObject(@SuppressWarnings("unused") ObjectInputStream in) {
        throw new UnsupportedOperationException(
                "This object is intended for client application only and doesn't accept serialization");
    }

}
