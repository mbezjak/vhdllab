package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;

/**
 * This class represents a registered method for "delete global file" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_DELETE_GLOBAL_FILE
 */
public class DoMethodDeleteGlobalFile extends AbstractRegisteredMethod {

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
		try {
			labman.deleteGlobalFile(fileId);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_DELETE_FILE, "fileId=" + fileId);
			return;
		}
		method.setResult(null);
	}
	
}