package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.entities.FileInfo;

import java.util.Properties;

/**
 * Simulators simulate VHDL testbench file. All simulators must have an empty
 * public default constructor for proper initialization! After simulator is
 * instantiated {@link #configure(Properties)} method will be called to
 * configure a simulator.
 * <p>
 * Usually there is only one simulator (only for VHDL testbench file type file).
 * To register a simulator to a file type edit server.xml configuration file.
 * </p>
 * <p>
 * A simulator implementation must be stateless! Meaning that repeated
 * invocation of {@link #execute(File)} method on the same object instance must
 * return the same result as if it was invoked on different object instance!
 * </p>
 * <p>
 * A simulator implementation must also be deterministic! Meaning that repeated
 * invocation of the same file parameter must return the same result!
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public interface Simulator {

    /**
     * Returns a result of a simulation. Return value can never be
     * <code>null</code>.
     * <p>
     * Implementor can expect <code>file</code> parameter to always be not
     * <code>null</code>.
     * </p>
     * 
     * @param file
     *            a file to simulate
     * @return a simulation result
     */
    SimulationResult simulate(FileInfo file) throws SimulationException;

}
