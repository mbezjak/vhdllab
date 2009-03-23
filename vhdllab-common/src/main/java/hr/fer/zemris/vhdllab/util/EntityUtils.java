package hr.fer.zemris.vhdllab.util;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class EntityUtils {

    public static Set<File> cloneFiles(Set<File> files) {
        Set<File> clonedFiles = new HashSet<File>(files.size());
        for (File file : files) {
            clonedFiles.add(new File(file));
        }
        return clonedFiles;
    }

    public static List<Project> cloneProjects(List<Project> projects) {
        List<Project> clonedProjects = new ArrayList<Project>(projects.size());
        for (Project project : projects) {
            clonedProjects.add(new Project(project));
        }
        return clonedProjects;
    }

    public static File lightweightClone(File file) {
        File clone = new File(file);
        clone.setProject(null);
        clone.setData(null);
        return clone;
    }

    public static Project lightweightClone(Project project) {
        Project clone = new Project(project);
        clone.setFiles(null);
        return clone;
    }

    public static void setNullFiles(Collection<Project> projects) {
        for (Project project : projects) {
            project.setFiles(null);
        }
    }

}
