package hr.fer.zemris.vhdllab.api.workspace;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.apache.commons.lang.Validate;

public final class FileSaveReport implements Serializable {

    private static final long serialVersionUID = 1L;

    private final ProjectInfo project;
    private final FileInfo file;
    private final Hierarchy hierarchy;

    public FileSaveReport(ProjectInfo project, FileInfo file,
            Hierarchy hierarchy) {
        this.project = project;
        this.file = file;
        this.hierarchy = hierarchy;
    }

    private void checkProperties() {
        Validate.notNull(project, "Project can't be null");
        Validate.notNull(file, "File can't be null");
        Validate.notNull(hierarchy, "Project hierarchy can't be null");
        if (!hierarchy.getProjectName().equals(project.getName())) {
            throw new IllegalArgumentException("Project " + project
                    + " is not described by hierarchy " + hierarchy);
        }
    }

    public ProjectInfo getProject() {
        return project;
    }

    public FileInfo getFile() {
        return file;
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
