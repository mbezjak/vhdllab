package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.JavaToAjaxRegisteredMethod;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;

import java.util.Properties;

/**
 * This class represents a registered method for "exists file" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodExistsFile implements JavaToAjaxRegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.JavaToAjaxRegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.service.VHDLLabManager)
	 */
	public Properties run(Properties p, VHDLLabManager labman) {
		String projectID = p.getProperty(MethodConstants.PROP_PROJECT_ID,null);
		String fileName = p.getProperty(MethodConstants.PROP_FILE_NAME,null);
		if(projectID == null) return errorProperties(MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No project ID specified!");
		if(fileName == null) return errorProperties(MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No file name specified!");
		
		Long id = null;
		try {
			id = Long.parseLong(projectID);
		} catch (NumberFormatException e) {
			return errorProperties(MethodConstants.SE_PARSE_ERROR,"Unable to parse project ID!");
		}
		
		// Check if file exists
		boolean exists = false;
		try {
			exists = labman.existsFile(id, fileName);
		} catch (ServiceException e) {
			return errorProperties(MethodConstants.SE_CAN_NOT_DETERMINE_EXISTANCE_OF_FILE, "Unable to determine if file exists.");
		}
		
		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,MethodConstants.MTD_EXISTS_FILE);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		resProp.setProperty(MethodConstants.PROP_FILE_EXISTS,String.valueOf(exists));
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
		resProp.setProperty(MethodConstants.STATUS_CONTENT,errorMessage);
		return resProp;
	}
}