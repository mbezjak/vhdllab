package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "exists project (2)" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodExistsProject2 extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
		String projectName = method.getParameter(String.class, PROP_PROJECT_NAME);
		if (projectName == null) {
			return;
		}
		String userId = method.getUserId();
		boolean exists;
		try {
			exists = container.getProjectManager().exists(userId, projectName);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_DETERMINE_EXISTANCE_OF_PROJECT,
					"userId=" + userId + ", name=" + projectName);
			return;
		}
		method.setResult(Boolean.valueOf(exists));
	}

}