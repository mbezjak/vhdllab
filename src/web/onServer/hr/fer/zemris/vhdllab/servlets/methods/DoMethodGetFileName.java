package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.JavaToAjaxRegisteredMethod;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class represents a registered method for "load.file.name" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodGetFileName implements JavaToAjaxRegisteredMethod {

	/**
	 * This method is called when "load.file.name" request is received.
	 * @param p Properties representing accepted request
	 * @param request http servlet request
	 * @param response http servlet response
	 * @param labman a lab manager from where to draw data
	 * @throws IOException
	 */
	public Properties run(Properties p, HttpServletRequest request, HttpServletResponse response, VHDLLabManager labman) throws IOException {
		String fileID = p.getProperty(MethodConstants.PROP_FILE_ID,null);
		if(fileID == null) {
			return errorProperties("No file ID specified!", request, response);
		}
		Long id = null;
		try {
			id = Long.parseLong(fileID);
		} catch (NumberFormatException e) {
			return errorProperties("Unable to parse file ID!", request, response);
		}
		
		// Load file
		File file = null;
		try {
			file = labman.loadFile(id);
		} catch (ServiceException e) {
			file = null;
		}
		if(file==null) {
			return errorProperties("File not found!", request, response);
		}
		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,MethodConstants.MTD_LOAD_FILE_NAME);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		resProp.setProperty(MethodConstants.PROP_FILE_NAME,file.getFileName());
		return resProp;
	}
	
	/**
	 * This method is called if errors occur.
	 * @param errorMessage Error message to pass back to caller
	 * @param request http servlet request
	 * @param response http servlet response
	 * @throws IOException
	 */
	private Properties errorProperties(String errorMessage, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_ERROR);
		resProp.setProperty(MethodConstants.STATUS_CONTENT,errorMessage);
		return resProp;
	}	
}