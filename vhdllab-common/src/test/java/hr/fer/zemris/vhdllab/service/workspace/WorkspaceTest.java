package hr.fer.zemris.vhdllab.service.workspace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.hierarchy.HierarchyNode;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

public class WorkspaceTest extends ValueObjectTestSupport {

    private List<Project> projects;
    private Hierarchy hierarchy;
    private Set<File> predefinedFiles;
    private Set<File> preferencesFiles;

    @Before
    public void initObject() {
        projects = new ArrayList<Project>(2);
        Project firstProject = new Project("userId", "project1");
        projects.add(firstProject);
        projects.add(new Project("userId", "project2"));

        hierarchy = new Hierarchy(firstProject, new ArrayList<HierarchyNode>());
        predefinedFiles = new HashSet<File>(1);
        predefinedFiles.add(new File("predefined", null, "data"));
        preferencesFiles = new HashSet<File>(2);
        preferencesFiles.add(new File("preferences1", null, "data"));
        preferencesFiles.add(new File("preferences2", null, "data2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullProjects() {
        new Workspace(null, hierarchy, predefinedFiles, preferencesFiles);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullPredefinedProject() {
        new Workspace(projects, hierarchy, null, preferencesFiles);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullPreferencesFiles() {
        new Workspace(projects, hierarchy, predefinedFiles, null);
    }

    @Test
    public void constructor() {
        Workspace workspace = new Workspace(projects, hierarchy,
                predefinedFiles, preferencesFiles);
        assertEquals(projects, workspace.getProjects());
        assertNotSame(projects, workspace.getProjects());
        assertEquals(projects.size(), workspace.getProjectCount());
        Project project = (Project) CollectionUtils.get(
                workspace.getProjects(), 0);
        assertNull(project.getFiles());
        projects.add(new Project());
        assertEquals(2, workspace.getProjectCount());

        assertEquals(hierarchy, workspace.getActiveProjectHierarchy());

        assertEquals(predefinedFiles.size(), workspace.getPredefinedFiles()
                .size());
        File file = (File) CollectionUtils.get(workspace.getPredefinedFiles(),
                0);
        assertNull(file.getProject());
        file = (File) CollectionUtils.get(predefinedFiles, 0);
        file.setName("new_name");
        file = (File) CollectionUtils.get(workspace.getPredefinedFiles(), 0);
        assertEquals("predefined", file.getName());

        file = (File) CollectionUtils.get(preferencesFiles, 0);
        file.setName("new_name");
        file = (File) CollectionUtils.get(workspace.getPreferencesFiles(), 0);
        assertEquals("preferences1", file.getName());
        assertNull(file.getProject());
    }

}
