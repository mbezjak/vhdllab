package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.File;

/**
 * VHDL generators generate VHDL code out of a specified file. All generators
 * must have an empty public default constructor for proper initialization!
 * <p>
 * Each generator generates VHDL code for a file with specific file type (i.e.
 * for a file that a generator can understand). To register a generator to a
 * file type edit server.xml configuration file.
 * </p>
 * <p>
 * A generator implementation must be stateless! Meaning that repeated
 * invocation of {@link #generateVHDL(File)} method on the same object instance
 * must return the same result as if it was invoked on different object
 * instance!
 * </p>
 * <p>
 * A generator implementation must also be deterministic! Meaning that repeated
 * invocation of the same file parameter must return the same result!
 * </p>
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface VHDLGenerator {

    /**
     * Returns a VHDL generation result for specified file. Return value can
     * never be <code>null</code>.
     * <p>
     * Implementor can expect <code>file</code> parameter to always be not
     * <code>null</code> and that a <code>file</code> will be of appropriate
     * type (one that generator can understand).
     * </p>
     *
     * @param file
     *            a file for which VHDL must be generated
     * @return a VHDL generation result
     * @throws ServiceException
     *             if exceptional condition occurs
     */
    VHDLGenerationResult generateVHDL(File file) throws ServiceException;
}
