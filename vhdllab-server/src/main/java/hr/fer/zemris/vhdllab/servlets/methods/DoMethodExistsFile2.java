package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "exists file" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodExistsFile2 extends AbstractRegisteredMethod {

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
		String fileName = method.getParameter(String.class, PROP_FILE_NAME);
		if (projectId == null || fileName == null) {
			return;
		}
		boolean exists;
		try {
			checkProjectSecurity(request, method, projectId);
			exists = container.getFileManager().exists(projectId, fileName);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_DETERMINE_EXISTANCE_OF_FILE,
					"projectId=" + projectId + ", name=" + fileName);
			return;
		}
		method.setResult(Boolean.valueOf(exists));
	}

}