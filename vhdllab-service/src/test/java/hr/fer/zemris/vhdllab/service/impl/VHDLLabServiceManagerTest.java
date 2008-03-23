package hr.fer.zemris.vhdllab.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.dao.DAOContainer;
import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.server.conf.FileTypeMapping;
import hr.fer.zemris.vhdllab.server.conf.ServerConf;
import hr.fer.zemris.vhdllab.server.conf.ServerConfParser;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLGenerator;
import hr.fer.zemris.vhdllab.service.generators.SourceGenerator;
import hr.fer.zemris.vhdllab.test.FileContentProvider;
import hr.fer.zemris.vhdllab.test.NameAndContent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A test case for {@link VHDLLabServiceManager}.
 * 
 * @author Miro Bezjak
 */
public class VHDLLabServiceManagerTest {

    private static final String USER_ID = "user.id";

    private static String generatorClass;
    private static ServiceContainer container;
    private static VHDLLabServiceManager man;
    private static Project project;
    private static File file;

    @BeforeClass
    public static void initOnce() throws Exception {
        container = new ServiceContainer(new DAOContainer());
        man = (VHDLLabServiceManager) container.getServiceManager();

        EntityManagerUtil.createEntityManagerFactory();
        EntityManagerUtil.currentEntityManager();
        project = new Project(USER_ID, "project.name");
        file = new File(project, "file.name", FileTypes.VHDL_SOURCE,
                "library ieee;...");
        container.getProjectManager().save(project);
        EntityManagerUtil.closeEntityManager();

        ServerConf conf = ServerConfParser.getConfiguration();
        FileTypeMapping mapping = conf.getFileTypeMapping(file.getType());
        generatorClass = mapping.getGenerator();
    }

    @Before
    public void initEachTest() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        FileTypeMapping mapping = conf.getFileTypeMapping(file.getType());
        mapping.setGenerator(generatorClass);
    }

    @AfterClass
    public static void destroyClass() throws Exception {
        EntityManagerUtil.currentEntityManager();
        container.getProjectManager().delete(project.getId());
        EntityManagerUtil.closeEntityManager();
    }

    /**
     * Generator class name is null
     */
    @Test
    public void generateVHDL() {
        ServerConf conf = ServerConfParser.getConfiguration();
        FileTypeMapping mapping = conf.getFileTypeMapping(file.getType());
        mapping.setGenerator(null);

        try {
            man.generateVHDL(file);
        } catch (ServiceException e) {
            if (!e.getMessage().contains("No generator defined")) {
                fail("Wrong exception thrown!");
            }
        }
    }

    /**
     * Generator threw runtime exception
     */
    @Test
    public void generateVHDL2() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        FileTypeMapping mapping = conf.getFileTypeMapping(file.getType());
        mapping.setGenerator(ExceptionThrowingGenerator.class.getName());

        try {
            man.generateVHDL(file);
        } catch (ServiceException e) {
            if (!e.getMessage().contains("exception during generation")) {
                fail("Wrong exception thrown!");
            }
        }
    }

    /**
     * Generator returned null
     */
    @Test
    public void generateVHDL3() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        FileTypeMapping mapping = conf.getFileTypeMapping(file.getType());
        mapping.setGenerator(NullGenerator.class.getName());

        try {
            man.generateVHDL(file);
        } catch (ServiceException e) {
            if (!e.getMessage().contains("returned null result")) {
                fail("Wrong exception thrown!");
            }
        }
    }

    /**
     * everthing ok
     */
    @Test
    public void generateVHDL4() throws Exception {
        VHDLGenerationResult result = man.generateVHDL(file);
        assertEquals("messages are set.", Collections.emptyList(), result
                .getMessages());
        assertTrue("result not successful.", result.isSuccessful());
        assertEquals("vhdl code not equal.", file.getContent(), result
                .getVHDL());
    }

    /**
     * Test that every generator can be instantiated.
     */
    @Test
    public void generateVHDL5() throws Exception {
        for (FileTypeMapping m : getGeneratorMappings()) {
            VHDLGenerator generator = (VHDLGenerator) Class.forName(
                    m.getGenerator()).newInstance();
            assertNotNull("generator not instantiated.", generator);
        }
    }

    /**
     * On few examples see the generator doesn't return null.
     */
    @Test
    public void generateVHDL6() throws Exception {
        List<File> files = getGenerableFile();

        for (File f : files) {
            VHDLGenerationResult result = man.generateVHDL(f);
            assertNotNull("result is null.", result);
        }
    }

    /**
     * Test that every generator is stateless.
     */
    @Test
    public void generateVHDL7() throws Exception {
        List<File> files = getGenerableFile();

        for (FileTypeMapping m : getGeneratorMappings()) {
            Class<?> genClass = Class.forName(m.getGenerator());
            VHDLGenerator g1 = (VHDLGenerator) genClass.newInstance();
            for (File f : filterByType(files, m.getType())) {
                VHDLGenerator g2 = (VHDLGenerator) genClass.newInstance();
                VHDLGenerationResult r1 = g1.generateVHDL(f);
                VHDLGenerationResult r2 = g1.generateVHDL(f);
                VHDLGenerationResult r3 = g2.generateVHDL(f);
                assertEquals("generator isn't stateless.", r1, r2);
                assertEquals("generator isn't stateless.", r2, r3);
            }
        }
    }

    /**
     * Test that every generator is deterministic.
     */
    @Test
    public void generateVHDL8() throws Exception {
        List<File> files = getGenerableFile();

        for (FileTypeMapping m : getGeneratorMappings()) {
            Class<?> genClass = Class.forName(m.getGenerator());
            VHDLGenerator gen = (VHDLGenerator) genClass.newInstance();
            for (File f : filterByType(files, m.getType())) {
                VHDLGenerationResult r1 = gen.generateVHDL(f);
                VHDLGenerationResult r2 = gen.generateVHDL(f);
                assertEquals("generator isn't deterministic.", r1, r2);
            }
        }
    }

    /**
     * Generator not defined
     */
    @Test(expected = ServiceException.class)
    public void getGenerator() throws Exception {
        Method method = getPrivateMethod("getGenerator", String.class);
        invokeMethod(method, FileTypes.PREFERENCES_USER);
    }

    /**
     * everything ok
     */
    @Test
    public void getGenerator2() throws Exception {
        Method method = getPrivateMethod("getGenerator", String.class);
        VHDLGenerator gen = invokeMethod(method, FileTypes.VHDL_SOURCE);
        assertTrue("wrong class instantiated.",
                gen.getClass() == SourceGenerator.class);
    }

    /**
     * non-existing class
     */
    @Test
    public void instantiateClass() throws Exception {
        Method method = getPrivateMethod("instantiateClass", String.class);
        try {
            invokeMethod(method, "non.existing.class");
        } catch (IllegalStateException e) {
            if (!e.getMessage().contains("doesn't exist")) {
                fail("Wrong exception thrown!");
            }
        }
    }

    /**
     * class doesn't have default constructor
     */
    @Test
    public void instantiateClass2() throws Exception {
        Method method = getPrivateMethod("instantiateClass", String.class);
        try {
            invokeMethod(method, "java.lang.Integer");
        } catch (IllegalStateException e) {
            if (!e.getMessage().contains("couldn't be instantiated")) {
                fail("Wrong exception thrown!");
            }
        }
    }

    /**
     * class doesn't have public default constructor
     */
    @Test
    public void instantiateClass3() throws Exception {
        Method method = getPrivateMethod("instantiateClass", String.class);
        try {
            invokeMethod(method, "hr.fer.zemris.vhdllab.entities.Resource");
        } catch (IllegalStateException e) {
            if (!e.getMessage().contains("public default constructor")) {
                fail("Wrong exception thrown!");
            }
        }
    }

    /**
     * Returns only those files that have specified type.
     */
    private List<File> filterByType(List<File> files, String type) {
        List<File> filtered = new ArrayList<File>();
        for (File f : files) {
            if (f.getType().equalsIgnoreCase(type)) {
                filtered.add(f);
            }
        }
        return filtered;
    }

    /**
     * Returns all dummy files that can be generated.
     */
    private List<File> getGenerableFile() throws Exception {
        List<File> files = new ArrayList<File>();
        for (FileTypeMapping m : getGeneratorMappings()) {
            String type = m.getType();
            List<NameAndContent> list = FileContentProvider.getContent(type);
            for (NameAndContent nc : list) {
                File f = new File(project, nc.getName(), type, nc.getContent());
                files.add(f);
            }
        }
        return files;
    }

    /**
     * Returns all file type mappings that have defined generators.
     */
    private List<FileTypeMapping> getGeneratorMappings() throws Exception {
        List<FileTypeMapping> mappings = new ArrayList<FileTypeMapping>();
        ServerConf conf = ServerConfParser.getConfiguration();
        for (String type : conf.getFileTypes()) {
            FileTypeMapping mapping = conf.getFileTypeMapping(type);
            if (mapping.getGenerator() != null) {
                mappings.add(mapping);
            }
        }
        return mappings;
    }

    /**
     * Invokes a method on service manager with a specified parameters.
     */
    @SuppressWarnings("unchecked")
    private <T> T invokeMethod(Method method, Object... params)
            throws Exception {
        try {
            return (T) method.invoke(man, params);
        } catch (InvocationTargetException e) {
            // rethrow original exception
            Exception cause = (Exception) e.getCause();
            throw cause;
        }
    }

    /**
     * Returns an accessible private method.
     */
    private Method getPrivateMethod(String name, Class<?>... params)
            throws Exception {
        Method method = man.getClass().getDeclaredMethod(name, params);
        method.setAccessible(true);
        return method;
    }

    /**
     * A dummy implementation that throws exception during VHDL generation.
     */
    public static class ExceptionThrowingGenerator implements VHDLGenerator {
        @Override
        public VHDLGenerationResult generateVHDL(File f)
                throws ServiceException {
            throw new IllegalStateException();
        }
    }

    /**
     * A dummy implementation return null as a generation result.
     */
    public static class NullGenerator implements VHDLGenerator {
        @Override
        public VHDLGenerationResult generateVHDL(File f)
                throws ServiceException {
            return null;
        }
    }

}
