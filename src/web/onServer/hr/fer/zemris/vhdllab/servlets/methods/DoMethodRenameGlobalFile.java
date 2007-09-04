package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "rename global file" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_RENAME_GLOBAL_FILE
 */
public class DoMethodRenameGlobalFile extends AbstractRegisteredMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(IMethod<Serializable> method, ManagerProvider provider, HttpServletRequest request) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		Long fileId = method.getParameter(Long.class, PROP_ID);
		String newName = method.getParameter(String.class, PROP_FILE_NAME);
		if (fileId == null || newName == null) {
			return;
		}
		try {
			labman.renameGlobalFile(fileId, newName);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_RENAME_FILE, "fileId=" + fileId + ", name="
					+ newName);
			return;
		}
		method.setResult(null);
	}
	
}