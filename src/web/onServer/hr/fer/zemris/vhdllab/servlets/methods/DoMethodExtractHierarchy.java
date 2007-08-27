package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;

import java.io.Serializable;

/**
 * This class represents a registered method for "extract hierarchy" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_EXTRACT_HIERARCHY
 */
public class DoMethodExtractHierarchy extends AbstractRegisteredMethod {

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
		Hierarchy h;
		try {
			Project project = labman.loadProject(projectId);
			checkProjectSecurity(method, project);
			h = labman.extractHierarchy(project);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_EXTRACT_HIERARCHY,
					"projectId=" + projectId);
			return;
		}
		method.setResult(h);
	}

}