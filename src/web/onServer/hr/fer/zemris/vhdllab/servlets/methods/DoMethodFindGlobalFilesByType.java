package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.List;
import java.util.Properties;

/**
 * This class represents a registered method for "find global files by type" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_FIND_GLOBAL_FILES_BY_TYPE
 */
public class DoMethodFindGlobalFilesByType implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties run(Properties p, ManagerProvider mprov) {
		VHDLLabManager labman = (VHDLLabManager)mprov.get("vhdlLabManager");
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String type = p.getProperty(MethodConstants.PROP_FILE_TYPE,null);
		if(type==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No file type specified!");

		// Find global files
		List<GlobalFile> files = null;
		try {
			files = labman.findGlobalFilesByType(type);
		} catch (ServiceException e) {
			files = null;
		}
		if(files==null) return errorProperties(method,MethodConstants.SE_NO_SUCH_FILE,"Files with type ='"+type+"' not found!");

		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,method);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		int i = 1;
		for(GlobalFile f : files) {
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