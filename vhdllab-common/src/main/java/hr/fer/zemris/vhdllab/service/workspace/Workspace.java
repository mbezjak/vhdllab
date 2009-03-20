package hr.fer.zemris.vhdllab.service.workspace;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.util.EntityUtils;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;

public final class Workspace {

    private final List<Project> projects;
    private final Hierarchy activeProjectHierarchy;
    private final Project predefinedProject;
    private final Set<File> preferencesFiles;

    public Workspace(List<Project> projects, Hierarchy activeProjectHierarchy,
            Project predefinedProject, Set<File> preferencesFiles) {
        Validate.notNull(projects, "Projects can't be null");
        Validate.notNull(predefinedProject, "Predefined project can't be null");
        Validate.notNull(preferencesFiles, "Preferences files can't be null");
        this.projects = EntityUtils.setNullFiles(EntityUtils
                .cloneProjects(projects));
        this.activeProjectHierarchy = activeProjectHierarchy;
        this.predefinedProject = new Project(predefinedProject);
        this.predefinedProject.setFiles(EntityUtils
                .cloneFiles(predefinedProject.getFiles()));
        this.preferencesFiles = EntityUtils.cloneFiles(preferencesFiles);
    }

    public int getProjectCount() {
        return projects.size();
    }

    public List<Project> getProjects() {
        return projects;
    }

    public Hierarchy getActiveProjectHierarchy() {
        return activeProjectHierarchy;
    }

    public Project getPredefinedProject() {
        return predefinedProject;
    }

    public Set<File> getPreferencesFiles() {
        return preferencesFiles;
    }

}
