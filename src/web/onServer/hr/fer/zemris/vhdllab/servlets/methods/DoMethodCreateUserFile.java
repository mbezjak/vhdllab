package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.Method;
import hr.fer.zemris.vhdllab.model.UserFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "create user file" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_CREATE_NEW_USER_FILE
 */
public class DoMethodCreateUserFile extends AbstractRegisteredMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(Method<Serializable> method, ManagerProvider provider, HttpServletRequest request) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		String fileName = method.getParameter(String.class, PROP_FILE_NAME);
		String fileType = method.getParameter(String.class, PROP_FILE_TYPE);
		if (fileName == null || fileType == null) {
			return;
		}
		String userId = method.getUserId();
		UserFile file;
		try {
			file = labman.createNewUserFile(userId, fileName, fileType);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_CREATE_FILE, "userId=" + userId
					+ ", name=" + fileName + ", type=" + fileType);
			return;
		}
		method.setResult(file.getId());
	}

}