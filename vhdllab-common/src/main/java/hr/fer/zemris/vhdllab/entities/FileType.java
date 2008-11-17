package hr.fer.zemris.vhdllab.entities;

/**
 * Defines all types that a file can have.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public enum FileType {
    SOURCE, TESTBENCH, SCHEMA, AUTOMATON, SIMULATION, PREDEFINED;

    /**
     * Returns boolean indicating if specified type is a VHDL circuit.
     * 
     * @param type
     *            a type
     * @return boolean indicating if specified type is a VHDL circuit
     */
    public static boolean isCircuit(FileType type) {
        switch (type) {
        case SOURCE:
        case SCHEMA:
        case AUTOMATON:
        case PREDEFINED:
            return true;
        default:
            return false;
        }
    }

}
