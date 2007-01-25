package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;

/**
 * This class represents a registered method for "create project" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_CREATE_NEW_PROJECT
 */
public class DoMethodCreateNewProject implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties run(Properties p, ManagerProvider mprov) {
		VHDLLabManager labman = (VHDLLabManager)mprov.get(ManagerProvider.VHDL_LAB_MANAGER);
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String projectName = p.getProperty(MethodConstants.PROP_PROJECT_NAME,null);
		String ownerId = p.getProperty(MethodConstants.PROP_PROJECT_OWNER_ID,null);
		if(projectName==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No project name specified!");
		if(ownerId==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No owner ID specified!");
		
		// Create new project
		Project project = null;
		try {
			project = labman.createNewProject(projectName, ownerId);
		} catch (NumberFormatException e) {
			return errorProperties(method,MethodConstants.SE_PARSE_ERROR,"Unable to parse owner ID = '"+ownerId+"'!");
		} catch (Exception e) {
			project = null;
		}
		if(project==null) return errorProperties(method,MethodConstants.SE_CAN_NOT_CREATE_PROJECT, "Unable to create a new project!");
		
		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,method);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		resProp.setProperty(MethodConstants.PROP_PROJECT_ID,String.valueOf(project.getId()));
		return resProp;
	}
	
	/**
	 * This method is called if error occurs.
	 * @param method a method that caused this error
	 * @param errNo error message number
	 * @param errorMessage error message to pass back to caller
	 * @return a response Properties
	 */
	private Properties errorProperties(String method, String errNo, String errorMessage) {
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,method);
		resProp.setProperty(MethodConstants.PROP_STATUS,errNo);
		resProp.setProperty(MethodConstants.PROP_STATUS_CONTENT,errorMessage);
		return resProp;
	}
}