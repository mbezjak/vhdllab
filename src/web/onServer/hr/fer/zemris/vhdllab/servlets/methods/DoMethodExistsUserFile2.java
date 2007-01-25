package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;

/**
 * This class represents a registered method for "exists user file (2)" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_EXISTS_USER_FILE2
 */
public class DoMethodExistsUserFile2 implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties run(Properties p, ManagerProvider mprov) {
		VHDLLabManager labman = (VHDLLabManager)mprov.get(ManagerProvider.VHDL_LAB_MANAGER);
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String ownerId = p.getProperty(MethodConstants.PROP_FILE_OWNER_ID, null);
		String fileName = p.getProperty(MethodConstants.PROP_FILE_NAME, null);
		if(ownerId == null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR, "No owner id specified!");
		if(fileName == null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR, "No file name specified!");
		
		// Check if file exists
		boolean exists = false;
		try {
			exists = labman.existsUserFile(ownerId, fileName);
		} catch (Exception e) {
			return errorProperties(method,MethodConstants.SE_CAN_NOT_DETERMINE_EXISTANCE_OF_FILE, "Unable to determine if file exists!");
		}
		
		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,method);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		resProp.setProperty(MethodConstants.PROP_FILE_EXISTS,String.valueOf(exists ? 1 : 0));
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