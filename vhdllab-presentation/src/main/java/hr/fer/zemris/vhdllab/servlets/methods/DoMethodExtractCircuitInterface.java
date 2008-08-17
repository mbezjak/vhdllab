package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "extract circuit interface"
 * request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodExtractCircuitInterface extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
		Long fileId = method.getParameter(Long.class, PROP_ID);
		if (fileId == null) {
			return;
		}
		CircuitInterface ci;
		try {
			File file = container.getFileManager().load(fileId);
			checkFileSecurity(request, method, file);
			ci = container.getServiceManager().extractCircuitInterface(file);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_EXTRACT_CIRCUIT_INTERFACE,
					"fileId=" + fileId);
			return;
		}
		method.setResult(ci);
	}

}