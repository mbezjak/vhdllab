package hr.fer.zemris.vhdllab.servlets.manprovs;

import hr.fer.zemris.ajax.shared.JavaToAjaxRegisteredMethod;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.dao.impl.dummy.FileDAOMemoryImpl;
import hr.fer.zemris.vhdllab.dao.impl.dummy.ProjectDAOMemoryImpl;
import hr.fer.zemris.vhdllab.service.impl.dummy.VHDLLabManagerImpl;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodGetFileContent;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodGetFileName;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodGetFileType;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodRenameFile;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodSaveFile;

import java.util.HashMap;
import java.util.Map;

public class SampleManagerProvider implements ManagerProvider {

	private Map<String,Object> beans = new HashMap<String,Object>();
	
	public SampleManagerProvider() {
		// Create all data access objects.
		// This is usualy done by inspecting XML configuration
		// file. However, here we will use simplified method:
		FileDAO fileDAO = new FileDAOMemoryImpl();
		ProjectDAO projectDAO = new ProjectDAOMemoryImpl();
		
		// Create all service managers, and configure them
		// with appropriate DAO objects. This too is often
		// performed by inspecting XML configuration file.
		VHDLLabManagerImpl labManImpl = new VHDLLabManagerImpl();
		labManImpl.setFileDAO(fileDAO);
		labManImpl.setProjectDAO(projectDAO);
		
		// Remember created managers, so we can later return
		// a reference to them.
		beans.put("vhdlLabManager",labManImpl);
		
		//Register all known methods
		Map<String,JavaToAjaxRegisteredMethod> registeredMethods = new HashMap<String, JavaToAjaxRegisteredMethod>();
		registeredMethods.put(MethodConstants.MTD_LOAD_FILE_NAME, new DoMethodGetFileName());
		registeredMethods.put(MethodConstants.MTD_LOAD_FILE_TYPE, new DoMethodGetFileType());
		registeredMethods.put(MethodConstants.MTD_LOAD_FILE_CONTENT, new DoMethodGetFileContent());
		registeredMethods.put(MethodConstants.MTD_SAVE_FILE, new DoMethodSaveFile());
		registeredMethods.put(MethodConstants.MTD_RENAME_FILE, new DoMethodRenameFile());
		
		// Remember created methods, so we can later return
		// a reference to them.
		beans.put("registeredMethods", registeredMethods);
	}
	
	public Object get(String name) {
		return beans.get(name);
	}

}
