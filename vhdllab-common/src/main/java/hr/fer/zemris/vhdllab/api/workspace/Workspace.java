package hr.fer.zemris.vhdllab.api.workspace;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.workspace.ProjectMetadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;

public class Workspace implements Serializable {

    private static final long serialVersionUID = 1L;

    protected List<ProjectMetadata> projects;
    protected Map<Project, ProjectMetadata> map;
    private Project preferencesProject;
    private List<File> predefinedFiles;

    public Workspace(List<ProjectMetadata> projects) {
        this.projects = projects;
        checkProperties();
    }

    private void checkProperties() {
        Validate.notNull(projects, "Projects can't be null");
        this.projects = new ArrayList<ProjectMetadata>(projects);
        createMap();
    }

    protected void createMap() {
        map = new HashMap<Project, ProjectMetadata>(projects.size());
        for (ProjectMetadata pm : projects) {
            map.put(pm.getProject(), pm);
        }
    }

    public int getProjectCount() {
        return projects.size();
    }

    public List<ProjectMetadata> getProjectMetadata() {
        return Collections.unmodifiableList(projects);
    }

    public ProjectMetadata getProjectMetadata(Project project) {
        Validate.notNull(project, "Project can't be null");
        ProjectMetadata pm = map.get(project);
        if (pm == null) {
            throw new IllegalArgumentException("No metadata for project: "
                    + project);
        }
        return pm;
    }

    public boolean contains(Project project) {
        Validate.notNull(project, "Project can't be null");
        return map.containsKey(project);
    }

}
