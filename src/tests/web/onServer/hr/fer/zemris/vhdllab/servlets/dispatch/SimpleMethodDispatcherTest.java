package hr.fer.zemris.vhdllab.servlets.dispatch;

import static org.junit.Assert.*;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.MethodDispatcher;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.manprovs.SampleManagerProvider;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleMethodDispatcherTest {

	private static VHDLLabManager labman;
	private static Map<String, RegisteredMethod> regMap;
	private static MethodDispatcher disp;
	
	private static Project project;
	private static File file1;
	private static File file2;
	private static File file3;
	
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void init() throws ServiceException {
		ManagerProvider mprov = new SampleManagerProvider();
		labman = (VHDLLabManager)mprov.get("vhdlLabManager");
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
		
		regMap = (Map<String, RegisteredMethod>) mprov.get("registeredMethods");
		disp = new SimpleMethodDispatcher();
	}
	
	/**
	 * Properties is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void preformMethodDispatching() {
		disp.preformMethodDispatching(null, regMap, labman);
	}
	
	/**
	 * Map of registrated methods is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void preformMethodDispatching2() {
		disp.preformMethodDispatching(new Properties(), null, labman);
	}
	
	/**
	 * Lab manager is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void preformMethodDispatching3() {
		disp.preformMethodDispatching(new Properties(), regMap, null);
	}
	
	/**
	 * Method is simple.
	 */
	@Test
	public void preformMethodDispatching4() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_NAME;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, regMap, labman);
		assertEquals(3, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
	}
	
	/**
	 * Method is simple.
	 */
	@Test
	public void preformMethodDispatching5() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_NAME;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		
		Properties prop = disp.preformMethodDispatching(p, regMap, labman);
		assertEquals(3, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_METHOD_ARGUMENT_ERROR, prop.getProperty(MethodConstants.PROP_STATUS, ""));
	}
	
	/**
	 * Method is simple.
	 */
	@Test
	public void preformMethodDispatching6() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_NAME;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()+"s"));
		
		Properties prop = disp.preformMethodDispatching(p, regMap, labman);
		assertEquals(3, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_PARSE_ERROR, prop.getProperty(MethodConstants.PROP_STATUS, ""));
	}
	
	/**
	 * Method is invalid.
	 */
	@Test
	public void preformMethodDispatching7() {
		Properties p = new Properties();
		p.setProperty(MethodConstants.PROP_METHOD, "mtd");
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, regMap, labman);
		assertEquals(3, prop.keySet().size());
		assertEquals("mtd", prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_INVALID_METHOD_CALL, prop.getProperty(MethodConstants.PROP_STATUS, ""));
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(SimpleMethodDispatcherTest.class);
	}
}
