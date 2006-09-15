package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;

/**
 * This class represents a registered method for "generate shema VHDL" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodGenerateShemaVHDL implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.service.VHDLLabManager)
	 */
	public Properties run(Properties p, VHDLLabManager labman) {
		String fileID = p.getProperty(MethodConstants.PROP_FILE_ID,null);
		if(fileID == null) return errorProperties(MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No file ID specified!");

		Long id = null;
		try {
			id = Long.parseLong(fileID);
		} catch (NumberFormatException e) {
			return errorProperties(MethodConstants.SE_PARSE_ERROR,"Unable to parse file ID!");
		}
		
		// Generate shema VHDL
		String vhdl = null;
		try {
			File file = labman.loadFile(id);
			vhdl = labman.generateShemaVHDL(file);
		} catch (ServiceException e) {
			vhdl = null;
		}
		if(vhdl==null) return errorProperties(MethodConstants.SE_CAN_NOT_GENERATE_VHDL_SHEMA,"Can not generate shema VHDL!");

		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,MethodConstants.MTD_GENERATE_SHEMA_VHDL);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		resProp.setProperty(MethodConstants.PROP_FILE_NAME,vhdl);
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