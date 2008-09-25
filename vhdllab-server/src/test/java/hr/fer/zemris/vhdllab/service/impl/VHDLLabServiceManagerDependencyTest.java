package hr.fer.zemris.vhdllab.service.impl;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.FileType;
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

    private static final Caseless USER_ID = new Caseless("user.id");

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
        project = new Project(USER_ID, new Caseless("project_name"));
        prepairProject(FileType.SOURCE);
        prepairProject(FileType.SCHEMA);
        prepairProject(FileType.AUTOMATON);
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
        File file = fileMan.findByName(project.getId(), new Caseless("comp_and"));
        Set<Caseless> dependencies = man.extractDependencies(file, true);
        dependencies.add(new Caseless("new_dependency"));
    }

    /**
     * Empty dependency.
     */
    @Test
    public void extractDependencyFirstLevel() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("comp_and"));
        assertEquals("not empty dependency.", Collections.emptySet(), man
                .extractDependencies(file, false));
    }

    /**
     * One predefined file as a dependency.
     */
    @Test
    public void extractDependencyFirstLevel2() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("complex_source"));
        Set<Caseless> dependencies = new HashSet<Caseless>(1);
        dependencies.add(new Caseless("comp_and"));
        assertEquals("wrong dependency.", dependencies, man
                .extractDependencies(file, false));
    }

    /**
     * Two files as a dependency.
     */
    @Test
    public void extractDependencyFirstLevel3() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("ultra_complex_source"));
        Set<Caseless> dependencies = new HashSet<Caseless>(2);
        dependencies.add(new Caseless("complex_source"));
        dependencies.add(new Caseless("comp_or"));
        assertEquals("wrong dependency.", dependencies, man
                .extractDependencies(file, false));
    }

    /**
     * Extract all dependencies from a complex component.
     */
    @Test
    public void extractAllDependencies() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("ultra_complex_source"));
        Set<Caseless> dependencies = new HashSet<Caseless>(4);
        dependencies.add(new Caseless("complex_source"));
        dependencies.add(new Caseless("comp_and"));
        dependencies.add(new Caseless("comp_or"));
        dependencies.add(new Caseless("vl_or"));
        assertEquals("wrong dependency.", dependencies, man
                .extractDependencies(file, true));
    }

    /**
     * Extract all dependencies from a complex component.
     */
    @Test
    public void extractAllDependencies2() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("comp_oror"));
        Set<Caseless> dependencies = new HashSet<Caseless>(3);
        dependencies.add(new Caseless("comp_or"));
        dependencies.add(new Caseless("comp_or2"));
        dependencies.add(new Caseless("vl_or"));
        assertEquals("wrong dependency.", dependencies, man
                .extractDependencies(file, true));
    }

    private static void prepairProject(FileType type) {
        List<NameAndContent> contents = FileContentProvider.getContent(type);
        for (NameAndContent nc : contents) {
            File f = new File(type, nc.getName(), nc.getContent());
            project.addFile(f);
        }
    }

}
