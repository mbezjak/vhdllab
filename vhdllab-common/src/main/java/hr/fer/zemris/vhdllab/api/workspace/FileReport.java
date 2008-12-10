package hr.fer.zemris.vhdllab.api.workspace;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.entities.FileInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.apache.commons.lang.Validate;

public final class FileReport implements Serializable {

    private static final long serialVersionUID = 1L;

    private final FileInfo file;
    private final Hierarchy hierarchy;

    public FileReport(FileInfo file, Hierarchy hierarchy) {
        this.file = file;
        this.hierarchy = hierarchy;
        checkProperties();
    }

    private void checkProperties() {
        Validate.notNull(file, "File can't be null");
        Validate.notNull(hierarchy, "Project hierarchy can't be null");
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
