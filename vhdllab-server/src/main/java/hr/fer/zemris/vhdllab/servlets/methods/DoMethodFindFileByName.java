package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "find file by name" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodFindFileByName extends AbstractRegisteredMethod {

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
        Caseless fileName = method.getParameter(Caseless.class, PROP_FILE_NAME);
		if (projectId == null || fileName == null) {
			return;
		}
		File file;
		try {
			file = container.getFileManager().findByName(projectId, fileName);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_FIND_FILE, "projectId=" + projectId
					+ ", name=" + fileName);
			return;
		}
		checkFileSecurity(request, method, file);
		method.setResult(file.getId());
	}

}