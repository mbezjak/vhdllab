package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "find global files by type" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodGetAllGlobalFiles extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
		Set<LibraryFile> files;
		try {
			Library lib = container.getLibraryManager().findByName("predefined");
			files = lib.getFiles();
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_FIND_FILE);
			return;
		}
		ArrayList<Long> ids = new ArrayList<Long>(files.size());
		for(LibraryFile f : files) {
			ids.add(f.getId());
		}
		method.setResult(ids);
	}
	
}