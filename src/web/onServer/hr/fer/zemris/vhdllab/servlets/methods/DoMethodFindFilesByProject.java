package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.Method;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "find files by project" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_FIND_FILES_BY_PROJECT
 */
public class DoMethodFindFilesByProject extends AbstractRegisteredMethod {

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
		if (projectId == null) {
			return;
		}
		Project project;
		try {
			project = labman.loadProject(projectId);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_FIND_PROJECT, "projectId=" + projectId);
			return;
		}
		checkProjectSecurity(request, method, project);
		ArrayList<Long> files = new ArrayList<Long>(project.getFiles().size());
		for(File f : project.getFiles()) {
			files.add(f.getId());
		}
		method.setResult(files);
	}
	
}