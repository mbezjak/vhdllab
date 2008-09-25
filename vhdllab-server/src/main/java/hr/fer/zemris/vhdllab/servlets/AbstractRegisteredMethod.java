package hr.fer.zemris.vhdllab.servlets;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceException;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Miro Bezjak
 * 
 */
public abstract class AbstractRegisteredMethod implements RegisteredMethod {
    
    protected final ServiceContainer container;
    
    public AbstractRegisteredMethod() {
        container = ServiceContainer.instance();
    }

	protected void checkUserFileSecurity(HttpServletRequest request, Method<Serializable> method,
	        Integer fileId) throws ServiceException {
		UserFile file = container.getUserFileManager().load(fileId);
		checkUserFileSecurity(request, method, file);
	}

	protected void checkUserFileSecurity(HttpServletRequest request, Method<Serializable> method,
			UserFile file) {
	    Caseless fileUser = file.getUserId();
		checkUsersForSecurity(request, fileUser, method.getUserId());
	}

	protected void checkFileSecurity(HttpServletRequest request, Method<Serializable> method,
	        Integer fileId) throws ServiceException {
		File file = container.getFileManager().load(fileId);
		checkFileSecurity(request, method, file);
	}

	protected void checkFileSecurity(HttpServletRequest request, Method<Serializable> method, File file) {
		checkProjectSecurity(request, method, file.getProject());
	}

	protected void checkProjectSecurity(HttpServletRequest request, Method<Serializable> method,
	        Integer projectId) throws ServiceException {
		Project project = container.getProjectManager().load(projectId);
		checkProjectSecurity(request, method, project);
	}

	protected void checkProjectSecurity(HttpServletRequest request, Method<Serializable> method,
			Project project) {
	    Caseless projectUser = project.getUserId();
		checkUsersForSecurity(request, projectUser, method.getUserId());
	}

	protected void checkUsersForSecurity(HttpServletRequest request, Caseless resourceUser, Caseless methodUser) {
		if (!resourceUser.equals(methodUser) && !request.isUserInRole("admin")) {
			throw new SecurityException();
		}
	}

}
