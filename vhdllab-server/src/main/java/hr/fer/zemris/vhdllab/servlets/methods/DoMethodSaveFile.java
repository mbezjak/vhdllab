package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.FileManager;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "save file" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodSaveFile extends AbstractRegisteredMethod {

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
			checkFileSecurity(request, method, fileId);
			FileManager man = container.getFileManager();
			File file = man.load(fileId);
			file.setData(content);
			man.save(file);
		} catch (ServiceException e) {
            if(e.getStatusCode() == StatusCodes.SERVICE_ENTITY_AND_FILE_NAME_DONT_MATCH) {
                method.setStatus(e.getStatusCode(), e.getMessage());
            } else {
                method.setStatus(SE_CAN_NOT_SAVE_FILE, "fileId=" + fileId);
            }
			return;
		}
		method.setResult(null);
	}
	
}