package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.servlets.RegisteredMethod;

import java.util.Properties;
import java.util.TreeSet;

/**
 * This class represents a registered method for "save project" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_SAVE_PROJECT
 */
public class DoMethodSaveProject implements RegisteredMethod {

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(java.util.Properties, hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	public Properties run(Properties p, ManagerProvider mprov) {
		VHDLLabManager labman = (VHDLLabManager)mprov.get(ManagerProvider.VHDL_LAB_MANAGER);
		String method = p.getProperty(MethodConstants.PROP_METHOD);
		String projectID = p.getProperty(MethodConstants.PROP_PROJECT_ID,null);
		if(projectID==null) return errorProperties(method,MethodConstants.SE_METHOD_ARGUMENT_ERROR,"No project ID specified!");
		
		// Load project
		Project project = null;
		try {
			Long id = Long.parseLong(projectID);
			project = labman.loadProject(id);
		} catch (NumberFormatException e) {
			return errorProperties(method, MethodConstants.SE_PARSE_ERROR,"Unable to parse project ID = '"+projectID+"'!");
		} catch (Exception e) {
			project = null;
		}
		if(project == null) return errorProperties(method, MethodConstants.SE_NO_SUCH_PROJECT, "Project ("+projectID+") not found!");
		
		// Load all files and add them to project
		boolean hasFileArguments = false;
		int i = 0;
		do {
			String fileId = null;
			if(i==0) {
				// Try to load file that does not have number extension
				fileId = p.getProperty(MethodConstants.PROP_FILE_ID);
				if(fileId==null) {
					i++;
					continue;
				}
				hasFileArguments = true;
			} else {
				// Load a list of files. They have number extension: ".[number]"
				fileId = p.getProperty(MethodConstants.PROP_FILE_ID+"."+i);
				if(fileId == null) break;
				hasFileArguments = true;
			}
			
			File file = null;
			try {
				Long id = Long.parseLong(fileId);
				file = labman.loadFile(id);
			} catch (NumberFormatException e) {
				return errorProperties(method, MethodConstants.SE_PARSE_ERROR,"Unable to parse file ID = '"+fileId+"'!");
			} catch (ServiceException e) {
				file = null;
			}
			if(file == null) return errorProperties(method, MethodConstants.SE_NO_SUCH_FILE, "File ("+fileId+") not found!");
			
			// Note that if project already contains same file it will be replaced.
			if(project.getFiles() == null) {
				project.setFiles(new TreeSet<File>());
			}
			project.getFiles().add(file);
			i++;
		} while(true);
		
		if(!hasFileArguments) return errorProperties(method, MethodConstants.SE_METHOD_ARGUMENT_ERROR, "No file ID specified!");
		
		// Save project
		try {
			labman.saveProject(project);
		} catch (ServiceException e) {
			return errorProperties(method, MethodConstants.SE_CAN_NOT_SAVE_PROJECT,"Project ("+projectID+") could not be saved!");
		}
		
		// Prepare response
		Properties resProp = new Properties();
		resProp.setProperty(MethodConstants.PROP_METHOD, method);
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