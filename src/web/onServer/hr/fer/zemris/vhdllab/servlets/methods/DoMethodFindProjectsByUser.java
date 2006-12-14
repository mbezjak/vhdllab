package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.List;
import java.util.Properties;

/**
 * This class represents a registered method for "find project by user" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_FIND_PROJECTS_BY_USER
 */
public class DoMethodFindProjectsByUser implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties run(Properties p, ManagerProvider mprov) {
		VHDLLabManager labman = (VHDLLabManager)mprov.get("vhdlLabManager");
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String ownerID = p.getProperty(MethodConstants.PROP_PROJECT_OWNER_ID,null);
		if(ownerID==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No owner ID specified!");

		// Find projects
		List<Project> projects = null;
		try {
			projects = labman.findProjectsByUser(ownerID);
		} catch (NumberFormatException e) {
			return errorProperties(method,MethodConstants.SE_PARSE_ERROR,"Unable to parse owner ID = '"+ownerID+"'!");
		} catch (Exception e) {
			projects = null;
		}
		if(projects==null) return errorProperties(method,MethodConstants.SE_NO_SUCH_PROJECT,"Projects for owner ("+ownerID+") not found!");

		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD,method);
		resProp.setProperty(MethodConstants.PROP_STATUS,MethodConstants.STATUS_OK);
		int i = 1;
		for(Project project : projects) {
			resProp.setProperty(MethodConstants.PROP_PROJECT_ID+"."+i, String.valueOf(project.getId()));
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