/**
 * 
 */
package hr.fer.zemris.vhdllab.servlets;

import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.model.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.utilities.ModelUtil;

import java.io.Serializable;

/**
 * @author Miro Bezjak
 * 
 */
public abstract class AbstractRegisteredMethod implements RegisteredMethod {

	protected VHDLLabManager getVHDLLabManager(ManagerProvider provider) {
		return (VHDLLabManager) provider.get(ManagerProvider.VHDL_LAB_MANAGER);
	}

	protected void checkUserFileSecurity(IMethod<Serializable> method,
			VHDLLabManager labman, Long fileId) throws ServiceException {
		UserFile file = labman.loadUserFile(fileId);
		checkUserFileSecurity(method, file);
	}

	protected void checkUserFileSecurity(IMethod<Serializable> method,
			UserFile file) {
		String fileUser = file.getOwnerID();
		checkUsersForSecurity(fileUser, method.getUserId());
	}

	protected void checkFileSecurity(IMethod<Serializable> method,
			VHDLLabManager labman, Long fileId) throws ServiceException {
		File file = labman.loadFile(fileId);
		checkFileSecurity(method, file);
	}

	protected void checkFileSecurity(IMethod<Serializable> method, File file) {
		checkProjectSecurity(method, file.getProject());
	}

	protected void checkProjectSecurity(IMethod<Serializable> method,
			VHDLLabManager labman, Long projectId) throws ServiceException {
		Project project = labman.loadProject(projectId);
		checkProjectSecurity(method, project);
	}

	protected void checkProjectSecurity(IMethod<Serializable> method,
			Project project) {
		String projectUser = project.getOwnerId();
		checkUsersForSecurity(projectUser, method.getUserId());
	}

	protected void checkUsersForSecurity(String resourceUser, String methodUser) {
		if (!ModelUtil.userIdAreEqual(resourceUser, methodUser)) {
			throw new SecurityException();
		}
	}

}
