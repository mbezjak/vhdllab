package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.Method;
import hr.fer.zemris.vhdllab.model.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "find user files by user" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_FIND_USER_FILES_BY_USER
 */
public class DoMethodFindUserFilesByUser extends AbstractRegisteredMethod {

	/* (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(Method<Serializable> method, ManagerProvider provider, HttpServletRequest request) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		String userId = method.getUserId();
		List<UserFile> files;
		try {
			files = labman.findUserFilesByUser(userId);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_FIND_FILE, "userId=" + userId);
			return;
		}
		ArrayList<Long> ids = new ArrayList<Long>(files.size());
		for(UserFile f : files) {
			ids.add(f.getId());
		}
		method.setResult(ids);
	}
	
}