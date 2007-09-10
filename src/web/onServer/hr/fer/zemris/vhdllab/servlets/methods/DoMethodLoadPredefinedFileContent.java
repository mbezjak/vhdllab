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
 * This class represents a registered method for "load predefined file content" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_LOAD_FILE_CONTENT
 */
public class DoMethodLoadPredefinedFileContent extends AbstractRegisteredMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(Method<Serializable> method, ManagerProvider provider, HttpServletRequest request) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		String fileName = method.getParameter(String.class, PROP_FILE_NAME);
		if (fileName == null) {
			return;
		}
		File file;
		try {
			file = labman.getPredefinedFile(fileName, true);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_FIND_FILE, "fileName=" + fileName);
			return;
		}
		method.setResult(file.getContent());
	}
	
}