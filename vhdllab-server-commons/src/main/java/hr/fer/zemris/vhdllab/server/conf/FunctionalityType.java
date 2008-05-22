package hr.fer.zemris.vhdllab.server.conf;

import hr.fer.zemris.vhdllab.api.FileTypes;

/**
 * This enum describes a type of extra functionality associated with certain
 * file type. For instance, all files having one of VHDL file type (see
 * {@link FileTypes} class) are capable of multiple extra functionalities:
 * generating VHDL code, extracting circuit interface, extracting dependencies
 * compiling and simulating a VHDL code.
 * <p>
 * Usually a file type is associated with all (except for {@link #SIMULATOR})
 * or none of functionality types defined in this enum.
 * </p>
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 * @see FileTypes
 */
public enum FunctionalityType {

    /**
     * Defines a functionality for generating VHDL code out of file content.
     */
    GENERATOR,
    /**
     * Defines a functionality for extracting circuit interface out of file
     * content.
     */
    EXTRACTOR,
    /**
     * Defines a functionality for extracting dependencies code out of file
     * content.
     */
    DEPENDENCY,
    /**
     * Defines a functionality for compiling VHDL code.
     */
    COMPILER,
    /**
     * Defines a functionality for simulating VHDL code.
     */
    SIMULATOR;

}
