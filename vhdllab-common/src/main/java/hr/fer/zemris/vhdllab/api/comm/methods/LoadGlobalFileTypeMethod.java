package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class LoadGlobalFileTypeMethod extends
        AbstractIdParameterMethod<String> {

    private static final long serialVersionUID = 1L;

    public LoadGlobalFileTypeMethod(Integer id, Caseless userId) {
        super("load.global.file.type", userId, id);
    }

}
