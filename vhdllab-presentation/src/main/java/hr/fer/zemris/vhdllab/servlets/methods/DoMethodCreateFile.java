package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "create file" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodCreateFile extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
        Long projectId = method.getParameter(Long.class, PROP_ID);
        String fileName = method.getParameter(String.class, PROP_FILE_NAME);
        String fileType = method.getParameter(String.class, PROP_FILE_TYPE);
        if (projectId == null || fileName == null || fileType == null) {
            return;
        }
        File file;
        try {
            Project project = container.getProjectManager().load(projectId);
            checkProjectSecurity(request, method, project);
            file = new File(project, fileName, fileType);
            container.getFileManager().save(file);
        } catch (ServiceException e) {
            method.setStatus(SE_CAN_NOT_CREATE_FILE, "projectId=" + projectId
                    + ", name=" + fileName + ", type=" + fileType);
            return;
        }
        method.setResult(file.getId());
    }

}