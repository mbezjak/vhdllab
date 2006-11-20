package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;

/**
 * This class represents a registered method for "find files by project" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_FIND_FILES_BY_PROJECT
 */
public class DoMethodFindFilesByProject implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties run(Properties p, ManagerProvider mprov) {
		VHDLLabManager labman = (VHDLLabManager)mprov.get("vhdlLabManager");
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String projectID = p.getProperty(MethodConstants.PROP_PROJECT_ID,null);
		if(projectID==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No project ID specified!");

		// Find project
		Project project;
		try {
			Long id = Long.parseLong(projectID);
			project = labman.loadProject(id);
		} catch (NumberFormatException e) {
			return errorProperties(method,MethodConstants.SE_PARSE_ERROR,"Unable to parse project ID = '"+projectID+"'!");
		} catch (ServiceException e) {
			project = null;
		}
		if(project==null || project.getFiles().size() == 0) return errorProperties(method,MethodConstants.SE_NO_SUCH_PROJECT,"Files for project ("+projectID+") not found!");

		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,method);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		int i = 1;
		for(File f : project.getFiles()) {
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