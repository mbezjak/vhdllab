package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entities.File;

/**
 * This is an interface representing a service manager. It defines all business
 * logic.
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface ServiceManager {

    /**
     * Generates a <code>VHDL</code> code for a specified <code>file</code>
     * (if necessary) and returns a result.
     *
     * @param file
     *            a file for which VHDL must be generated
     * @return <code>VHDL</code> generation result for specified file
     * @throws ServiceException
     *             if any exception occurs
     */
    VHDLGenerationResult generateVHDL(File file) throws ServiceException;

    /**
     * Extracts circuit interface out of a file content.
     *
     * @param file
     *            a file for which circuit interface must be extracted
     * @return circuit interface for specified file
     * @throws ServiceException
     *             if any exception occurs
     */
    public CircuitInterface extractCircuitInterface(File file)
            throws ServiceException;

}
