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
     * Returns boolean indicating if type is a VHDL circuit.
     * 
     * @return boolean indicating if type is a VHDL circuit
     */
    public boolean isCircuit() {
        switch (this) {
        case SOURCE:
        case SCHEMA:
        case AUTOMATON:
        case PREDEFINED:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns boolean indicating if type can be compiled.
     * 
     * @return boolean indicating if type can be compiled
     */
    public boolean isCompilable() {
        return isCircuit() && !this.equals(PREDEFINED);
    }

    /**
     * Returns boolean indicating if type can be simulated.
     * 
     * @return boolean indicating if type can be simulated
     */
    public boolean isSimulatable() {
        switch (this) {
        case TESTBENCH:
            return true;
        default:
            return false;
        }
    }

}
