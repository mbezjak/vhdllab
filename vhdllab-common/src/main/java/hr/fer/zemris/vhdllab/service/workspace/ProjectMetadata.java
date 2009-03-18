package hr.fer.zemris.vhdllab.service.workspace;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.util.EntityUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

public class ProjectMetadata implements Serializable {

    private static final long serialVersionUID = -5980151344070198073L;

    private final Set<File> files;
    private final Hierarchy hierarchy;

    @SuppressWarnings("unchecked")
    public ProjectMetadata(Project project) {
        this(new Hierarchy(project, Collections.EMPTY_LIST),
                new HashSet<File>());
    }

    public ProjectMetadata(Hierarchy hierarchy, Set<File> files) {
        Validate.notNull(hierarchy, "Hierarchy can't be null");
        Validate.notNull(files, "Files can't be null");
        this.files = EntityUtils.cloneFiles(files);
        this.hierarchy = hierarchy;
    }

    public Project getProject() {
        return EntityUtils.lightweightClone(hierarchy.getProject());
    }

    public Set<File> getFiles() {
        return EntityUtils.setProject(EntityUtils.cloneFiles(files), getProject());
    }

    public Hierarchy getHierarchy() {
        return hierarchy;
    }

    public void addFile(File file) {
        Validate.notNull(file, "File can't be null");
        files.add(new File(file));
    }

    public void removeFile(File file) {
        Validate.notNull(file, "File can't be null");
        files.remove(file);
    }

}
