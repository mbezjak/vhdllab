package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "load project name" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodLoadProjectName extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
		Long projectId = method.getParameter(Long.class, PROP_ID);
		if (projectId == null) {
			return;
		}
		Project project;
		try {
			project = container.getProjectManager().load(projectId);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_FIND_PROJECT, "projectId=" + projectId);
			return;
		}
		checkProjectSecurity(request, method, project);
		method.setResult(project.getName());
	}
	
}