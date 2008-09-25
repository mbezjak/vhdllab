package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "delete project" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodDeleteProject extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
        Integer projectId = method.getParameter(Integer.class, PROP_ID);
		if (projectId == null) {
			return;
		}
		try {
			checkProjectSecurity(request, method, projectId);
			container.getProjectManager().delete(projectId);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_DELETE_PROJECT, "projectId=" + projectId);
			return;
		}
		method.setResult(null);
	}
	
}