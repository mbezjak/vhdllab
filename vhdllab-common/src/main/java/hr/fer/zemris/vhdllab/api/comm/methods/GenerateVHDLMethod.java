package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class GenerateVHDLMethod extends
        AbstractIdParameterMethod<VHDLGenerationResult> {

    private static final long serialVersionUID = 1L;

    public GenerateVHDLMethod(Integer id, Caseless userId) {
        super("generate.vhdl", userId, id);
    }

}
