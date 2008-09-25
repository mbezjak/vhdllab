package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "find user files by user" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodFindUserFilesByUser extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
        Caseless userId = method.getUserId();
		List<UserFile> files;
		try {
			files = container.getUserFileManager().findByUser(userId);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_FIND_FILE, "userId=" + userId);
			return;
		}
		ArrayList<Integer> ids = new ArrayList<Integer>(files.size());
		for(UserFile f : files) {
			ids.add(f.getId());
		}
		method.setResult(ids);
	}
	
}