package hr.fer.zemris.vhdllab.service.workspace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.PreferencesFile;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.hierarchy.HierarchyNode;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

public class WorkspaceTest extends ValueObjectTestSupport {

    private Project firstProject;
    private Project secondProject;
    private Project thirdProject;
    private Map<Project, ProjectMetadata> projectMetadata;
    private Set<File> predefinedFiles;
    private List<PreferencesFile> preferencesFiles;
    private Workspace workspace;

    @SuppressWarnings("unchecked")
    @Before
    public void initObject() {
        projectMetadata = new HashMap<Project, ProjectMetadata>(3);
        firstProject = new Project("userId", "project1");
        Hierarchy hierarchy = new Hierarchy(firstProject,
                new ArrayList<HierarchyNode>());
        projectMetadata.put(firstProject, new ProjectMetadata(hierarchy,
                Collections.EMPTY_SET));

        secondProject = new Project("userId", "project2");
        hierarchy = new Hierarchy(secondProject, new ArrayList<HierarchyNode>());
        projectMetadata.put(secondProject, new ProjectMetadata(hierarchy,
                Collections.EMPTY_SET));

        thirdProject = new Project("userId", "project3");
        projectMetadata.put(thirdProject, null);

        predefinedFiles = new HashSet<File>(1);
        predefinedFiles.add(new File("predefined", null, "data"));
        preferencesFiles = new ArrayList<PreferencesFile>(2);
        preferencesFiles.add(new PreferencesFile("preferences1", "data"));
        preferencesFiles.add(new PreferencesFile("preferences2", "data2"));
        workspace = new Workspace(projectMetadata, predefinedFiles,
                preferencesFiles);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullProjectMetadata() {
        new Workspace(null, predefinedFiles, preferencesFiles);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullPredefinedProject() {
        new Workspace(projectMetadata, null, preferencesFiles);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullPreferencesFiles() {
        new Workspace(projectMetadata, predefinedFiles, null);
    }

    @Test
    public void constructor() {
        workspace = new Workspace(projectMetadata, predefinedFiles,
                preferencesFiles);
        assertEquals(3, workspace.getProjectCount());
        for (Project project : workspace.getProjects()) {
            assertNull(project.getFiles());
        }

        assertEquals(predefinedFiles.size(), workspace.getPredefinedFiles()
                .size());
        File file = (File) CollectionUtils.get(workspace.getPredefinedFiles(),
                0);
        assertNull(file.getProject());
        file = (File) CollectionUtils.get(predefinedFiles, 0);
        file.setName("new_name");
        file = (File) CollectionUtils.get(workspace.getPredefinedFiles(), 0);
        assertEquals("predefined", file.getName());

        PreferencesFile preferencesFile = preferencesFiles.get(0);
        preferencesFile.setName("new_name");
        preferencesFile = workspace.getPreferencesFiles().get(0);
        assertEquals("preferences1", preferencesFile.getName());
    }

    @Test
    public void getProjects() {
        assertEquals(3, workspace.getProjectCount());
        assertEquals(3, workspace.getProjects().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addProject() {
        workspace.addProject(null);
    }

    @Test
    public void addProject2() {
        Project project = new Project();
        workspace.addProject(project);
        assertEquals(4, workspace.getProjectCount());
        assertEquals(4, workspace.getProjects().size());
        assertNotNull(workspace.getFiles(project));
        assertNotNull(workspace.getHierarchy(project));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeProject() {
        workspace.removeProject(null);
    }

    @Test
    public void removeProject2() {
        workspace.removeProject(new Project());
        assertEquals(3, workspace.getProjectCount());
        assertEquals(3, workspace.getProjects().size());

        workspace.removeProject(firstProject);
        assertEquals(2, workspace.getProjectCount());
        assertEquals(2, workspace.getProjects().size());

        workspace.removeProject(secondProject);
        assertEquals(1, workspace.getProjectCount());
        assertEquals(1, workspace.getProjects().size());

        workspace.removeProject(thirdProject);
        assertEquals(0, workspace.getProjectCount());
        assertEquals(0, workspace.getProjects().size());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void addFile() {
        workspace.addFile(null, new Hierarchy(new Project(),
                Collections.EMPTY_LIST));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFile2() {
        File file = new File();
        file.setProject(firstProject);
        workspace.addFile(file, null);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void addFile3() {
        Project project = new Project();
        File file = new File();
        file.setProject(project);
        workspace.addFile(file, new Hierarchy(project, Collections.EMPTY_LIST));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void addFile4() {
        assertTrue(workspace.getFiles(firstProject).isEmpty());
        File file = new File();
        file.setProject(firstProject);
        Hierarchy hierarchy = new Hierarchy(firstProject,
                Collections.EMPTY_LIST);
        workspace.addFile(file, hierarchy);
        assertEquals(1, workspace.getFiles(firstProject).size());
        assertEquals(hierarchy, workspace.getHierarchy(firstProject));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void removeFile() {
        workspace.removeFile(null, new Hierarchy(new Project(),
                Collections.EMPTY_LIST));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeFile2() {
        File file = new File();
        file.setProject(firstProject);
        workspace.removeFile(file, null);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void removeFile3() {
        Project project = new Project();
        File file = new File();
        file.setProject(project);
        workspace.removeFile(file, new Hierarchy(project,
                Collections.EMPTY_LIST));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void removeFile4() {
        assertTrue(workspace.getFiles(firstProject).isEmpty());
        File file = new File();
        file.setProject(firstProject);
        Hierarchy hierarchy = new Hierarchy(firstProject,
                Collections.EMPTY_LIST);
        workspace.addFile(file, hierarchy);
        assertEquals(1, workspace.getFiles(firstProject).size());

        workspace.removeFile(file, hierarchy);
        assertTrue(workspace.getFiles(firstProject).isEmpty());
        assertEquals(hierarchy, workspace.getHierarchy(firstProject));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getHierarchy() {
        workspace.getHierarchy(null);
    }

    @Test
    public void getHierarchy2() {
        assertNull(workspace.getHierarchy(new Project()));
        assertNotNull(workspace.getHierarchy(firstProject));
        assertNull(workspace.getHierarchy(thirdProject));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFiles() {
        workspace.getFiles(null);
    }

    @Test
    public void getFiles2() {
        assertNull(workspace.getFiles(new Project()));
        assertNotNull(workspace.getFiles(firstProject));
        assertNull(workspace.getFiles(thirdProject));
    }

    @Test
    public void testToString() {
        toStringPrint(workspace);
    }

}
