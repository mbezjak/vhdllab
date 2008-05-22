package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entities.File;

/**
 * Circuit interface extractors extract circuit interface out of a specified
 * file. All extractors must have an empty public default constructor for proper
 * initialization!
 * <p>
 * Each extractors extracts circuit interface for a file with specific file type
 * (i.e. for a file that an extractor can understand). To register an extractor
 * to a file type edit server.xml configuration file.
 * </p>
 * <p>
 * An extractor implementation must be stateless! Meaning that repeated
 * invocation of {@link #execute(File)} method on the same object instance must
 * return the same result as if it was invoked on different object instance!
 * </p>
 * <p>
 * An extractor implementation must also be deterministic! Meaning that repeated
 * invocation of the same file parameter must return the same result!
 * </p>
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface CircuitInterfaceExtractor extends Functionality<CircuitInterface> {

    /**
     * Returns a circuit interface for specified file. Return value can never be
     * <code>null</code>.
     * <p>
     * Implementor can expect <code>file</code> parameter to always be not
     * <code>null</code> and that a <code>file</code> will be of appropriate
     * type (one that extractor can understand).
     * </p>
     *
     * @param file
     *            a file for circuit interface must be extracted
     * @return a circuit interface
     * @throws ServiceException
     *             if exceptional condition occurs
     */
    CircuitInterface execute(File file) throws ServiceException;

}
