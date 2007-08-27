package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;

/**
 * This class represents a registered method for "exists project" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_EXISTS_PROJECT
 */
public class DoMethodExistsProject extends AbstractRegisteredMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(IMethod<Serializable> method, ManagerProvider provider) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		Long projectId = method.getParameter(Long.class, PROP_ID);
		if (projectId == null) {
			return;
		}
		boolean exists;
		try {
			checkProjectSecurity(method, labman, projectId);
			exists = labman.existsProject(projectId);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_DETERMINE_EXISTANCE_OF_PROJECT,
					"projectId=" + projectId);
			return;
		}
		method.setResult(Boolean.valueOf(exists));
	}
	
}