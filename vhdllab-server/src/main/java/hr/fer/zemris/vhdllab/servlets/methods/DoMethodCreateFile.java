package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.FileType;
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
        Integer projectId = method.getParameter(Integer.class, PROP_ID);
        Caseless fileName = method.getParameter(Caseless.class, PROP_FILE_NAME);
        FileType fileType = method.getParameter(FileType.class, PROP_FILE_TYPE);
        String content = method.getParameter(String.class, PROP_FILE_CONTENT);
        if (projectId == null || fileName == null || fileType == null) {
            return;
        }
        File file;
        try {
            Project project = container.getProjectManager().load(projectId);
            checkProjectSecurity(request, method, project);
            file = new File(fileType, fileName, content);
            project.addFile(file);
            container.getFileManager().save(file);
        } catch (ServiceException e) {
            if(e.getStatusCode() == StatusCodes.SERVICE_ENTITY_AND_FILE_NAME_DONT_MATCH) {
                method.setStatus(e.getStatusCode(), e.getMessage());
            } else {
                method.setStatus(SE_CAN_NOT_CREATE_FILE, "projectId=" + projectId
                        + ", name=" + fileName + ", type=" + fileType);
            }
            return;
        }
        method.setResult(file.getId());
    }

}