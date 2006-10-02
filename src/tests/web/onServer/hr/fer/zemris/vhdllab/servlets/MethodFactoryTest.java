package hr.fer.zemris.vhdllab.servlets;

import static org.junit.Assert.*;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.manprovs.SampleManagerProvider;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MethodFactoryTest {
	
	private static ManagerProvider mprov;
	private Properties p;
	
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
	}
	
	@Before
	public void initEachTest() {
		p = new Properties();
	}
	
	/**
	 * Method is simple.
	 */
	@Test
	public void getMethod() {
		String method = MethodConstants.MTD_LOAD_FILE_NAME;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		RegisteredMethod regMethod = MethodFactory.getMethod(method);
		Properties response = regMethod.run(p, mprov);
		assertEquals(3, response.size());
		assertEquals(method, response.getProperty(MethodConstants.PROP_METHOD));
		assertEquals(MethodConstants.STATUS_OK, response.getProperty(MethodConstants.PROP_STATUS));
		assertEquals(file1.getFileName(), response.getProperty(MethodConstants.PROP_FILE_NAME));
	}
	
	/**
	 * Method is advanced.
	 */
	@Test
	public void getMethod2() {
		String method = MethodConstants.MTD_LOAD_FILE_NAME+"&"+MethodConstants.MTD_LOAD_FILE_TYPE;
		p.setProperty(MethodConstants.PROP_METHOD, method);
		p.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file1.getId()));
		
		RegisteredMethod regMethod = MethodFactory.getMethod(method);
		Properties response = regMethod.run(p, mprov);
		assertEquals(4, response.size());
		assertEquals(method, response.getProperty(MethodConstants.PROP_METHOD));
		assertEquals(MethodConstants.STATUS_OK, response.getProperty(MethodConstants.PROP_STATUS));
		assertEquals(file1.getFileName(), response.getProperty(MethodConstants.PROP_FILE_NAME));
		assertEquals(file1.getFileType(), response.getProperty(MethodConstants.PROP_FILE_TYPE));
	}
	
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(MethodFactoryTest.class);
	}
}
