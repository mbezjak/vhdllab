package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "create project" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodCreateProject extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
        Caseless projectName = method.getParameter(Caseless.class,
				PROP_PROJECT_NAME);
		if (projectName == null) {
			return;
		}
		Caseless userId = method.getUserId();
		Project project;
		try {
		    project = new Project(userId, projectName);
		    container.getProjectManager().save(project);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_CREATE_PROJECT, "name=" + projectName
					+ ", userId=" + userId);
			return;
		}
		method.setResult(project.getId());
	}

}