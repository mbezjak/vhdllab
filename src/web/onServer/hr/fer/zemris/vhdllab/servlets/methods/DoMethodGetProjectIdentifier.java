package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;

/**
 * This class represents a registered method for "get project identifier" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_GET_PROJECT_IDENTIFIER
 */
public class DoMethodGetProjectIdentifier implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties run(Properties p, ManagerProvider mprov) {
		VHDLLabManager labman = (VHDLLabManager)mprov.get(ManagerProvider.VHDL_LAB_MANAGER);
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String ownerId = p.getProperty(MethodConstants.PROP_PROJECT_OWNER_ID, null);
		String projectName = p.getProperty(MethodConstants.PROP_PROJECT_NAME, null);
		if(ownerId==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No owner id specified!");
		if(projectName==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No project name specified!");

		// Get project
		Project project = null;
		try {
			project = labman.getProject(ownerId, projectName);
		} catch (Exception e) {
			project = null;
		}
		if(project==null) return errorProperties(method,MethodConstants.SE_NO_SUCH_PROJECT,"Project ("+projectName+") not found!");

		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,method);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		resProp.setProperty(MethodConstants.PROP_PROJECT_ID, String.valueOf(project.getId()));
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