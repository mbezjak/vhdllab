package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;

/**
 * This class represents a registered method for "exists project" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodExistsProject implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.service.VHDLLabManager)
	 */
	public Properties run(Properties p, VHDLLabManager labman) {
		String projectID = p.getProperty(MethodConstants.PROP_PROJECT_ID,null);
		if(projectID == null) return errorProperties(MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No project ID specified!");
		
		Long id = null;
		try {
			id = Long.parseLong(projectID);
		} catch (NumberFormatException e) {
			return errorProperties(MethodConstants.SE_PARSE_ERROR,"Unable to parse project ID!");
		}
		
		// Check if project exists
		boolean exists = false;
		try {
			exists = labman.existsProject(id);
		} catch (ServiceException e) {
			return errorProperties(MethodConstants.SE_CAN_NOT_DETERMINE_EXISTANCE_OF_PROJECT, "Unable to determine if project exists.");
		}
		
		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,MethodConstants.MTD_EXISTS_PROJECT);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		resProp.setProperty(MethodConstants.PROP_PROJECT_EXISTS,String.valueOf(exists));
		return resProp;
	}
	
	/**
	 * This method is called if errors occur.
	 * @param errNo error message number
	 * @param errorMessage error message to pass back to caller
	 * @return a response Properties
	 */
	private Properties errorProperties(String errNo, String errorMessage) {
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_STATUS,errNo);
		resProp.setProperty(MethodConstants.PROP_STATUS_CONTENT,errorMessage);
		return resProp;
	}
}