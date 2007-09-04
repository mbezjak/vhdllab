package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;

/**
 * This class represents a registered method for "report application error" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_EXISTS_FILE
 */
public class DoMethodReportApplicationError extends AbstractRegisteredMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(IMethod<Serializable> method, ManagerProvider provider) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		String content = method.getParameter(String.class, PROP_FILE_CONTENT);
		if (content == null) {
			return;
		}
		String userId = method.getUserId();
		try {
			labman.saveErrorMessage(userId, content);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_SAVE_FILE, "userId=" + userId);
			return;
		}
		method.setResult(null);
	}
	
}