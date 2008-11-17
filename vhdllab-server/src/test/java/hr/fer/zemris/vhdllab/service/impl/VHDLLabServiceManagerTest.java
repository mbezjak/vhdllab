/*package hr.fer.zemris.vhdllab.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.server.conf.FileTypeMapping;
import hr.fer.zemris.vhdllab.server.conf.FunctionalityType;
import hr.fer.zemris.vhdllab.server.conf.ServerConf;
import hr.fer.zemris.vhdllab.server.conf.ServerConfParser;
import hr.fer.zemris.vhdllab.service.Functionality;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.test.FileContentProvider;
import hr.fer.zemris.vhdllab.test.NameAndContent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

*//**
 * A test case for {@link VHDLLabServiceManager}.
 *
 * @author Miro Bezjak
 *//*
public class VHDLLabServiceManagerTest {

    private static final Caseless USER_ID = new Caseless("user.id");

    private static String generatorClass;
    private static String extractorClass;
    private static ServiceContainer container;
    private static VHDLLabServiceManager man;
    private static Project project;
    private static File file;

    @BeforeClass
    public static void initOnce() throws Exception {
        container = ServiceContainer.instance();

        EntityManagerUtil.createEntityManagerFactory();

        ServerConf conf = ServerConfParser.getConfiguration();
        FileTypeMapping mapping = conf.getFileTypeMapping(FileType.SOURCE);
        generatorClass = mapping.getFunctionality(FunctionalityType.GENERATOR);
        extractorClass = mapping.getFunctionality(FunctionalityType.EXTRACTOR);
    }

    @Before
    public void initEachTest() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        FileTypeMapping mapping = conf.getFileTypeMapping(FileType.SOURCE);
        mapping.addFunctionality(FunctionalityType.GENERATOR.toString(),
                generatorClass);
        mapping.addFunctionality(FunctionalityType.EXTRACTOR.toString(),
                extractorClass);
        man = new VHDLLabServiceManager();

        EntityManagerUtil.currentEntityManager();
        project = new Project(USER_ID, new Caseless("project_name"));
        prepairProject(FileType.SOURCE);
//        prepairProject(FileTypes.VHDL_SCHEMA);
//        prepairProject(FileTypes.VHDL_AUTOMATON);
        container.getProjectManager().save(project);
        file = container.getFileManager().findByName(project.getId(),
                new Caseless("comp_and"));
    }

    @After
    public void destroyEachTest() throws Exception {
        EntityManagerUtil.closeEntityManager();
        EntityManagerUtil.currentEntityManager();
        container.getProjectManager().delete(project.getId());
        EntityManagerUtil.closeEntityManager();
    }

    @AfterClass
    public static void destroyClass() throws Exception {
//        EntityManagerUtil.currentEntityManager();
//        container.getProjectManager().delete(project.getId());
//        EntityManagerUtil.closeEntityManager();
    }

    *//**
     * Test initialization in constructor.
     *//*
    @SuppressWarnings("unchecked")
    @Test
    public void constructor() throws Exception {
        Field field = getPrivateField("functionalities");
        Map<String, Map<FunctionalityType, Functionality<?>>> functionalities = (Map<String, Map<FunctionalityType, Functionality<?>>>) field
                .get(man);
        assertTrue("no defined functionalities.", functionalities.size() > 0);
    }

    *//**
     * File is null.
     *//*
    @Test(expected = NullPointerException.class)
    public void executeFunctionality() throws Exception {
        Method method = getPrivateMethod("executeFunctionality", File.class,
                FunctionalityType.class);
        invokeMethod(method, null, FunctionalityType.GENERATOR);
    }

    *//**
     * File with specified file type doesn't have any functionality defined.
     *//*
    @Test(expected = ServiceException.class)
    public void executeFunctionality2() throws Exception {
        File f = new File(FileType.PREDEFINED, new Caseless("file_name_1"), "");
        project.addFile(f);
        man.generateVHDL(f);

        // test cleanup
        project.removeFile(f);
    }

    *//**
     * Implementor threw runtime exception.
     *//*
    @Test(expected = ServiceException.class)
    public void executeFunctionality3() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        FileTypeMapping mapping = conf.getFileTypeMapping(file.getType());
        mapping.addFunctionality(FunctionalityType.GENERATOR.toString(),
                ExceptionThrowingFunctionality.class.getName());
        man = new VHDLLabServiceManager();
        man.generateVHDL(file);
    }

    *//**
     * Implementor returned null.
     *//*
    @Test(expected = ServiceException.class)
    public void executeFunctionality4() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        FileTypeMapping mapping = conf.getFileTypeMapping(file.getType());
        mapping.addFunctionality(FunctionalityType.GENERATOR.toString(),
                NullFunctionality.class.getName());
        man = new VHDLLabServiceManager();
        man.generateVHDL(file);
    }

    *//**
     * Every functionality implementor must be instantiable.
     *//*
    @SuppressWarnings("unchecked")
    @Test
    public void executeFunctionality5() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        Field field = getPrivateField("functionalities");
        Map<FileType, Map<FunctionalityType, Functionality<?>>> functionalities = (Map<FileType, Map<FunctionalityType, Functionality<?>>>) field
                .get(man);
        for (Entry<FileType, Map<FunctionalityType, Functionality<?>>> types : functionalities
                .entrySet()) {
            FileTypeMapping mapping = conf.getFileTypeMapping(types.getKey());
            for (Entry<FunctionalityType, Functionality<?>> func : types
                    .getValue().entrySet()) {
                String clazz = mapping.getFunctionality(func.getKey());
                Object instance = Class.forName(clazz).newInstance();
                assertNotNull("implementor not instantiated.", instance);
            }
        }
    }

    *//**
     * Every implementor must be stateless and deterministic.
     *//*
    @SuppressWarnings("unchecked")
    @Test
    public void executeFunctionality6() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        Set<File> files = project.getFiles();
        Field field = getPrivateField("functionalities");
        Map<FileType, Map<FunctionalityType, Functionality<?>>> functionalities = (Map<FileType, Map<FunctionalityType, Functionality<?>>>) field
                .get(man);
        for (Entry<FileType, Map<FunctionalityType, Functionality<?>>> types : functionalities
                .entrySet()) {
            FileTypeMapping mapping = conf.getFileTypeMapping(types.getKey());
            for (Entry<FunctionalityType, Functionality<?>> func : types
                    .getValue().entrySet()) {
                String clazz = mapping.getFunctionality(func.getKey());
                Class<?> funcClass = Class.forName(clazz);
                Functionality<Object> i1 = (Functionality<Object>) funcClass
                        .newInstance();
                for (File f : filterByType(files, types.getKey())) {
                    Functionality<Object> i2 = (Functionality<Object>) funcClass
                            .newInstance();
                    Object r1 = i1.execute(f);
                    Object r2 = i1.execute(f);
                    Object r3 = i2.execute(f);
                    assertNotNull("functionality implementor returned null", r1);
                    assertNotNull("functionality implementor returned null", r2);
                    assertNotNull("functionality implementor returned null", r3);
                    assertEquals(
                            "functionality implementor isn't deterministic.",
                            r1, r2);
                    assertEquals("functionality implementor isn't stateless.",
                            r2, r3);
                }
            }
        }
    }

    *//**
     * non-existing class
     *//*
    @Test
    public void configureFunctionality() throws Exception {
        Method method = getPrivateMethod("configureFunctionality",
                String.class, Properties.class);
        try {
            invokeMethod(method, "non.existing.class", new Properties());
        } catch (IllegalStateException e) {
            if (!e.getMessage().contains("doesn't exist")) {
                fail("Wrong exception thrown!");
            }
        }
    }

    *//**
     * class doesn't have default constructor
     *//*
    @Test
    public void configureFunctionality2() throws Exception {
        Method method = getPrivateMethod("configureFunctionality",
                String.class, Properties.class);
        try {
            invokeMethod(method, "java.lang.Integer", new Properties());
        } catch (IllegalStateException e) {
            if (!e.getMessage().contains("couldn't be instantiated")) {
                fail("Wrong exception thrown!");
            }
        }
    }

    *//**
     * class doesn't have public default constructor
     *//*
    @Test
    public void configureFunctionality3() throws Exception {
        Method method = getPrivateMethod("configureFunctionality",
                String.class, Properties.class);
        try {
            invokeMethod(method, "hr.fer.zemris.vhdllab.entities.Resource",
                    new Properties());
        } catch (IllegalStateException e) {
            if (!e.getMessage().contains("public default constructor")) {
                fail("Wrong exception thrown!");
            }
        }
    }

    *//**
     * class is not a functionality.
     *//*
    @Test
    public void configureFunctionality4() throws Exception {
        Method method = getPrivateMethod("configureFunctionality",
                String.class, Properties.class);
        try {
            invokeMethod(method, "java.lang.String", new Properties());
        } catch (IllegalStateException e) {
            if (!e.getMessage().contains("Inappropriate class type")) {
                fail("Wrong exception thrown!");
            }
        }
    }

    *//**
     * After class is instantiated, configure method is called.
     *//*
    @Test(expected = IllegalArgumentException.class)
    public void configureFunctionality5() throws Exception {
        Method method = getPrivateMethod("configureFunctionality",
                String.class, Properties.class);
        Properties p = new Properties();
        p.setProperty("test", "calling configure method");
        invokeMethod(method, ConfiguringFunctionality.class.getName(), p);
    }

    *//**
     * Returns only those files that have specified type.
     *//*
    private List<File> filterByType(Set<File> files, FileType type) {
        List<File> filtered = new ArrayList<File>();
        for (File f : files) {
            if (f.getType().equals(type)) {
                filtered.add(f);
            }
        }
        return filtered;
    }

    *//**
     * Invokes a method on service manager with a specified parameters.
     *//*
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

    *//**
     * Returns an accessible private method.
     *//*
    private Method getPrivateMethod(String name, Class<?>... params)
            throws Exception {
        Method method = man.getClass().getDeclaredMethod(name, params);
        method.setAccessible(true);
        return method;
    }

    *//**
     * Returns an accessible private field.
     *//*
    private Field getPrivateField(String name) throws Exception {
        Field field = man.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    private static void prepairProject(FileType type) {
        List<NameAndContent> contents = FileContentProvider.getContent(type);
        for (NameAndContent nc : contents) {
            File f = new File(type, nc.getName(), nc.getContent());
            project.addFile(f);
        }
    }

    *//**
     * A dummy implementation that throws exception during execution.
     *//*
    public static class ExceptionThrowingFunctionality implements
            Functionality<Object> {
        @Override
        public void configure(Properties properties) {
        }

        @Override
        public Object execute(File f) throws ServiceException {
            throw new IllegalStateException();
        }
    }

    *//**
     * A dummy implementation return null as an execution result.
     *//*
    public static class NullFunctionality implements Functionality<Object> {
        @Override
        public void configure(Properties properties) {
        }

        @Override
        public Object execute(File f) throws ServiceException {
            return null;
        }
    }

    *//**
     * A dummy implementation that throws {@link IllegalArgumentException} when
     * configure method is called and thus indicating that configure method is
     * called after class initialization.
     *//*
    public static class ConfiguringFunctionality implements
            Functionality<Object> {
        @Override
        public void configure(Properties properties) {
            throw new IllegalArgumentException();
        }

        @Override
        public Object execute(File f) throws ServiceException {
            return new Object();
        }
    }

}
*/