package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.UserFileManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "save user file" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodSaveUserFile extends AbstractRegisteredMethod {

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
		String content = method.getParameter(String.class, PROP_FILE_CONTENT);
		if (fileId == null || content == null) {
			return;
		}
		try {
			checkUserFileSecurity(request, method, fileId);
			UserFileManager man = container.getUserFileManager();
			UserFile file = man.load(fileId);
			file.setData(content);
			man.save(file);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_SAVE_FILE, "fileId=" + fileId);
			return;
		}
		method.setResult(null);
	}
	
}