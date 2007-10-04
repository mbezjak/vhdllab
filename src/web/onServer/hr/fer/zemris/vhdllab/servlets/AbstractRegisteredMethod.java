/**
 * 
 */
package hr.fer.zemris.vhdllab.servlets;

import hr.fer.zemris.vhdllab.communicaton.Method;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.model.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.utilities.ModelUtil;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Miro Bezjak
 * 
 */
public abstract class AbstractRegisteredMethod implements RegisteredMethod {

	protected VHDLLabManager getVHDLLabManager(ManagerProvider provider) {
		return (VHDLLabManager) provider.get(ManagerProvider.VHDL_LAB_MANAGER);
	}

	protected void checkUserFileSecurity(HttpServletRequest request, Method<Serializable> method,
			VHDLLabManager labman, Long fileId) throws ServiceException {
		UserFile file = labman.loadUserFile(fileId);
		checkUserFileSecurity(request, method, file);
	}

	protected void checkUserFileSecurity(HttpServletRequest request, Method<Serializable> method,
			UserFile file) {
		String fileUser = file.getOwnerID();
		checkUsersForSecurity(request, fileUser, method.getUserId());
	}

	protected void checkFileSecurity(HttpServletRequest request, Method<Serializable> method,
			VHDLLabManager labman, Long fileId) throws ServiceException {
		File file = labman.loadFile(fileId);
		checkFileSecurity(request, method, file);
	}

	protected void checkFileSecurity(HttpServletRequest request, Method<Serializable> method, File file) {
		checkProjectSecurity(request, method, file.getProject());
	}

	protected void checkProjectSecurity(HttpServletRequest request, Method<Serializable> method,
			VHDLLabManager labman, Long projectId) throws ServiceException {
		Project project = labman.loadProject(projectId);
		checkProjectSecurity(request, method, project);
	}

	protected void checkProjectSecurity(HttpServletRequest request, Method<Serializable> method,
			Project project) {
		String projectUser = project.getOwnerId();
		checkUsersForSecurity(request, projectUser, method.getUserId());
	}

	protected void checkUsersForSecurity(HttpServletRequest request, String resourceUser, String methodUser) {
		if (!ModelUtil.userIdAreEqual(resourceUser, methodUser) && !request.isUserInRole("admin")) {
			throw new SecurityException();
		}
	}

}
