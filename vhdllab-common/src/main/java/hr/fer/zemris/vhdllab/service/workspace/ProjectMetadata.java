package hr.fer.zemris.vhdllab.service.workspace;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.util.EntityUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class ProjectMetadata implements Serializable {

    private static final long serialVersionUID = 6024599240022056727L;

    private final Set<File> files;
    private Hierarchy hierarchy;

    @SuppressWarnings("unchecked")
    public ProjectMetadata(Project project) {
        this(new Hierarchy(project, Collections.EMPTY_LIST),
                new HashSet<File>());
    }

    public ProjectMetadata(Hierarchy hierarchy, Set<File> files) {
        Validate.notNull(files, "Files can't be null");
        setHierarchy(hierarchy);
        this.files = EntityUtils.cloneFiles(files);
    }

    public Project getProject() {
        return EntityUtils.lightweightClone(hierarchy.getProject());
    }

    public Set<File> getFiles() {
        return Collections.unmodifiableSet(files);
    }

    public Hierarchy getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Hierarchy hierarchy) {
        Validate.notNull(hierarchy, "Hierarchy can't be null");
        this.hierarchy = hierarchy;
    }

    public void addFile(File file) {
        Validate.notNull(file, "File can't be null");
        File fileToAdd = new File(file);
        fileToAdd.setProject(hierarchy.getProject());
        files.remove(fileToAdd);
        files.add(fileToAdd);
    }

    public void removeFile(File file) {
        Validate.notNull(file, "File can't be null");
        files.remove(file);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        Project project = getProject();
        for (File f : files) {
            f.setProject(project);
        }
    }

}
