package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class SimulateFileMethod extends
        AbstractIdParameterMethod<SimulationResult> {

    private static final long serialVersionUID = 1L;

    public SimulateFileMethod(Integer id, Caseless userId) {
        super("simulate.file", userId, id);
    }

}
