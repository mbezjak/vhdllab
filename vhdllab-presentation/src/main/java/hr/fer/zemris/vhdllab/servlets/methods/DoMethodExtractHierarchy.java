package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "extract hierarchy" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodExtractHierarchy extends AbstractRegisteredMethod {

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
		Hierarchy h;
		try {
			Project project = container.getProjectManager().load(projectId);
			checkProjectSecurity(request, method, project);
			h = container.getServiceManager().extractHierarchy(project);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_EXTRACT_HIERARCHY,
					"projectId=" + projectId);
			// TODO ovo je samo temp
			e.printStackTrace();
			return;
		}
		method.setResult(h);
	}

}