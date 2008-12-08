package hr.fer.zemris.vhdllab.api.workspace;

import hr.fer.zemris.vhdllab.entities.ProjectInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;

public class Workspace implements Iterable<ProjectMetadata>, Serializable {

    private static final long serialVersionUID = 1L;

    protected List<ProjectMetadata> projects;
    protected Map<ProjectInfo, ProjectMetadata> map;

    public Workspace(List<ProjectMetadata> projects) {
        this.projects = projects;
        checkProperties();
    }

    protected Workspace() {
    }

    private void checkProperties() {
        Validate.notNull(projects, "Projects can't be null");
        this.projects = new ArrayList<ProjectMetadata>(projects);
        createMap();
    }

    protected void createMap() {
        map = new HashMap<ProjectInfo, ProjectMetadata>(projects.size());
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

    public ProjectMetadata getProjectMetadata(ProjectInfo project) {
        Validate.notNull(project, "Project can't be null");
        ProjectMetadata pm = map.get(project);
        if (pm == null) {
            throw new IllegalArgumentException("No metadata for project: "
                    + project);
        }
        return pm;
    }

    @Override
    public Iterator<ProjectMetadata> iterator() {
        return projects.iterator();
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
