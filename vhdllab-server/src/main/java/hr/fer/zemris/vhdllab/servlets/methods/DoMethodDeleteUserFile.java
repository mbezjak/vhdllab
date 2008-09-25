package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "delete user file" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodDeleteUserFile extends AbstractRegisteredMethod {


    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
        Integer fileId = method.getParameter(Integer.class, PROP_ID);
		if (fileId == null) {
			return;
		}
		try {
			checkUserFileSecurity(request, method, fileId);
			container.getUserFileManager().delete(fileId);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_DELETE_FILE, "fileId=" + fileId);
			return;
		}
		method.setResult(null);
	}

}