package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;

/**
 * This class represents a registered method for "rename file" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodRenameFile implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.service.VHDLLabManager)
	 */
	public Properties run(Properties p, VHDLLabManager labman) {
		String fileID = p.getProperty(MethodConstants.PROP_FILE_ID,null);
		String newName = p.getProperty(MethodConstants.PROP_FILE_NAME,null);
		if(fileID == null) return errorProperties(MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No file ID specified!");
		if(newName == null) return errorProperties(MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No file name specified!");

		Long id = null;
		try {
			id = Long.parseLong(fileID);
		} catch (NumberFormatException e) {
			return errorProperties(MethodConstants.SE_PARSE_ERROR,"Unable to parse file ID!");
		}
		
		// Rename file
		try {
			labman.renameFile(id, newName);
		} catch (ServiceException e) {
			return errorProperties(MethodConstants.SE_CAN_NOT_RENAME_FILE,"File could not be renamed.");
		}
		
		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,MethodConstants.MTD_RENAME_FILE);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
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