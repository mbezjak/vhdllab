package hr.fer.zemris.vhdllab.servlets.methods;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.manprovs.SampleManagerProvider;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DoMethodSaveProjectTest {
	
	private static ManagerProvider mprov;
	private static RegisteredMethod regMethod;
	private static String method;
	private Properties prop;
	
	private static Project project;
	private static File file1;
	private static File file2;
	private static File file3;
	
	@BeforeClass
	public static void init() throws ServiceException {
		mprov = new SampleManagerProvider();
		VHDLLabManager labman = (VHDLLabManager)mprov.get("vhdlLabManager");
		project = labman.createNewProject("TestProjectName", Long.valueOf(1000));
		file1 = labman.createNewFile(project, "TestFileName_1", File.FT_VHDLSOURCE);
		file2 = labman.createNewFile(project, "TestFileName_2", File.FT_VHDLSOURCE);
		file3 = labman.createNewFile(project, "TestFileName_3", File.FT_VHDLTB);
		Set<File> files = new TreeSet<File>();
		files.add(file1);
		files.add(file2);
		files.add(file3);
		project.setFiles(files);
		labman.saveProject(project);
		regMethod = new DoMethodSaveProject();
		method = MethodConstants.MTD_SAVE_PROJECT;
	}
	
	@Before
	public void initEachTest() {
		prop = new Properties();
		prop.setProperty(MethodConstants.PROP_METHOD, method);
	}
	
	/**
	 * There is no file ID argument.
	 */
	@Test
	public void run() {
		prop.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));

		Properties p = regMethod.run(prop, mprov);
		assertEquals(3, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_METHOD_ARGUMENT_ERROR, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}
	
	/**
	 * There is no project ID argument.
	 */
	@Test
	public void run2() {
		prop.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));

		Properties p = regMethod.run(prop, mprov);
		assertEquals(3, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_METHOD_ARGUMENT_ERROR, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}

	/**
	 * There is no project ID argument.
	 */
	@Test
	public void run3() {
		prop.setProperty(MethodConstants.PROP_FILE_ID+".1", String.valueOf(file1.getId()));

		Properties p = regMethod.run(prop, mprov);
		assertEquals(3, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_METHOD_ARGUMENT_ERROR, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}

	/**
	 * File ID has no number extension
	 */
	@Test
	public void run4() {
		prop.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		prop.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));

		Properties p = regMethod.run(prop, mprov);
		assertEquals(2, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}

	/**
	 * File ID has a number extension '1'
	 */
	@Test
	public void run5() {
		prop.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		prop.setProperty(MethodConstants.PROP_FILE_ID+".1", String.valueOf(file1.getId()));

		Properties p = regMethod.run(prop, mprov);
		assertEquals(2, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}
	
	/**
	 * File ID has a number extension '2'
	 */
	@Test
	public void run6() {
		prop.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		prop.setProperty(MethodConstants.PROP_FILE_ID+".2", String.valueOf(file1.getId()));

		Properties p = regMethod.run(prop, mprov);
		assertEquals(3, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_METHOD_ARGUMENT_ERROR, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}
	
	/**
	 * One file ID has no number extension and other files have
	 */
	@Test
	public void run7() {
		prop.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		prop.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		prop.setProperty(MethodConstants.PROP_FILE_ID+".1", String.valueOf(file2.getId()));
		prop.setProperty(MethodConstants.PROP_FILE_ID+".2", String.valueOf(file3.getId()));

		Properties p = regMethod.run(prop, mprov);
		assertEquals(2, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}

	/**
	 * File ID is not a number
	 */
	@Test
	public void run8() {
		prop.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		prop.setProperty(MethodConstants.PROP_FILE_ID, "n");
		prop.setProperty(MethodConstants.PROP_FILE_ID+".1", String.valueOf(file2.getId()));
		prop.setProperty(MethodConstants.PROP_FILE_ID+".2", String.valueOf(file3.getId()));

		Properties p = regMethod.run(prop, mprov);
		assertEquals(3, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_PARSE_ERROR, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}

	/**
	 * Test should pass without errors.
	 */
	@Test
	public void run9() {
		prop.setProperty(MethodConstants.PROP_PROJECT_ID, "n");
		prop.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		prop.setProperty(MethodConstants.PROP_FILE_ID+".1", String.valueOf(file2.getId()));
		prop.setProperty(MethodConstants.PROP_FILE_ID+".2", String.valueOf(file3.getId()));

		Properties p = regMethod.run(prop, mprov);
		assertEquals(3, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_PARSE_ERROR, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DoMethodSaveProjectTest.class);
	}
}