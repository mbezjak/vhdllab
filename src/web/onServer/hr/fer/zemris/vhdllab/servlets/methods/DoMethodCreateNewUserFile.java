package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;

/**
 * This class represents a registered method for "create user file" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_CREATE_NEW_USER_FILE
 */
public class DoMethodCreateNewUserFile implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties run(Properties p, ManagerProvider mprov) {
		VHDLLabManager labman = (VHDLLabManager)mprov.get("vhdlLabManager");
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String ownerId = p.getProperty(MethodConstants.PROP_FILE_OWNER_ID,null);
		String fileType = p.getProperty(MethodConstants.PROP_FILE_TYPE,null);
		if(ownerId==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No file name specified!");
		if(fileType==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No file type specified!");
		
		// Create new user file
		UserFile file = null;
		try {
			Long id = Long.parseLong(ownerId);
			file = labman.createNewUserFile(id, fileType);
		} catch (NumberFormatException e) {
			return errorProperties(method,MethodConstants.SE_PARSE_ERROR,"Unable to parse owner ID = '"+ownerId+"'!");
		} catch (ServiceException e) {
			file = null;
		}
		if(file == null) return errorProperties(method,MethodConstants.SE_CAN_NOT_CREATE_FILE, "Unable to create a new file!");
		
		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,method);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		resProp.setProperty(MethodConstants.PROP_FILE_ID,String.valueOf(file.getId()));
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