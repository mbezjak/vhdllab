package hr.fer.zemris.vhdllab.servlets.manprovs;

import hr.fer.zemris.ajax.shared.RegisteredMethod;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.ajax.shared.MethodDispatcher;
import hr.fer.zemris.vhdllab.dao.FileDAO;
import hr.fer.zemris.vhdllab.dao.ProjectDAO;
import hr.fer.zemris.vhdllab.dao.impl.dummy.FileDAOMemoryImpl;
import hr.fer.zemris.vhdllab.dao.impl.dummy.ProjectDAOMemoryImpl;
import hr.fer.zemris.vhdllab.service.impl.dummy.VHDLLabManagerImpl;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.dispatch.AdvancedMethodDispatcher;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodCompileFile;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodCreateNewFile;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodCreateNewProject;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodExistsFile;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodExistsProject;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodFindProjectsByUser;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodGenerateShemaVHDL;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodGenerateTestbenchVHDL;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodGenerateVHDL;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodLoadFileBelongsToProjectId;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodLoadFileContent;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodLoadFileName;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodLoadFileType;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodLoadProjectFilesId;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodLoadProjectName;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodLoadProjectNmbrFiles;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodLoadProjectOwnerId;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodRenameFile;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodRenameProject;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodRunSimulation;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodSaveFile;
import hr.fer.zemris.vhdllab.servlets.methods.DoMethodSaveProject;

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
		Map<String,RegisteredMethod> registeredMethods = new HashMap<String, RegisteredMethod>();
		registeredMethods.put(MethodConstants.MTD_LOAD_FILE_NAME, new DoMethodLoadFileName());
		registeredMethods.put(MethodConstants.MTD_LOAD_FILE_TYPE, new DoMethodLoadFileType());
		registeredMethods.put(MethodConstants.MTD_LOAD_FILE_CONTENT, new DoMethodLoadFileContent());
		registeredMethods.put(MethodConstants.MTD_SAVE_FILE, new DoMethodSaveFile());
		registeredMethods.put(MethodConstants.MTD_RENAME_FILE, new DoMethodRenameFile());
		registeredMethods.put(MethodConstants.MTD_CREATE_NEW_FILE, new DoMethodCreateNewFile());
		registeredMethods.put(MethodConstants.MTD_EXISTS_FILE, new DoMethodExistsFile());
		registeredMethods.put(MethodConstants.MTD_LOAD_FILE_BELONGS_TO_PROJECT_ID, new DoMethodLoadFileBelongsToProjectId());
		
		registeredMethods.put(MethodConstants.MTD_LOAD_PROJECT_NAME, new DoMethodLoadProjectName());
		registeredMethods.put(MethodConstants.MTD_LOAD_PROJECT_NMBR_FILES, new DoMethodLoadProjectNmbrFiles());
		registeredMethods.put(MethodConstants.MTD_LOAD_PROJECT_OWNER_ID, new DoMethodLoadProjectOwnerId());
		registeredMethods.put(MethodConstants.MTD_LOAD_PROJECT_FILES_ID, new DoMethodLoadProjectFilesId());
		registeredMethods.put(MethodConstants.MTD_SAVE_PROJECT, new DoMethodSaveProject());
		registeredMethods.put(MethodConstants.MTD_RENAME_PROJECT, new DoMethodRenameProject());
		registeredMethods.put(MethodConstants.MTD_CREATE_NEW_PROJECT, new DoMethodCreateNewProject());
		registeredMethods.put(MethodConstants.MTD_EXISTS_PROJECT, new DoMethodExistsProject());
		registeredMethods.put(MethodConstants.MTD_FIND_PROJECTS_BY_USER, new DoMethodFindProjectsByUser());
		
		registeredMethods.put(MethodConstants.MTD_COMPILE_FILE, new DoMethodCompileFile());
		registeredMethods.put(MethodConstants.MTD_RUN_SIMULATION, new DoMethodRunSimulation());
		registeredMethods.put(MethodConstants.MTD_GENERATE_VHDL, new DoMethodGenerateVHDL());
		registeredMethods.put(MethodConstants.MTD_GENERATE_TESTBENCH_VHDL, new DoMethodGenerateTestbenchVHDL());
		registeredMethods.put(MethodConstants.MTD_GENERATE_SHEMA_VHDL, new DoMethodGenerateShemaVHDL());
		
		// Remember created methods, so we can later return
		// a reference to them.
		beans.put("registeredMethods", registeredMethods);
		
		//Register method dispatcher
		MethodDispatcher disp = new AdvancedMethodDispatcher();
		
		// Remember created methods, so we can later return
		// a reference to them.
		beans.put("methodDispatcher", disp);
	}
	
	public Object get(String name) {
		return beans.get(name);
	}

}
