package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;

/**
 * This class represents a registered method for "rename project" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_RENAME_PROJECT
 */
public class DoMethodRenameProject extends AbstractRegisteredMethod {

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
		String newName = method.getParameter(String.class, PROP_FILE_NAME);
		if (projectId == null || newName == null) {
			return;
		}
		try {
			checkProjectSecurity(method, labman, projectId);
			labman.renameProject(projectId, newName);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_RENAME_PROJECT, "projectId=" + projectId + ", name="
					+ newName);
			return;
		}
		method.setResult(null);
	}
	
}