package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;

/**
 * This class represents a registered method for "save project" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodSaveProject implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.service.VHDLLabManager)
	 */
	public Properties run(Properties p, VHDLLabManager labman) {
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String projectID = p.getProperty(MethodConstants.PROP_PROJECT_ID,null);
		if(projectID == null) return errorProperties(method, MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No project ID specified!");
		
		Long id = null;
		try {
			id = Long.parseLong(projectID);
		} catch (NumberFormatException e) {
			return errorProperties(method, MethodConstants.SE_PARSE_ERROR,"Unable to parse project ID ("+projectID+")!");
		}
		
		// Load Project
		Project project = null;
		try {
			project = labman.loadProject(id);
		} catch (ServiceException e) {
			project = null;
		}
		if(project == null) return errorProperties(method, MethodConstants.SE_NO_SUCH_PROJECT, "Project ("+projectID+") not found!");
		
		int i = 1;
		do {
			String fileId = p.getProperty(MethodConstants.PROP_FILE_ID+i);
			if(fileId == null) break;
			File file = null;
			try {
				id = Long.parseLong(fileId);
				file = labman.loadFile(id);
			} catch (NumberFormatException e) {
				return errorProperties(method, MethodConstants.SE_PARSE_ERROR,"Unable to parse file ID!");
			} catch (ServiceException e) {
				file = null;
			}
			if(file == null) return errorProperties(method, MethodConstants.SE_NO_SUCH_FILE, "File not found!");
			
			project.getFiles().add(file);
			i++;
		} while(true);
		
		try {
			labman.saveProject(project);
		} catch (ServiceException e) {
			return errorProperties(method, MethodConstants.SE_CAN_NOT_SAVE_PROJECT,"Project could not be saved.");
		}
		
		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,MethodConstants.MTD_SAVE_PROJECT);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
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