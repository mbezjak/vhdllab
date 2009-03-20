package hr.fer.zemris.vhdllab.util;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class EntityUtils {

    public static Set<File> cloneFiles(Set<File> files) {
        Set<File> lightweight = new HashSet<File>(files.size());
        for (File file : files) {
            lightweight.add(new File(file));
        }
        return lightweight;
    }

    public static List<Project> cloneProjects(List<Project> projects) {
        List<Project> lightweight = new ArrayList<Project>(projects.size());
        for (Project project : projects) {
            lightweight.add(new Project(project));
        }
        return lightweight;
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

    public static Set<File> setProject(Set<File> files, Project project) {
        for (File file : files) {
            file.setProject(project);
        }
        return files;
    }

    public static List<Project> setNullFiles(List<Project> projects) {
        for (Project project : projects) {
            project.setFiles(null);
        }
        return projects;
    }

}
