package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "report application error" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodReportApplicationError extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
		String content = method.getParameter(String.class, PROP_FILE_CONTENT);
		if (content == null) {
			return;
		}
		String userId = method.getUserId();
		try {
		    Library lib = container.getLibraryManager().findByName("errors");
		    LibraryFile file = new LibraryFile(lib, new Date().toString(), FileTypes.ERROR, content);
			container.getLibraryFileManager().save(file);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_SAVE_FILE, "userId=" + userId);
			return;
		}
		method.setResult(null);
	}
	
}