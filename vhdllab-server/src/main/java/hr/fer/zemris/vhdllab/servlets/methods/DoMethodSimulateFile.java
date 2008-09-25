package hr.fer.zemris.vhdllab.servlets.methods;

import hr.fer.zemris.vhdllab.api.comm.Method;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.servlets.AbstractRegisteredMethod;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * This class represents a registered method for "run simulation" request.
 * 
 * @author Miro Bezjak
 */
public class DoMethodSimulateFile extends AbstractRegisteredMethod {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.servlets.RegisteredMethod#run(hr.fer.zemris.vhdllab
     * .api.comm.Method, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void run(Method<Serializable> method, HttpServletRequest request) {
        Integer fileId = method.getParameter(Integer.class, PROP_ID);
        if (fileId == null) {
            return;
        }
        SimulationResult result;
        try {
            File file = container.getFileManager().load(fileId);
            checkFileSecurity(request, method, file);
            result = container.getServiceManager().simulate(file);
        } catch (ServiceException e) {
            method.setStatus(SE_CAN_NOT_GET_SIMULATION_RESULT, "fileId="
                    + fileId);
            return;
        }
        method.setResult(result);
    }

}