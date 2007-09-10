package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.Method;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "extract circuit interface"
 * request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_EXTRACT_CIRCUIT_INTERFACE
 */
public class DoMethodExtractCircuitInterface extends AbstractRegisteredMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(Method<Serializable> method, ManagerProvider provider, HttpServletRequest request) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		Long fileId = method.getParameter(Long.class, PROP_ID);
		if (fileId == null) {
			return;
		}
		CircuitInterface ci;
		try {
			File file = labman.loadFile(fileId);
			checkFileSecurity(method, file);
			ci = labman.extractCircuitInterface(file);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_EXTRACT_CIRCUIT_INTERFACE,
					"fileId=" + fileId);
			return;
		}
		method.setResult(ci);
	}

}