package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;

/**
 * This class represents a registered method for "create global file" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_CREATE_NEW_GLOBAL_FILE
 */
public class DoMethodCreateGlobalFile extends AbstractRegisteredMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(IMethod<Serializable> method, ManagerProvider provider) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		String fileName = method.getParameter(String.class, PROP_FILE_NAME);
		String fileType = method.getParameter(String.class, PROP_FILE_TYPE);
		if (fileName == null || fileType == null) {
			return;
		}
		GlobalFile file;
		try {
			file = labman.createNewGlobalFile(fileName, fileType);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_CREATE_FILE, "name=" + fileName
					+ ", type=" + fileType);
			return;
		}
		method.setResult(file.getId());
	}

}