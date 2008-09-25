package hr.fer.zemris.vhdllab.service.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.FileManager;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
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
 * A test case for {@link GhdlCompiler}.
 *
 * @author Miro Bezjak
 */
public class GhdlCompilerTest {

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
    public void compileNull() throws Exception {
        man.compile(null);
    }

    /**
     * Compilation of a simple component.
     */
    @Test
    public void compile() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("comp_and"));
        CompilationResult result = man.compile(file);
        assertTrue("not a successful compilation.", result.isSuccessful());
        assertEquals("not a status 0.", Integer.valueOf(0), result.getStatus());
        assertEquals("not an empty list.", Collections.emptyList(), result
                .getMessages());
    }

    /**
     * Compilation of a 1-1 hierarchy component.
     */
    @Test
    public void compile2() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("complex_source"));
        CompilationResult result = man.compile(file);
        assertTrue("not a successful compilation.", result.isSuccessful());
        assertEquals("not a status 0.", Integer.valueOf(0), result.getStatus());
        assertEquals("not an empty list.", Collections.emptyList(), result
                .getMessages());
    }

    /**
     * Compilation of a 1-2-2 hierarchy component.
     */
    @Test
    public void compile3() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("ultra_complex_source"));
        CompilationResult result = man.compile(file);
        assertTrue("not a successful compilation.", result.isSuccessful());
        assertEquals("not a status 0.", Integer.valueOf(0), result.getStatus());
        assertEquals("not an empty list.", Collections.emptyList(), result
                .getMessages());
    }

    /**
     * Compilation of a 1-2-1 hierarchy component.
     */
    @Test
    public void compile4() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("comp_oror"));
        CompilationResult result = man.compile(file);
        assertTrue("not a successful compilation.", result.isSuccessful());
        assertEquals("not a status 0.", Integer.valueOf(0), result.getStatus());
        assertEquals("not an empty list.", Collections.emptyList(), result
                .getMessages());
    }

    /**
     * Errors during compilation.
     */
    @Test
    public void compile5() throws Exception {
        File file = fileMan.findByName(project.getId(), new Caseless("comp_and"));
        String content = file.getData();
        // missing half of a file
        content = content.substring(0, content.length() / 2);
        file.setData(content);
        fileMan.save(file);

        CompilationResult result = man.compile(file);
        assertFalse("no error during compilation.", result.isSuccessful());
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
