package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "exists user file (2)" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodExistsUserFile2 extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
        Caseless fileName = method.getParameter(Caseless.class, PROP_FILE_NAME);
		if (fileName == null) {
			return;
		}
		Caseless userId = method.getUserId();
		boolean exists;
		try {
			exists = container.getUserFileManager().exists(userId, fileName);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_DETERMINE_EXISTANCE_OF_FILE,
					"userId=" + userId + ", name=" + fileName);
			return;
		}
		method.setResult(Boolean.valueOf(exists));
	}
	
}