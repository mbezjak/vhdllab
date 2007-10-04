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
 * This class represents a registered method for "exists user file" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_EXISTS_USER_FILE
 */
public class DoMethodExistsUserFile extends AbstractRegisteredMethod {

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
		if (fileId == null) {
			return;
		}
		boolean exists;
		try {
			checkUserFileSecurity(request, method, labman, fileId);
			exists = labman.existsUserFile(fileId);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_DETERMINE_EXISTANCE_OF_FILE,
					"fileId=" + fileId);
			return;
		}
		method.setResult(Boolean.valueOf(exists));
	}
	
}