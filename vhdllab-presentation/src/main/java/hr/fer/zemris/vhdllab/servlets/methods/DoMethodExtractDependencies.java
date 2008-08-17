package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.FileManager;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "extract dependencies" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodExtractDependencies extends AbstractRegisteredMethod {

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
		ArrayList<Long> dependencies;
		try {
		    FileManager man = container.getFileManager();
			File file = man.load(fileId);
			Long projectId = file.getProject().getId();
			checkFileSecurity(request, method, file);
			Set<String> names = container.getServiceManager().extractDependencies(file, true);
			dependencies = new ArrayList<Long>(names.size());
			for (String name : names) {
                File f = man.findByName(projectId, name);
                dependencies.add(f.getId());
            }
		} catch (ServiceException e) {
			method.setStatus(SE_CAN_NOT_EXTRACT_DEPENDENCIES,
					"fileId=" + fileId);
			return;
		}
		method.setResult(dependencies);
	}
	
}