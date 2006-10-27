package hr.fer.zemris.vhdllab.servlets.methods;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.manprovs.SampleManagerProvider;

import java.util.Properties;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DoMethodSaveUserFileTest {
	
	private static ManagerProvider mprov;
	private static RegisteredMethod regMethod;
	private static String method;
	private Properties prop;
	
	private static UserFile file;
	
	@BeforeClass
	public static void init() throws ServiceException {
		mprov = new SampleManagerProvider();
		VHDLLabManager labman = (VHDLLabManager)mprov.get("vhdlLabManager");
		file = labman.createNewUserFile(Long.valueOf(1000), UserFile.UFT_THEME);
		regMethod = new DoMethodSaveUserFile();
		method = MethodConstants.MTD_SAVE_USER_FILE;
	}

	
	@Before
	public void initEachTest() {
		prop = new Properties();
		prop.setProperty(MethodConstants.PROP_METHOD, method);
	}
	
	/**
	 * Not enough arguments.
	 */
	@Test
	public void run() {
		prop.setProperty(MethodConstants.PROP_FILE_CONTENT, "NewTestFileName_1");
		
		Properties p = regMethod.run(prop, mprov);
		assertEquals(3, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_METHOD_ARGUMENT_ERROR, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}

	/**
	 * Not enough arguments.
	 */
	@Test
	public void run2() {
		prop.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file.getId()));
		
		Properties p = regMethod.run(prop, mprov);
		assertEquals(3, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_METHOD_ARGUMENT_ERROR, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}
	
	/**
	 * File ID argument is not number.
	 */
	@Test
	public void run3() {
		prop.setProperty(MethodConstants.PROP_FILE_ID, "n");
		prop.setProperty(MethodConstants.PROP_FILE_CONTENT, "NewTestFileName_1");
		
		Properties p = regMethod.run(prop, mprov);
		assertEquals(3, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.SE_PARSE_ERROR, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}
	
	/**
	 * Test should pass without errors.
	 */
	@Test
	public void run4() {
		prop.setProperty(MethodConstants.PROP_FILE_ID, String.valueOf(file.getId()));
		prop.setProperty(MethodConstants.PROP_FILE_CONTENT, "NewTestFileName_1");
		
		Properties p = regMethod.run(prop, mprov);
		assertEquals(2, p.keySet().size());
		assertEquals(method, p.getProperty(MethodConstants.PROP_METHOD, ""));
		assertEquals(MethodConstants.STATUS_OK, p.getProperty(MethodConstants.PROP_STATUS, ""));
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(DoMethodSaveUserFileTest.class);
	}
}