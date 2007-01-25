package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;

/**
 * This class represents a registered method for "rename project" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_RENAME_PROJECT
 */
public class DoMethodRenameProject implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties run(Properties p, ManagerProvider mprov) {
		VHDLLabManager labman = (VHDLLabManager)mprov.get(ManagerProvider.VHDL_LAB_MANAGER);
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String projectID = p.getProperty(MethodConstants.PROP_PROJECT_ID,null);
		String newName = p.getProperty(MethodConstants.PROP_PROJECT_NAME,null);
		if(projectID == null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No project ID specified!");
		if(newName == null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No project name specified!");

		// Rename project
		try {
			Long id = Long.parseLong(projectID);
			labman.renameProject(id, newName);
		} catch (NumberFormatException e) {
			return errorProperties(method,MethodConstants.SE_PARSE_ERROR,"Unable to parse project ID = '"+projectID+"'!");
		} catch (Exception e) {
			return errorProperties(method,MethodConstants.SE_CAN_NOT_RENAME_PROJECT,"Project ("+projectID+") could not be renamed!");
		}
		
		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,method);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
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