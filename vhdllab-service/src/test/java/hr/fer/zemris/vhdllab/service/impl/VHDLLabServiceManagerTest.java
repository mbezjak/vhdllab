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
import hr.fer.zemris.vhdllab.server.conf.FunctionalityType;
import hr.fer.zemris.vhdllab.server.conf.ServerConf;
import hr.fer.zemris.vhdllab.server.conf.ServerConfParser;
import hr.fer.zemris.vhdllab.service.Functionality;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLGenerator;
import hr.fer.zemris.vhdllab.test.FileContentProvider;
import hr.fer.zemris.vhdllab.test.NameAndContent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
    private static String extractorClass;
    private static ServiceContainer container;
    private static VHDLLabServiceManager man;
    private static Project project;
    private static File file;

    @BeforeClass
    public static void initOnce() throws Exception {
        container = new ServiceContainer(new DAOContainer());

        EntityManagerUtil.createEntityManagerFactory();
        EntityManagerUtil.currentEntityManager();
        project = new Project(USER_ID, "project_name");
        String type = FileTypes.VHDL_SOURCE;
        List<NameAndContent> contents = FileContentProvider.getContent(type);
        NameAndContent content = contents.get(0);
        file = new File(project, content.getName(), type, content.getContent());
        container.getProjectManager().save(project);
        EntityManagerUtil.closeEntityManager();

        ServerConf conf = ServerConfParser.getConfiguration();
        FileTypeMapping mapping = conf.getFileTypeMapping(file.getType());
        generatorClass = mapping.getFunctionality(FunctionalityType.GENERATOR);
        extractorClass = mapping.getFunctionality(FunctionalityType.EXTRACTOR);
    }

    @Before
    public void initEachTest() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        FileTypeMapping mapping = conf.getFileTypeMapping(file.getType());
        mapping.addFunctionality(FunctionalityType.GENERATOR.toString(),
                generatorClass);
        mapping.addFunctionality(FunctionalityType.EXTRACTOR.toString(),
                extractorClass);
        man = new VHDLLabServiceManager();
    }

    @AfterClass
    public static void destroyClass() throws Exception {
        EntityManagerUtil.currentEntityManager();
        container.getProjectManager().delete(project.getId());
        EntityManagerUtil.closeEntityManager();
    }

    /**
     * Test initialization in constructor.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void constructor() throws Exception {
        Class<?> clazz = man.getClass();
        Field field = clazz.getDeclaredField("functionalities");
        field.setAccessible(true);
        Map<String, Map<FunctionalityType, Functionality<?>>> functionalities = (Map<String, Map<FunctionalityType, Functionality<?>>>) field
                .get(man);
        assertTrue("no defined functionalities.", functionalities.size() > 0);
    }

    /**
     * Functionality class name is null.
     */
    @Test(expected = ServiceException.class)
    public void executeFunctionality() throws Exception {
        // no simulation defined for VHDL source file type
        // man.simulate(file);
        fail("not yet implemented!");
    }

    /**
     * Implementor threw runtime exception.
     */
    @Test(expected = ServiceException.class)
    public void executeFunctionality2() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        FileTypeMapping mapping = conf.getFileTypeMapping(file.getType());
        mapping.addFunctionality(FunctionalityType.GENERATOR.toString(),
                ExceptionThrowingGenerator.class.getName());
        man = new VHDLLabServiceManager();
        man.generateVHDL(file);
    }

    /**
     * Implementor returned null.
     */
    @Test(expected = ServiceException.class)
    public void executeFunctionality3() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        FileTypeMapping mapping = conf.getFileTypeMapping(file.getType());
        mapping.addFunctionality(FunctionalityType.GENERATOR.toString(),
                NullGenerator.class.getName());
        man = new VHDLLabServiceManager();
        man.generateVHDL(file);
    }

    /**
     * Every functionality implementor must be instantiable.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void executeFunctionality4() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        Field field = getPrivateField("functionalities");
        Map<String, Map<FunctionalityType, Functionality<?>>> functionalities = (Map<String, Map<FunctionalityType, Functionality<?>>>) field
                .get(man);
        for (Entry<String, Map<FunctionalityType, Functionality<?>>> types : functionalities
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

    /**
     * Every implementor must be stateless and deterministic.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void executeFunctionality5() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        List<File> files = getAllFiles();
        Field field = getPrivateField("functionalities");
        Map<String, Map<FunctionalityType, Functionality<?>>> functionalities = (Map<String, Map<FunctionalityType, Functionality<?>>>) field
                .get(man);
        for (Entry<String, Map<FunctionalityType, Functionality<?>>> types : functionalities
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
                    assertEquals("functionality implementor isn't deterministic.", r1, r2);
                    assertEquals("functionality implementor isn't stateless.", r2, r3);
                }
            }
        }
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
     * Returns all dummy files.
     */
    private List<File> getAllFiles() throws Exception {
        List<File> files = new ArrayList<File>();
        ServerConf conf = ServerConfParser.getConfiguration();
        for (String type : conf.getFileTypes()) {
            List<NameAndContent> list = FileContentProvider.getContent(type);
            if(list == null) {
                continue;
            }
            for (NameAndContent nc : list) {
                File f = new File(project, nc.getName(), type, nc.getContent());
                files.add(f);
            }
        }
        return files;
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
     * Returns an accessible private field.
     */
    private Field getPrivateField(String name) throws Exception {
        Field field = man.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    /**
     * A dummy implementation that throws exception during VHDL generation.
     */
    public static class ExceptionThrowingGenerator implements VHDLGenerator {
        @Override
        public VHDLGenerationResult execute(File f) throws ServiceException {
            throw new IllegalStateException();
        }
    }

    /**
     * A dummy implementation return null as a generation result.
     */
    public static class NullGenerator implements VHDLGenerator {
        @Override
        public VHDLGenerationResult execute(File f) throws ServiceException {
            return null;
        }
    }

}
