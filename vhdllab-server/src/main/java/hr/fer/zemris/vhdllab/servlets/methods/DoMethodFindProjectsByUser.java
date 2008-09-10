package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "find project by user" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodFindProjectsByUser extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
		String userId = method.getUserId();
		List<Project> projects;
		try {
			projects = container.getProjectManager().findByUser(userId);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_FIND_PROJECT, "userId=" + userId);
			return;
		}
		ArrayList<Long> ids = new ArrayList<Long>(projects.size());
		for(Project p : projects) {
			ids.add(p.getId());
		}
		method.setResult(ids);
	}
	
}