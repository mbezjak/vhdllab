package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.entities.File;

/**
 * Describes an extra functionality associated with certain file type. For
 * instance, all files having one of VHDL file type (see {@link FileTypes}
 * class) are capable of multiple extra functionalities: generating VHDL code,
 * extracting circuit interface, extracting dependencies, compiling and
 * simulating VHDL code.
 * <p>
 * All implementors of this interface must have an empty public default
 * constructor for proper initialization!
 * </p>
 *
 * @param <T>
 *            a type of an execution result
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 * @see FileTypes
 */
public interface Functionality<T> {

    /**
     * Preforms a functionality for specified file and returns a result. Return
     * value can never be <code>null</code>.
     *
     * @param file
     *            a file for which to preform functionality
     * @return a result of execution
     * @throws ServiceException
     *             if exceptional condition occurs
     */
    T execute(File file) throws ServiceException;

}
