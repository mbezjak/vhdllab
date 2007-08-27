package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.ajax.shared.MethodConstants;
import hr.fer.zemris.vhdllab.communicaton.IMethod;
import hr.fer.zemris.vhdllab.model.GlobalFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;
import hr.fer.zemris.vhdllab.servlets.ManagerProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a registered method for "find global files by type" request.
 * 
 * @author Miro Bezjak
 * @see MethodConstants#MTD_FIND_GLOBAL_FILES_BY_TYPE
 */
public class DoMethodGetAllGlobalFiles extends AbstractRegisteredMethod {

	/* (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab.communicaton.IMethod,
	 *      hr.fer.zemris.vhdllab.servlets.ManagerProvider)
	 */
	@Override
	public void run(IMethod<Serializable> method, ManagerProvider provider) {
		VHDLLabManager labman = getVHDLLabManager(provider);
		List<GlobalFile> files;
		try {
			files = labman.getAllGlobalFiles();
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_FIND_FILE);
			return;
		}
		ArrayList<Long> ids = new ArrayList<Long>(files.size());
		for(GlobalFile f : files) {
			ids.add(f.getId());
		}
		method.setResult(ids);
	}
	
}