package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entities.File;

import java.util.Set;

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
     * @throws NullPointerException
     *             if <code>file</code> is <code>null</code>
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
     * @throws NullPointerException
     *             if <code>file</code> is <code>null</code>
     * @throws ServiceException
     *             if any exception occurs
     */
    CircuitInterface extractCircuitInterface(File file) throws ServiceException;

    /**
     * Returns an unmodifiable collection of file names representing files on
     * which specified file depends on. Transitive dependencies are included
     * based on <code>includeTransitive</code> flag. Return value will never
     * be <code>null</code>, although it can be an empty collection if a file
     * has no dependencies.
     *
     * @param file
     *            a file for which dependencies must be extracted
     * @param includeTransitive
     *            a flag indicating if transitive dependencies are to be
     *            included
     * @return an unmodifiable collection of file names representing files on
     *         which specified file depends on
     * @throws NullPointerException
     *             if <code>file</code> is <code>null</code>
     * @throws ServiceException
     *             if any exception occurs
     */
    Set<String> extractDependencies(File file, boolean includeTransitive)
            throws ServiceException;

}
