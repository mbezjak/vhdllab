package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;

/**
 * This class represents a registered method for "find file by name" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_FIND_FILE_BY_NAME
 */
public class DoMethodFindFileByName implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties run(Properties p, ManagerProvider mprov) {
		VHDLLabManager labman = (VHDLLabManager)mprov.get(ManagerProvider.VHDL_LAB_MANAGER);
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String projectID = p.getProperty(MethodConstants.PROP_PROJECT_ID,null);
		String fileName = p.getProperty(MethodConstants.PROP_FILE_NAME,null);
		if(projectID==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No project ID specified!");
		if(fileName==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No file name specified!");
		
		// Load file
		File file;
		try {
			Long id = Long.parseLong(projectID);
			file = labman.findFileByName(id, fileName);
		} catch (NumberFormatException e) {
			return errorProperties(method,MethodConstants.SE_PARSE_ERROR,"Unable to parse project ID = '"+projectID+"'!");
		} catch (Exception e) {
			file = null;
		}
		if(file==null) return errorProperties(method,MethodConstants.SE_NO_SUCH_FILE, "File with name ='"+fileName+"' and project id = '"+projectID+"' not found!");
		
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