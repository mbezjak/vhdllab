package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.JavaToAjaxRegisteredMethod;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;

import java.util.Properties;
import java.util.Set;

/**
 * This class represents a registered method for "load project's id of files" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodLoadProjectFilesId implements JavaToAjaxRegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.JavaToAjaxRegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.service.VHDLLabManager)
	 */
	public Properties run(Properties p, VHDLLabManager labman) {
		String projectID = p.getProperty(MethodConstants.PROP_PROJECT_ID,null);
		if(projectID == null) return errorProperties(MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No project ID specified!");

		Long id = null;
		try {
			id = Long.parseLong(projectID);
		} catch (NumberFormatException e) {
			return errorProperties(MethodConstants.SE_PARSE_ERROR,"Unable to parse project ID!");
		}
		
		// Load files of a project
		Set<File> files = null;
		try {
			Project project = labman.loadProject(id);
			files = project.getFiles();
		} catch (ServiceException e) {
			files = null;
		}
		if(files==null) return errorProperties(MethodConstants.SE_NO_SUCH_PROJECT,"Project not found!");

		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,MethodConstants.MTD_LOAD_PROJECT_FILES_ID);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		int i = 1;
		for(File f : files) {
			resProp.setProperty(MethodConstants.PROP_FILE_ID+"."+i,String.valueOf(f.getId()));
			i++;
		}
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
		resProp.setProperty(MethodConstants.STATUS_CONTENT,errorMessage);
		return resProp;
	}
}