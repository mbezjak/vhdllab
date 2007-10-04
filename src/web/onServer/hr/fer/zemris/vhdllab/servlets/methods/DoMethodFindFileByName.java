package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.Method;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "find file by name" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_FIND_FILE_BY_NAME
 */
public class DoMethodFindFileByName extends AbstractRegisteredMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(Method<Serializable> method, ManagerProvider provider, HttpServletRequest request) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		Long projectId = method.getParameter(Long.class, PROP_ID);
		String fileName = method.getParameter(String.class, PROP_FILE_NAME);
		if (projectId == null || fileName == null) {
			return;
		}
		File file;
		try {
			file = labman.findFileByName(projectId, fileName);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_FIND_FILE, "projectId=" + projectId
					+ ", name=" + fileName);
			return;
		}
		checkFileSecurity(request, method, file);
		method.setResult(file.getId());
	}

}