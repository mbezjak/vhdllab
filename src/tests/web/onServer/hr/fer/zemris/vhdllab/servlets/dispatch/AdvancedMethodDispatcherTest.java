package hr.fer.zemris.vhdllab.servlets.dispatch;

import hr.fer.zemris.ajax.shared.JavaToAjaxRegisteredMethod;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.ajax.shared.MethodDispatcher;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.impl.dummy.VHDLLabManagerImpl;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.manprovs.SampleManagerProvider;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * This is a TestCase for {@linkplain hr.fer.zemris.servlets.dispatch.AdvancedMethodDispatcher}
 * class.
 * 
 * @author Miro Bezjak
 */
public class AdvancedMethodDispatcherTest extends TestCase {

	private VHDLLabManager labman = null;
	private Map<String, JavaToAjaxRegisteredMethod> regMap = null;
	private MethodDispatcher disp = null;
	
	private Project project = null;
	private File file1 = null;
	private File file2 = null;
	private File file3 = null;
	
	@SuppressWarnings("unchecked")
	private void init() {
		this.labman = new VHDLLabManagerImpl();
		try {
			this.project = labman.createNewProject("TestProjectName", Long.valueOf(1000));
			this.file1 = labman.createNewFile(project, "TestFileName_1", File.FT_VHDLSOURCE);
			this.file2 = labman.createNewFile(project, "TestFileName_2", File.FT_VHDLSOURCE);
			this.file3 = labman.createNewFile(project, "TestFileName_3", File.FT_VHDLTB);
			Set<File> files = new TreeSet<File>();
			files.add(file1);
			files.add(file2);
			files.add(file3);
			project.setFiles(files);
			labman.saveProject(project);
		} catch (ServiceException e) {
			e.printStackTrace();
			fail("Failed to create a file.");
		}
		ManagerProvider mprov = new SampleManagerProvider();
		this.regMap = (Map<String, JavaToAjaxRegisteredMethod>) mprov.get("registeredMethods");
		this.disp = new AdvancedMethodDispatcher();
	}
	
	public AdvancedMethodDispatcherTest(String name) {
		super(name);
		init();
	}
	
	/**
	 * Test method preformMethodDispatching() when properties is null.
	 */
	@Test
	public void testPreformMethodDispatching() {
		try {
			disp.preformMethodDispatching(null, regMap, labman);
			fail("No exception when properties is null.");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when map of registrated methods is null.
	 */
	@Test
	public void testPreformMethodDispatching2() {
		try {
			disp.preformMethodDispatching(new Properties(), null, labman);
			fail("No exception when map of registrated is null.");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when lab manager is null.
	 */
	@Test
	public void testPreformMethodDispatching3() {
		try {
			disp.preformMethodDispatching(new Properties(), regMap, null);
			fail("No exception when lab manager is null.");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when method is simple - no operators.
	 */
	@Test
	public void testPreformMethodDispatching4() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_NAME;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			assertEquals(3, prop.keySet().size());
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when method is composed of a method separating operator.
	 */
	@Test
	public void testPreformMethodDispatching5() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			assertEquals(4, prop.keySet().size());
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
			assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE, ""));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when method is composed of a redirection to right operator.
	 */
	@Test
	public void testPreformMethodDispatching6() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_PROJECT_FILES_ID+">"+MethodConstants.MTD_LOAD_FILE_NAME;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			assertEquals(5, prop.keySet().size());
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+1, ""));
			assertEquals(file2.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+2, ""));
			assertEquals(file3.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+3, ""));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}

	/**
	 * Test method preformMethodDispatching() when method is composed of a redirection to left operator.
	 */
	@Test
	public void testPreformMethodDispatching7() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_FILE_NAME+"<"+MethodConstants.MTD_LOAD_PROJECT_FILES_ID;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			assertEquals(5, prop.keySet().size());
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+1, ""));
			assertEquals(file2.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+2, ""));
			assertEquals(file3.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+3, ""));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when method is simple surrounded with brackets.
	 */
	@Test
	public void testPreformMethodDispatching8() {
		Properties p = new Properties();
		String method = "("+MethodConstants.MTD_LOAD_FILE_NAME+")";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			assertEquals(3, prop.keySet().size());
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when method is composed of a method separating operator
	 * while one inner method is surrounded with brackets.
	 */
	@Test
	public void testPreformMethodDispatching9() {
		Properties p = new Properties();
		String method = "("+MethodConstants.MTD_LOAD_FILE_NAME+")&"+MethodConstants.MTD_LOAD_FILE_TYPE;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			assertEquals(4, prop.keySet().size());
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
			assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE, ""));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when method is advanced:
	 * <blockquote>
	 * method1&method2>method3
	 * </blockquote>
	 */
	@Test
	public void testPreformMethodDispatching10() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_PROJECT_NAME+"&"+MethodConstants.MTD_LOAD_PROJECT_OWNER_ID+">"+MethodConstants.MTD_CREATE_NEW_PROJECT;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			Long id = Long.parseLong(prop.getProperty(MethodConstants.PROP_PROJECT_ID));
			assertEquals(true, labman.existsProject(id));
			assertEquals(project.getProjectName(), labman.loadProject(id).getProjectName());
			assertEquals(project.getOwnerID(), labman.loadProject(id).getOwnerID());
			assertEquals(3, prop.keySet().size());
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertNotSame(String.valueOf(project.getId()), prop.getProperty(MethodConstants.PROP_PROJECT_ID, ""));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when method is advanced:
	 * <blockquote>
	 * (method1&method2)>method3
	 * </blockquote>
	 */
	@Test
	public void testPreformMethodDispatching11() {
		Properties p = new Properties();
		String method = "("+MethodConstants.MTD_LOAD_PROJECT_NAME+"&"+MethodConstants.MTD_LOAD_PROJECT_OWNER_ID+")>"+MethodConstants.MTD_CREATE_NEW_PROJECT;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			Long id = Long.parseLong(prop.getProperty(MethodConstants.PROP_PROJECT_ID));
			assertEquals(true, labman.existsProject(id));
			assertEquals(project.getProjectName(), labman.loadProject(id).getProjectName());
			assertEquals(project.getOwnerID(), labman.loadProject(id).getOwnerID());
			assertEquals(3, prop.keySet().size());
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertNotSame(String.valueOf(project.getId()), prop.getProperty(MethodConstants.PROP_PROJECT_ID, ""));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when method is advanced:
	 * <blockquote>
	 * method1<(method2&method3)
	 * </blockquote>
	 */
	@Test
	public void testPreformMethodDispatching12() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_CREATE_NEW_PROJECT+"<("+MethodConstants.MTD_LOAD_PROJECT_NAME+"&"+MethodConstants.MTD_LOAD_PROJECT_OWNER_ID+")";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			Long id = Long.parseLong(prop.getProperty(MethodConstants.PROP_PROJECT_ID));
			assertEquals(true, labman.existsProject(id));
			assertEquals(project.getProjectName(), labman.loadProject(id).getProjectName());
			assertEquals(project.getOwnerID(), labman.loadProject(id).getOwnerID());
			assertEquals(3, prop.keySet().size());
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertNotSame(String.valueOf(project.getId()), prop.getProperty(MethodConstants.PROP_PROJECT_ID, ""));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when method is advanced:
	 * <blockquote>
	 * ((method1&method2)>method3)&method4
	 * </blockquote>
	 */
	@Test
	public void testPreformMethodDispatching13() {
		Properties p = new Properties();
		String method = "(("+MethodConstants.MTD_LOAD_PROJECT_NAME+"&"+MethodConstants.MTD_LOAD_PROJECT_OWNER_ID+")>"+MethodConstants.MTD_CREATE_NEW_PROJECT+")&"+MethodConstants.MTD_LOAD_FILE_NAME;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			Long id = Long.parseLong(prop.getProperty(MethodConstants.PROP_PROJECT_ID));
			assertEquals(true, labman.existsProject(id));
			assertEquals(project.getProjectName(), labman.loadProject(id).getProjectName());
			assertEquals(project.getOwnerID(), labman.loadProject(id).getOwnerID());
			assertEquals(4, prop.keySet().size());
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertNotSame(String.valueOf(project.getId()), prop.getProperty(MethodConstants.PROP_PROJECT_ID, ""));
			assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when method is advanced:
	 * <blockquote>
	 * (method1&method2)&(method3&method4)
	 * </blockquote>
	 */
	@Test
	public void testPreformMethodDispatching14() {
		Properties p = new Properties();
		String method = "("+MethodConstants.MTD_LOAD_PROJECT_NAME+"&"+MethodConstants.MTD_LOAD_PROJECT_OWNER_ID+")&("+MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE+")";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			assertEquals(6, prop.keySet().size());
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertEquals(project.getProjectName(), prop.getProperty(MethodConstants.PROP_PROJECT_NAME, ""));
			assertEquals(String.valueOf(project.getOwnerID()), prop.getProperty(MethodConstants.PROP_PROJECT_OWNER_ID, ""));
			assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
			assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE, ""));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when method is advanced:
	 * <blockquote>
	 * ((method1&method2)&(method3&method4))
	 * </blockquote>
	 */
	@Test
	public void testPreformMethodDispatching15() {
		Properties p = new Properties();
		String method = "(("+MethodConstants.MTD_LOAD_PROJECT_NAME+"&"+MethodConstants.MTD_LOAD_PROJECT_OWNER_ID+")&("+MethodConstants.MTD_CREATE_NEW_PROJECT+")&("+MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE+"))";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			assertEquals(6, prop.keySet().size());
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertEquals(project.getProjectName(), prop.getProperty(MethodConstants.PROP_PROJECT_NAME, ""));
			assertEquals(String.valueOf(project.getOwnerID()), prop.getProperty(MethodConstants.PROP_PROJECT_OWNER_ID, ""));
			assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME, ""));
			assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE, ""));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when method is advanced:
	 * <blockquote>
	 * method1>(method2&method3)
	 * </blockquote>
	 */
	@Test
	public void testPreformMethodDispatching16() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_PROJECT_FILES_ID+">("+MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE+")";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			assertEquals(8, prop.keySet().size());
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+1, ""));
			assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+"."+1, ""));
			assertEquals(file2.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+2, ""));
			assertEquals(file2.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+"."+2, ""));
			assertEquals(file3.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+3, ""));
			assertEquals(file3.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+"."+3, ""));
			} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when method is advanced:
	 * <blockquote>
	 * method1>(method2>(method3&method4))
	 * </blockquote>
	 */
	@Test
	public void testPreformMethodDispatching17() {
		Properties p = new Properties();
		String method = MethodConstants.MTD_LOAD_PROJECT_FILES_ID+">("+MethodConstants.MTD_LOAD_PROJECT_FILES_ID+">("+MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE+"))";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			assertEquals(8, prop.keySet().size());
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+1, ""));
			assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+"."+1, ""));
			assertEquals(file2.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+2, ""));
			assertEquals(file2.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+"."+2, ""));
			assertEquals(file3.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+3, ""));
			assertEquals(file3.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+"."+3, ""));
			} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test method preformMethodDispatching() when method is advanced:
	 * <blockquote>
	 * (method1>(method2>(method3&method4)))
	 * </blockquote>
	 */
	@Test
	public void testPreformMethodDispatching18() {
		Properties p = new Properties();
		String method = "("+MethodConstants.MTD_LOAD_PROJECT_FILES_ID+">("+MethodConstants.MTD_LOAD_PROJECT_FILES_ID+">("+MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE+")))";
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
		
		try {
			Properties prop = disp.preformMethodDispatching(p, regMap, labman);
			assertEquals(8, prop.keySet().size());
			method = method.substring(1, method.length()-1);
			assertEquals(method, prop.getProperty(MethodConstants.PROP_METHOD, ""));
			assertEquals(MethodConstants.STATUS_OK, prop.getProperty(MethodConstants.PROP_STATUS, ""));
			assertEquals(file1.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+1, ""));
			assertEquals(file1.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+"."+1, ""));
			assertEquals(file2.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+2, ""));
			assertEquals(file2.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+"."+2, ""));
			assertEquals(file3.getFileName(), prop.getProperty(MethodConstants.PROP_FILE_NAME+"."+3, ""));
			assertEquals(file3.getFileType(), prop.getProperty(MethodConstants.PROP_FILE_TYPE+"."+3, ""));
			} catch (Exception e) {
			e.printStackTrace();
			fail("Generated exception: "+e.getMessage());
		}
	}
}
