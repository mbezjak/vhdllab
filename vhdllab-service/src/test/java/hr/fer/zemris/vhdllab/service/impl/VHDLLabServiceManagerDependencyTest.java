package hr.fer.zemris.vhdllab.service.impl;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.FileManager;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceManager;
import hr.fer.zemris.vhdllab.test.FileContentProvider;
import hr.fer.zemris.vhdllab.test.NameAndContent;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A test case for {@link VHDLLabServiceManager}'s extractDependencies method.
 *
 * @author Miro Bezjak
 */
public class VHDLLabServiceManagerDependencyTest {

    private static final String USER_ID = "user.id";

    private static ServiceContainer container;
    private static FileManager fileMan;
    private static ServiceManager man;
    private static Project project;

    @BeforeClass
    public static void initOnce() throws Exception {
        container = ServiceContainer.instance();
        fileMan = container.getFileManager();
        man = container.getServiceManager();

        EntityManagerUtil.createEntityManagerFactory();
        EntityManagerUtil.currentEntityManager();
        project = new Project(USER_ID, "project_name");
        prepairProject(FileTypes.VHDL_SOURCE);
        prepairProject(FileTypes.VHDL_SCHEMA);
        prepairProject(FileTypes.VHDL_AUTOMATON);
        container.getProjectManager().save(project);
        EntityManagerUtil.closeEntityManager();
    }

    @AfterClass
    public static void destroyClass() throws Exception {
        EntityManagerUtil.currentEntityManager();
        container.getProjectManager().delete(project.getId());
        EntityManagerUtil.closeEntityManager();
    }

    /**
     * File is null.
     */
    @Test(expected = NullPointerException.class)
    public void extractDependencyNull() throws Exception {
        man.extractDependencies(null, false);
    }

    /**
     * File is null.
     */
    @Test(expected = NullPointerException.class)
    public void extractDependencyNull2() throws Exception {
        man.extractDependencies(null, true);
    }

    /**
     * Tests if returned collection is unmodifiable.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void extractDependencyUnmodifiableResult() throws Exception {
        File file = fileMan.findByName(project.getId(), "comp_and");
        Set<String> dependencies = man.extractDependencies(file, true);
        dependencies.add("new_dependency");
    }

    /**
     * Empty dependency.
     */
    @Test
    public void extractDependencyFirstLevel() throws Exception {
        File file = fileMan.findByName(project.getId(), "comp_and");
        assertEquals("not empty dependency.", Collections.emptySet(), man
                .extractDependencies(file, false));
    }

    /**
     * One predefined file as a dependency.
     */
    @Test
    public void extractDependencyFirstLevel2() throws Exception {
        File file = fileMan.findByName(project.getId(), "complex_source");
        Set<String> dependencies = new HashSet<String>(1);
        dependencies.add("comp_and");
        assertEquals("wrong dependency.", dependencies, man
                .extractDependencies(file, false));
    }

    /**
     * Two files as a dependency.
     */
    @Test
    public void extractDependencyFirstLevel3() throws Exception {
        File file = fileMan.findByName(project.getId(), "ultra_complex_source");
        Set<String> dependencies = new HashSet<String>(2);
        dependencies.add("complex_source");
        dependencies.add("comp_or");
        assertEquals("wrong dependency.", dependencies, man
                .extractDependencies(file, false));
    }

    /**
     * Extract all dependencies from a complex component.
     */
    @Test
    public void extractAllDependencies() throws Exception {
        File file = fileMan.findByName(project.getId(), "ultra_complex_source");
        Set<String> dependencies = new HashSet<String>(4);
        dependencies.add("complex_source");
        dependencies.add("comp_and");
        dependencies.add("comp_or");
        dependencies.add("vl_or");
        assertEquals("wrong dependency.", dependencies, man
                .extractDependencies(file, true));
    }

    private static void prepairProject(String type) {
        List<NameAndContent> contents = FileContentProvider.getContent(type);
        for (NameAndContent nc : contents) {
            new File(project, nc.getName(), type, nc.getContent());
        }
    }

}
