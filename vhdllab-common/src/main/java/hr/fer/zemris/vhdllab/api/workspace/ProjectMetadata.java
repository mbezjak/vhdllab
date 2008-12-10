package hr.fer.zemris.vhdllab.api.workspace;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;

public class ProjectMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    protected final ProjectInfo project;
    protected List<FileInfo> files;
    protected Hierarchy hierarchy;

    public ProjectMetadata(ProjectInfo project, List<FileInfo> files,
            Hierarchy hierarchy) {
        this.project = project;
        this.files = files;
        this.hierarchy = hierarchy;
        checkProperties();
    }

    public ProjectMetadata(ProjectMetadata metadata) {
        this.project = metadata.project;
        this.files = metadata.files;
        this.hierarchy = metadata.hierarchy;
        checkProperties();
    }

    private void checkProperties() {
        Validate.notNull(project, "Project can't be null");
        Validate.notNull(files, "Files can't be null");
        Validate.notNull(hierarchy, "Hierarchy can't be null");
        this.files = new ArrayList<FileInfo>(files);
    }

    public ProjectInfo getProject() {
        return project;
    }

    public List<FileInfo> getFiles() {
        return Collections.unmodifiableList(files);
    }

    public Hierarchy getHierarchy() {
        return hierarchy;
    }

    /**
     * Ensures that properties are in correct state after deserialization.
     */
    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        checkProperties();
    }

}
