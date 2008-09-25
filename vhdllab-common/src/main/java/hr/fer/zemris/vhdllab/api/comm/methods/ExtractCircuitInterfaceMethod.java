package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class ExtractCircuitInterfaceMethod extends
        AbstractIdParameterMethod<CircuitInterface> {

    private static final long serialVersionUID = 1L;

    public ExtractCircuitInterfaceMethod(Integer id, Caseless userId) {
        super("extract.circuit.interface", userId, id);
    }

}
