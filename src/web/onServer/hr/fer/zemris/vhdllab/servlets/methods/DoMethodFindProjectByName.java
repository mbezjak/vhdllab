package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "get project identifier"
 * request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_GET_PROJECT_IDENTIFIER
 */
public class DoMethodFindProjectByName extends AbstractRegisteredMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(IMethod<Serializable> method, ManagerProvider provider, HttpServletRequest request) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		String projectName = method.getParameter(String.class,
				PROP_PROJECT_NAME);
		if (projectName == null) {
			return;
		}
		String userId = method.getUserId();
		Project project;
		try {
			project = labman.findProjectByName(userId, projectName);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_FIND_PROJECT, "userId=" + userId
					+ ", name=" + projectName);
			return;
		}
		method.setResult(project.getId());
	}

}