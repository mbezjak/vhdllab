package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;

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
     * Generates a <code>VHDL</code> code for a specified <code>file</code> (if
     * necessary) and returns a result.
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
     * based on <code>includeTransitive</code> flag. Return value will never be
     * <code>null</code>, although it can be an empty collection if a file has
     * no dependencies.
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

    /**
     * Returns a hierarchy for specified project. Return value will never be
     * <code>null</code>.
     * 
     * @param project
     *            a project for whom to extract hierarchy
     * @return a hierarchy for specified project
     * @throws NullPointerException
     *             if <code>project</code> is <code>null</code>
     * @throws ServiceException
     *             if any exception occurs
     */
    Hierarchy extractHierarchy(Project project) throws ServiceException;

    /**
     * Compiles specified file and returns a result of a compilation. Returned
     * value will never be <code>null</code>.
     * 
     * @param file
     *            a file to compile
     * @return a compilation result
     * @throws NullPointerException
     *             if <code>file</code> is <code>null</code>
     * @throws ServiceException
     *             if any exception occurs
     */
    CompilationResult compile(File file) throws ServiceException;

    /**
     * Simulates specified file and returns a result of a simulation. Returned
     * value will never be <code>null</code>.
     * 
     * @param file
     *            a file to simulate
     * @return a simulation result
     * @throws NullPointerException
     *             if <code>file</code> is <code>null</code>
     * @throws ServiceException
     *             if any exception occurs
     */
    SimulationResult simulate(File file) throws ServiceException;

}
