package hr.fer.zemris.vhdllab.service.simulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.FileManager;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.ServiceManager;
import hr.fer.zemris.vhdllab.test.FileContentProvider;
import hr.fer.zemris.vhdllab.test.NameAndContent;

import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A test case for {@link GHDLSimulator}.
 *
 * @author Miro Bezjak
 */
public class GhdlSimulatorTest {

    private static final Caseless USER_ID = new Caseless("user.id");

    private static ServiceContainer container;
    private static FileManager fileMan;
    private static ServiceManager man;
    private static Project project;
    private static Library library;

    @BeforeClass
    public static void initOnce() throws Exception {
        container = ServiceContainer.instance();
        fileMan = container.getFileManager();
        man = container.getServiceManager();

        EntityManagerUtil.createEntityManagerFactory();
        EntityManagerUtil.currentEntityManager();
        project = new Project(USER_ID, new Caseless("project_name"));
        prepairProject(FileType.SOURCE);
        prepairProject(FileType.TESTBENCH);
        container.getProjectManager().save(project);

        library = new Library(new Caseless("predefined"));
        prepairLibrary();
        container.getLibraryManager().save(library);
        EntityManagerUtil.closeEntityManager();
    }

    @AfterClass
    public static void destroyClass() throws Exception {
        EntityManagerUtil.currentEntityManager();
        container.getProjectManager().delete(project.getId());
        container.getLibraryManager().delete(library.getId());
        EntityManagerUtil.closeEntityManager();
    }

    @Before
    public void initEachTest() throws Exception {
        EntityManagerUtil.currentEntityManager();
    }

    @After
    public void destroyEachTest() throws Exception {
        EntityManagerUtil.closeEntityManager();
    }

    /**
     * File is null.
     */
    @Test(expected = NullPointerException.class)
    public void simulateNull() throws Exception {
        man.simulate(null);
    }

    /**
     * File is not simulatable.
     */
    @Test(expected = ServiceException.class)
    public void simulateFileNotSimulatable() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("comp_and"));
        man.simulate(file);
    }
    
    /**
     * Simulation of a simple component.
     */
    @Test
    public void simulate() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("comp_and_tb"));
        SimulationResult result = man.simulate(file);
        assertTrue("not a successful simulation.", result.isSuccessful());
        assertEquals("not a status 0.", Integer.valueOf(0), result.getStatus());
        assertEquals("not an empty list.", Collections.emptyList(), result
                .getMessages());
    }

    /**
     * Simulation of a 1-1 hierarchy component.
     */
    @Test
    public void simulate2() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("complex_source_tb"));
        SimulationResult result = man.simulate(file);
        assertTrue("not a successful simulation.", result.isSuccessful());
        assertEquals("not a status 0.", Integer.valueOf(0), result.getStatus());
        assertEquals("not an empty list.", Collections.emptyList(), result
                .getMessages());
    }

    /**
     * Simulation of a 1-2-2 hierarchy component.
     */
    @Test
    public void simulate3() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("ultra_complex_source_tb"));
        SimulationResult result = man.simulate(file);
        assertTrue("not a successful simulation.", result.isSuccessful());
        assertEquals("not a status 0.", Integer.valueOf(0), result.getStatus());
        assertEquals("not an empty list.", Collections.emptyList(), result
                .getMessages());
    }

    /**
     * Simulation of a 1-2-1 hierarchy component.
     */
    @Test
    public void simulate4() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("comp_oror_tb"));
        SimulationResult result = man.simulate(file);
        System.out.println(result);
        assertTrue("not a successful simulation.", result.isSuccessful());
        assertEquals("not a status 0.", Integer.valueOf(0), result.getStatus());
        assertEquals("not an empty list.", Collections.emptyList(), result
                .getMessages());
    }

    /**
     * Errors during simulation.
     */
    @Test
    public void simulate5() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("comp_and_tb"));
        String content = file.getData();
        // missing half of a file
        content = content.substring(0, content.length() / 2);
        file.setData(content);
        fileMan.save(file);

        SimulationResult result = man.simulate(file);
        assertFalse("no error during simulation.", result.isSuccessful());
        assertFalse("status=0.", result.getStatus().equals(Integer.valueOf(0)));
        assertFalse("no error messages.", result.getMessages().isEmpty());
    }

    private static void prepairProject(FileType type) {
        List<NameAndContent> contents = FileContentProvider.getContent(type);
        for (NameAndContent nc : contents) {
            File f = new File(type, nc.getName(), nc.getContent());
            project.addFile(f);
        }
    }

    private static void prepairLibrary() {
        FileType type = FileType.PREDEFINED;
        List<NameAndContent> contents = FileContentProvider.getContent(type);
        for (NameAndContent nc : contents) {
            LibraryFile f = new LibraryFile(nc.getName(), nc.getContent());
            library.addFile(f);
        }
    }

}
