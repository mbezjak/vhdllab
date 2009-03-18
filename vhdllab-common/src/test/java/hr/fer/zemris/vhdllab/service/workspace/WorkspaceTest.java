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

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

public class WorkspaceTest extends ValueObjectTestSupport {

    private HashSet<Project> projects;
    private Hierarchy hierarchy;
    private Project predefinedProject;
    private HashSet<File> preferencesFiles;

    @Before
    public void initObject() {
        projects = new HashSet<Project>(2);
        Project firstProject = new Project("userId", "project1");
        projects.add(firstProject);
        projects.add(new Project("userId", "project2"));

        hierarchy = new Hierarchy(firstProject, new ArrayList<HierarchyNode>());
        predefinedProject = new Project(null, "predefined");
        predefinedProject.addFile(new File("predefined", null, "data"));
        preferencesFiles = new HashSet<File>(2);
        preferencesFiles.add(new File("preferences1", null, "data"));
        preferencesFiles.add(new File("preferences2", null, "data2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullProjects() {
        new Workspace(null, hierarchy, predefinedProject, preferencesFiles);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullHierarchy() {
        new Workspace(projects, null, predefinedProject, preferencesFiles);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullPredefinedProject() {
        new Workspace(projects, hierarchy, null, preferencesFiles);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullPreferencesFiles() {
        new Workspace(projects, hierarchy, predefinedProject, null);
    }

    @Test
    public void constructor() {
        Workspace workspace = new Workspace(projects, hierarchy,
                predefinedProject, preferencesFiles);
        assertEquals(projects, workspace.getProjects());
        assertNotSame(projects, workspace.getProjects());
        assertEquals(projects.size(), workspace.getProjectCount());
        Project project = (Project) CollectionUtils.get(
                workspace.getProjects(), 0);
        assertNull(project.getFiles());
        projects.add(new Project());
        assertEquals(2, workspace.getProjectCount());

        assertEquals(hierarchy, workspace.getActiveProjectHierarchy());

        predefinedProject.setName("new_name");
        assertEquals("predefined", workspace.getPredefinedProject().getName());
        assertEquals(predefinedProject.getFiles().size(), workspace
                .getPredefinedProject().getFiles().size());
        File file = (File) CollectionUtils.get(workspace.getPredefinedProject()
                .getFiles(), 0);
        assertNull(file.getProject());
        file = (File) CollectionUtils.get(predefinedProject.getFiles(), 0);
        file.setName("new_name");
        file = (File) CollectionUtils.get(workspace.getPredefinedProject()
                .getFiles(), 0);
        assertEquals("predefined", file.getName());

        file = (File) CollectionUtils.get(preferencesFiles, 0);
        file.setName("new_name");
        file = (File) CollectionUtils.get(workspace.getPreferencesFiles(), 0);
        assertEquals("preferences1", file.getName());
        assertNull(file.getProject());
    }

}
