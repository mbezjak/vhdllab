package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "create file" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_CREATE_NEW_FILE
 */
public class DoMethodCreateFile extends AbstractRegisteredMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(IMethod<Serializable> method, ManagerProvider provider, HttpServletRequest request) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		Long projectId = method.getParameter(Long.class, PROP_ID);
		String fileName = method.getParameter(String.class, PROP_FILE_NAME);
		String fileType = method.getParameter(String.class, PROP_FILE_TYPE);
		if (projectId == null || fileName == null || fileType == null) {
			return;
		}
		File file;
		try {
			Project project = labman.loadProject(projectId);
			checkProjectSecurity(method, project);
			file = labman.createNewFile(project, fileName, fileType);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_CREATE_FILE, "projectId=" + projectId
					+ ", name=" + fileName + ", type=" + fileType);
			return;
		}
		method.setResult(file.getId());
	}

}