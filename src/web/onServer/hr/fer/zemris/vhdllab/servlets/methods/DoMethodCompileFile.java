package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;

import java.io.Serializable;

/**
 * This class represents a registered method for "compile file" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_COMPILE_FILE
 */
public class DoMethodCompileFile extends AbstractRegisteredMethod {

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
		CompilationResult result;
		try {
			File file = labman.loadFile(fileId);
			checkFileSecurity(method, file);
			result = labman.compile(file);
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_GET_COMPILATION_RESULT, "fileId=" + fileId);
			return;
		}
		method.setResult(result);
	}

}