package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.Method;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "save user file" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_SAVE_USER_FILE
 */
public class DoMethodSaveUserFile extends AbstractRegisteredMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(Method<Serializable> method, ManagerProvider provider, HttpServletRequest request) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		Long fileId = method.getParameter(Long.class, PROP_ID);
		String content = method.getParameter(String.class, PROP_FILE_CONTENT);
		if (fileId == null || content == null) {
			return;
		}
		try {
			checkUserFileSecurity(method, labman, fileId);
			labman.saveUserFile(fileId, content);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_SAVE_FILE, "fileId=" + fileId);
			return;
		}
		method.setResult(null);
	}
	
}