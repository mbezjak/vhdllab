package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a registered method for "extract dependencies" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_EXTRACT_DEPENDENCIES
 */
public class DoMethodExtractDependencies extends AbstractRegisteredMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(IMethod<Serializable> method, ManagerProvider provider) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		Long fileId = method.getParameter(Long.class, PROP_ID);
		if (fileId == null) {
			return;
		}
		List<File> files;
		try {
			File file = labman.loadFile(fileId);
			checkFileSecurity(method, file);
			files = labman.extractDependencies(file);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_EXTRACT_DEPENDENCIES,
					"fileId=" + fileId);
			return;
		}
		ArrayList<Long> dependencies = new ArrayList<Long>(files.size());
		for(File f : files) {
			dependencies.add(f.getId());
		}
		method.setResult(dependencies);
	}
	
}