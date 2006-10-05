package hr.fer.zemris.vhdllab.servlets.dispatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.MethodDispatcher;
import hr.fer.zemris.vhdllab.servlets.manprovs.SampleManagerProvider;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

public class AdvancedMethodDispatcherTest {

	private static VHDLLabManager labman;
	private static ManagerProvider mprov;
	private static MethodDispatcher disp;
	
	private static Project project;
	private static File file1;
	private static File file2;
	private static File file3;
	
	@BeforeClass
	public static void init() throws ServiceException {
		mprov = new SampleManagerProvider();
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
		
		disp = new AdvancedMethodDispatcher();
	}
	
	/**
	 * Properties is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void preformMethodDispatching() {
		disp.preformMethodDispatching(null, mprov);
	}
	
	/**
	 * Manager Provider is <code>null</code>.
	 */
	@Test(expected=NullPointerException.class)
	public void preformMethodDispatching2() {
		disp.preformMethodDispatching(new Properties(), null);
	}
	
	/**
	 * Method is simple - no operators.
	 */
	@Test
	public void preformMethodDispatching3() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_NAME;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(3, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * method1&method2
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching4() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(4, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
		assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE, ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * method1>method2
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching5() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_PROJECT_FILES_ID+">"+MethodConstants.MTD_LOAD_FILE_NAME;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(5, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".1", ""));
		assertEquals(file2.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".2", ""));
		assertEquals(file3.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".3", ""));
	}

	/**
	 * Method is complex:
	 * <blockquote>
	 * method1<method2
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching6() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_NAME+"<"+MethodConstants.MTD_LOAD_PROJECT_FILES_ID;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(5, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".1", ""));
		assertEquals(file2.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".2", ""));
		assertEquals(file3.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".3", ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * (method1)
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching7() {
		Properties p = new Properties();
		String method = "("+MethodConstants.MTD_LOAD_FILE_NAME+")";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(3, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * (method1)&method2
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching8() {
		Properties p = new Properties();
		String method = "("+MethodConstants.MTD_LOAD_FILE_NAME+")&"+MethodConstants.MTD_LOAD_FILE_TYPE;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(4, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
		assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE, ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * method1&method2>method3
	 * </blockquote>
	 * @throws ServiceException 
	 */
	@Test
	public void preformMethodDispatching9() throws ServiceException {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_PROJECT_NAME+"&"+MethodConstants.MTD_LOAD_PROJECT_OWNER_ID+">"+MethodConstants.MTD_CREATE_NEW_PROJECT;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		Long id = Long.parseLong(prop.getProperty(MethodConstants.PROP_PROJECT_ID));
		assertEquals(true, labman.existsProject(id));
		assertEquals(project.getProjectName(), labman.loadProject(id).getProjectName());
		assertEquals(project.getOwnerID(), labman.loadProject(id).getOwnerID());
		assertEquals(3, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertNotSame(String.valueOf(project.getId()), prop.getProperty(MethodConstants.PROP_PROJECT_ID, ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * (method1&method2)>method3
	 * </blockquote>
	 * @throws ServiceException 
	 */
	@Test
	public void preformMethodDispatching10() throws ServiceException {
		Properties p = new Properties();
		String method = "("+MethodConstants.MTD_LOAD_PROJECT_NAME+"&"+MethodConstants.MTD_LOAD_PROJECT_OWNER_ID+")>"+MethodConstants.MTD_CREATE_NEW_PROJECT;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		Long id = Long.parseLong(prop.getProperty(MethodConstants.PROP_PROJECT_ID));
		assertEquals(true, labman.existsProject(id));
		assertEquals(project.getProjectName(), labman.loadProject(id).getProjectName());
		assertEquals(project.getOwnerID(), labman.loadProject(id).getOwnerID());
		assertEquals(3, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertNotSame(String.valueOf(project.getId()), prop.getProperty(MethodConstants.PROP_PROJECT_ID, ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * method1<(method2&method3)
	 * </blockquote>
	 * @throws ServiceException 
	 */
	@Test
	public void preformMethodDispatching11() throws ServiceException {
		Properties p = new Properties();
		String method = MethodConstants.MTD_CREATE_NEW_PROJECT+"<("+MethodConstants.MTD_LOAD_PROJECT_NAME+"&"+MethodConstants.MTD_LOAD_PROJECT_OWNER_ID+")";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		Long id = Long.parseLong(prop.getProperty(MethodConstants.PROP_PROJECT_ID));
		assertEquals(true, labman.existsProject(id));
		assertEquals(project.getProjectName(), labman.loadProject(id).getProjectName());
		assertEquals(project.getOwnerID(), labman.loadProject(id).getOwnerID());
		assertEquals(3, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertNotSame(String.valueOf(project.getId()), prop.getProperty(MethodConstants.PROP_PROJECT_ID, ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * ((method1&method2)>method3)&method4
	 * </blockquote>
	 * @throws ServiceException 
	 */
	@Test
	public void preformMethodDispatching12() throws ServiceException {
		Properties p = new Properties();
		String method = "(("+MethodConstants.MTD_LOAD_PROJECT_NAME+"&"+MethodConstants.MTD_LOAD_PROJECT_OWNER_ID+")>"+MethodConstants.MTD_CREATE_NEW_PROJECT+")&"+MethodConstants.MTD_LOAD_FILE_NAME;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		Long id = Long.parseLong(prop.getProperty(MethodConstants.PROP_PROJECT_ID));
		assertEquals(true, labman.existsProject(id));
		assertEquals(project.getProjectName(), labman.loadProject(id).getProjectName());
		assertEquals(project.getOwnerID(), labman.loadProject(id).getOwnerID());
		assertEquals(4, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertNotSame(String.valueOf(project.getId()), prop.getProperty(MethodConstants.PROP_PROJECT_ID, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * (method1&method2)&(method3&method4)
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching13() {
		Properties p = new Properties();
		String method = "("+MethodConstants.MTD_LOAD_PROJECT_NAME+"&"+MethodConstants.MTD_LOAD_PROJECT_OWNER_ID+")&("+MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE+")";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(6, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertEquals(project.getProjectName(), prop.getProperty(MethodConstants.PROP_PROJECT_NAME, ""));
		assertEquals(String.valueOf(project.getOwnerID()), prop.getProperty(MethodConstants.PROP_PROJECT_OWNER_ID, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
		assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE, ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * ((method1&method2)&(method3&method4))
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching14() {
		Properties p = new Properties();
		String method = "(("+MethodConstants.MTD_LOAD_PROJECT_NAME+"&"+MethodConstants.MTD_LOAD_PROJECT_OWNER_ID+")&("+MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE+"))";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(6, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertEquals(project.getProjectName(), prop.getProperty(MethodConstants.PROP_PROJECT_NAME, ""));
		assertEquals(String.valueOf(project.getOwnerID()), prop.getProperty(MethodConstants.PROP_PROJECT_OWNER_ID, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
		assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE, ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * method1>(method2&method3)
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching15() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_PROJECT_FILES_ID+">("+MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE+")";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(8, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".1", ""));
		assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".1", ""));
		assertEquals(file2.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".2", ""));
		assertEquals(file2.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".2", ""));
		assertEquals(file3.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".3", ""));
		assertEquals(file3.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".3", ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * method1>(method2>(method3&method4))
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching16() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_PROJECT_FILES_ID+">("+MethodConstants.MTD_LOAD_PROJECT_FILES_ID+">("+MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE+"))";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(8, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".1", ""));
		assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".1", ""));
		assertEquals(file2.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".2", ""));
		assertEquals(file2.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".2", ""));
		assertEquals(file3.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".3", ""));
		assertEquals(file3.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".3", ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * (method1>(method2>(method3&method4)))
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching17() {
		Properties p = new Properties();
		String method = "("+MethodConstants.MTD_LOAD_PROJECT_FILES_ID+">("+MethodConstants.MTD_LOAD_PROJECT_FILES_ID+">("+MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE+")))";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(8, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".1", ""));
		assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".1", ""));
		assertEquals(file2.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".2", ""));
		assertEquals(file2.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".2", ""));
		assertEquals(file3.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".3", ""));
		assertEquals(file3.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".3", ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * method1>method2>(method3&method4)
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching18() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_BELONGS_TO_PROJECT_ID+">"+MethodConstants.MTD_LOAD_PROJECT_FILES_ID+">("+MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE+")";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(8, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".1", ""));
		assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".1", ""));
		assertEquals(file2.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".2", ""));
		assertEquals(file2.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".2", ""));
		assertEquals(file3.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".3", ""));
		assertEquals(file3.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".3", ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * ((method1>method2>(method3&method4))&method5)>method6
	 * </blockquote>
	 * @throws ServiceException 
	 */
	@Test
	public void preformMethodDispatching19() throws ServiceException {
		Properties p = new Properties();
		String method = "(("+MethodConstants.MTD_LOAD_FILE_BELONGS_TO_PROJECT_ID+">"+MethodConstants.MTD_LOAD_PROJECT_FILES_ID+">("+MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE+"))&"+MethodConstants.MTD_CREATE_NEW_PROJECT+")>"+MethodConstants.MTD_CREATE_NEW_FILE;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		p.setProperty(MethodConstants.PROP_PROJECT_NAME, "NewProjectName");
		p.setProperty(MethodConstants.PROP_PROJECT_OWNER_ID, "500");
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		Long id = Long.parseLong(prop.getProperty(MethodConstants.PROP_FILE_ID+".1"));
		id = labman.loadFile(id).getProject().getId();
		assertEquals(true, labman.existsProject(id));
		assertEquals("NewProjectName", labman.loadProject(id).getProjectName());
		assertEquals(Long.valueOf(500), labman.loadProject(id).getOwnerID());
		assertEquals(5, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * method1&method2
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching20() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID+".1", String.valueOf(file1.getId()));
		p.setProperty(MethodConstants.PROP_FILE_ID+".2", String.valueOf(file2.getId()));
		p.setProperty(MethodConstants.PROP_FILE_ID+".3", String.valueOf(file3.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(8, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
		assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".1", ""));
		assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".1", ""));
		assertEquals(file2.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".2", ""));
		assertEquals(file2.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".2", ""));
		assertEquals(file3.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+".3", ""));
		assertEquals(file3.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+".3", ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * method1&method2
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching21() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID+".2", String.valueOf(file2.getId()));
		p.setProperty(MethodConstants.PROP_FILE_ID+".3", String.valueOf(file3.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(3, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_METHOD_ARGUMENT_ERROR, prop.getProperty(MethodConstants.PROP_STATUS, ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * method1&method2
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching22() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(3, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_METHOD_ARGUMENT_ERROR, prop.getProperty(MethodConstants.PROP_STATUS, ""));
	}
	
	/**
	 * Method is complex:
	 * <blockquote>
	 * method1&method2
	 * </blockquote>
	 */
	@Test
	public void preformMethodDispatching23() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()+"s"));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(3, prop.keySet().size());
		assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_PARSE_ERROR, prop.getProperty(MethodConstants.PROP_STATUS, ""));
	}
	
	/**
	 * Method is invalid.
	 */
	@Test
	public void preformMethodDispatching24() {
		Properties p = new Properties();
		p.setProperty(MethodConstants.PROP_METHOD, "mtd");
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		Properties prop = disp.preformMethodDispatching(p, mprov);
		assertEquals(3, prop.keySet().size());
		assertEquals("mtd", prop.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_INVALID_METHOD_CALL, prop.getProperty(MethodConstants.PROP_STATUS, ""));
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AdvancedMethodDispatcherTest.class);
	}
}
