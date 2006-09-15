package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;

/**
 * This class represents a registered method for "save file" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodSaveFile implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.service.VHDLLabManager)
	 */
	public Properties run(Properties p, VHDLLabManager labman) {
		String fileID = p.getProperty(MethodConstants.PROP_FILE_ID,null);
		String content = p.getProperty(MethodConstants.PROP_FILE_CONTENT,null);
		if(fileID == null) return errorProperties(MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No file ID specified!");
		if(content == null) return errorProperties(MethodConstants.SE_METHOD_ARGUMENT_ERROR, "No file content specified!");
		
		Long id = null;
		try {
			id = Long.parseLong(fileID);
		} catch (NumberFormatException e) {
			return errorProperties(MethodConstants.SE_PARSE_ERROR,"Unable to parse file ID!");
		}
		
		// Save file
		try {
			labman.saveFile(id, content);
		} catch (ServiceException e) {
			return errorProperties(MethodConstants.SE_CAN_NOT_SAVE_FILE,"File could not be saved.");
		}
		
		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,MethodConstants.MTD_SAVE_FILE);
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