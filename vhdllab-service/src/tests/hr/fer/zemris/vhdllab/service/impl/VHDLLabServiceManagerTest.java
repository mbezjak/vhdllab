package hr.fer.zemris.vhdllab.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.dao.impl.FileDAOImpl;
import hr.fer.zemris.vhdllab.dao.impl.LibraryDAOImpl;
import hr.fer.zemris.vhdllab.dao.impl.LibraryFileDAOImpl;
import hr.fer.zemris.vhdllab.dao.impl.ProjectDAOImpl;
import hr.fer.zemris.vhdllab.dao.impl.UserFileDAOImpl;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.server.conf.FileTypeMapping;
import hr.fer.zemris.vhdllab.server.conf.ServerConf;
import hr.fer.zemris.vhdllab.server.conf.ServerConfParser;
import hr.fer.zemris.vhdllab.service.FileManager;
import hr.fer.zemris.vhdllab.service.LibraryFileManager;
import hr.fer.zemris.vhdllab.service.LibraryManager;
import hr.fer.zemris.vhdllab.service.ProjectManager;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.UserFileManager;
import hr.fer.zemris.vhdllab.service.VHDLGenerator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

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
	private static VHDLLabServiceManager man;
	private static Project project;
	private static File file;

	@BeforeClass
	public static void initOnce() throws Exception {
		/*
		 * DAO and
		 */
		ProjectManager projectManager = new ProjectManagerImpl(
				new ProjectDAOImpl());
		FileManager fileManager = new FileManagerImpl(new FileDAOImpl());
		LibraryManager library = new LibraryManagerImpl(new LibraryDAOImpl());
		LibraryFileManager libraryFile = new LibraryFileManagerImpl(
				new LibraryFileDAOImpl());
		UserFileManager UserFile = new UserFileManagerImpl(
				new UserFileDAOImpl());

		man = new VHDLLabServiceManager(projectManager, fileManager, library,
				libraryFile, UserFile);

		EntityManagerUtil.createEntityManagerFactory();
		EntityManagerUtil.currentEntityManager();
		project = new Project(USER_ID, "project.name");
		file = new File(project, "file.name", FileTypes.VHDL_SOURCE,
				"library ieee;...");
		projectManager.save(project);
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
		man.getProjectManager().delete(project.getId());
		EntityManagerUtil.closeEntityManager();
	}

	/**
	 * A dummy implementation that throws exception during VHDL generation.
	 */
	public static class ExceptionThrowingGenerator implements VHDLGenerator {
		@Override
		public VHDLGenerationResult generateVHDL(File file)
				throws ServiceException {
			throw new IllegalStateException();
		}
	}

	/**
	 * A dummy implementation return null as a generation result.
	 */
	public static class NullGenerator implements VHDLGenerator {
		@Override
		public VHDLGenerationResult generateVHDL(File file)
				throws ServiceException {
			return null;
		}
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
	 * Not defined file type
	 */
	@Test
	public void getFileTypeMapping() throws Throwable {
		Method method = getPrivateMethod("getFileTypeMapping", String.class);
		try {
			invokeMethod(method, "not.defined.file.type");
		} catch (ServiceException e) {
			if (!e.getMessage().contains("not defined in configuration")) {
				fail("Wrong exception thrown!");
			}
		}
	}

	/**
	 * non-existing class
	 */
	@Test
	public void instantiateClass() throws Throwable {
		Method method = getPrivateMethod("instantiateClass", String.class);
		try {
			invokeMethod(method, "non.existing.class");
		} catch (ServiceException e) {
			if (!e.getMessage().contains("doesn't exist")) {
				fail("Wrong exception thrown!");
			}
		}
	}

	/**
	 * class doesn't have default constructor
	 */
	@Test
	public void instantiateClass2() throws Throwable {
		Method method = getPrivateMethod("instantiateClass", String.class);
		try {
			invokeMethod(method, "java.lang.Integer");
		} catch (ServiceException e) {
			if (!e.getMessage().contains("couldn't be instantiated")) {
				fail("Wrong exception thrown!");
			}
		}
	}

	/**
	 * class doesn't have public default constructor
	 */
	@Test
	public void instantiateClass3() throws Throwable {
		Method method = getPrivateMethod("instantiateClass", String.class);
		try {
			invokeMethod(method, "hr.fer.zemris.vhdllab.entities.Resource");
		} catch (ServiceException e) {
			if (!e.getMessage().contains("public default constructor")) {
				fail("Wrong exception thrown!");
			}
		}
	}

	/**
	 * Invokes a method on service manager with a specified parameters.
	 */
	private Object invokeMethod(Method method, Object... params)
			throws Throwable {
		try {
			return method.invoke(man, params);
		} catch (InvocationTargetException e) {
			// rethrow original exception
			throw e.getCause();
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

}
