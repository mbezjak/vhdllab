package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.entities.File;

import java.util.Properties;

/**
 * Compilers compile VHDL code out of a specified file. All compilers must have
 * an empty public default constructor for proper initialization! After compiler
 * is instantiated {@link #configure(Properties)} method will be called to
 * configure a compiler.
 * <p>
 * Usually there is only one compiler for every VHDL file type so before
 * compilation VHDL code should be generated out of file content. To register a
 * compiler to a file type edit server.xml configuration file.
 * </p>
 * <p>
 * A compiler implementation must be stateless! Meaning that repeated invocation
 * of {@link #execute(File)} method on the same object instance must return the
 * same result as if it was invoked on different object instance!
 * </p>
 * <p>
 * A compiler implementation must also be deterministic! Meaning that repeated
 * invocation of the same file parameter must return the same result!
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface Compiler extends Functionality<CompilationResult> {

    /**
     * Returns a result of a compilation. Return value can never be
     * <code>null</code>.
     * <p>
     * Implementor can expect <code>file</code> parameter to always be not
     * <code>null</code>.
     * </p>
     * 
     * @param file
     *            a file to compile
     * @return a compilation result
     * @throws ServiceException
     *             if exceptional condition occurs
     */
    CompilationResult execute(File file) throws ServiceException;

}
