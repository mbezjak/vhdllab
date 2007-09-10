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
 * This class represents a registered method for "exists project (2)" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_EXISTS_PROJECT2
 */
public class DoMethodExistsProject2 extends AbstractRegisteredMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(Method<Serializable> method, ManagerProvider provider, HttpServletRequest request) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		String projectName = method.getParameter(String.class, PROP_PROJECT_NAME);
		if (projectName == null) {
			return;
		}
		String userId = method.getUserId();
		boolean exists;
		try {
			exists = labman.existsProject(userId, projectName);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_DETERMINE_EXISTANCE_OF_PROJECT,
					"userId=" + userId + ", name=" + projectName);
			return;
		}
		method.setResult(Boolean.valueOf(exists));
	}

}