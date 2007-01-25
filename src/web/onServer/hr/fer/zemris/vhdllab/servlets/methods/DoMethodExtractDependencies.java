package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.List;
import java.util.Properties;

/**
 * This class represents a registered method for "extract dependencies" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_EXTRACT_DEPENDENCIES
 */
public class DoMethodExtractDependencies implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties run(Properties p, ManagerProvider mprov) {
		VHDLLabManager labman = (VHDLLabManager)mprov.get(ManagerProvider.VHDL_LAB_MANAGER);
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String fileID = p.getProperty(MethodConstants.PROP_FILE_ID,null);
		if(fileID==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No file ID specified!");
		
		// Load files
		List<File> files;
		try {
			Long id = Long.parseLong(fileID);
			File file = labman.loadFile(id);
			files = labman.extractDependencies(file);
		} catch (NumberFormatException e) {
			return errorProperties(method,MethodConstants.SE_PARSE_ERROR,"Unable to parse file ID = '"+fileID+"'!");
		} catch (Exception e) {
			return errorProperties(method,MethodConstants.SE_CAN_NOT_EXTRACT_DEPENDENCIES, "Can not extract dependencies for file id = '"+fileID+"'!");
		}
		
		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,method);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		int i = 1;
		for(File f : files) {
			resProp.setProperty(MethodConstants.PROP_FILE_ID+"."+i, String.valueOf(f.getId()));
			i++;
		}
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