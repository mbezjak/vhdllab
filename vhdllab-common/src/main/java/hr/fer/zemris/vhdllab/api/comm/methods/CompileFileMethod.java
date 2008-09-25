package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class CompileFileMethod extends
        AbstractIdParameterMethod<CompilationResult> {

    private static final long serialVersionUID = 1L;

    public CompileFileMethod(Integer id, Caseless userId) {
        super("compile.file", userId, id);
    }

}
