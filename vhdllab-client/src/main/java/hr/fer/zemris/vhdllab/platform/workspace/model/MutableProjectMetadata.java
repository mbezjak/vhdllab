package hr.fer.zemris.vhdllab.platform.workspace.model;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.workspace.ProjectMetadata;
import hr.fer.zemris.vhdllab.entities.FileInfo;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;

public class MutableProjectMetadata extends ProjectMetadata {

    private static final long serialVersionUID = 1L;

    public MutableProjectMetadata(ProjectMetadata project) {
        super(project);
        this.files = new ArrayList<FileInfo>(files);
    }

    @Override
    public List<FileInfo> getFiles() {
        return Collections.unmodifiableList(files);
    }

    public void addFile(FileInfo file) {
        Validate.notNull(file, "File can't be null");
        files.add(file);
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
