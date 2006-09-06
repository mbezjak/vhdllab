package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.JavaToAjaxRegisteredMethod;
import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;

import java.util.List;
import java.util.Properties;

/**
 * This class represents a registered method for "find project by user" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodFindProjectsByUser implements JavaToAjaxRegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.ajax.shared.JavaToAjaxRegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.service.VHDLLabManager)
	 */
	public Properties run(Properties p, VHDLLabManager labman) {
		String ownerID = p.getProperty(MethodConstants.PROP_PROJECT_ID,null);
		if(ownerID == null) return errorProperties(MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No owner ID specified!");

		Long id = null;
		try {
			id = Long.parseLong(ownerID);
		} catch (NumberFormatException e) {
			return errorProperties(MethodConstants.SE_PARSE_ERROR,"Unable to parse owner ID!");
		}
		
		// Find projects
		List<Project> projects = null;
		try {
			projects = labman.findProjectsByUser(id);
		} catch (ServiceException e) {
			projects = null;
		}
		if(projects==null) return errorProperties(MethodConstants.SE_NO_SUCH_PROJECT,"Projects not found!");

		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,MethodConstants.MTD_LOAD_PROJECT_NAME);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		int i = 1;
		for(Project project : projects) {
			resProp.setProperty(MethodConstants.PROP_PROJECT_ID+i, String.valueOf(project.getId()));
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