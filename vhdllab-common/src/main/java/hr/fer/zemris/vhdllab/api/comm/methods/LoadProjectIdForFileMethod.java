package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class LoadProjectIdForFileMethod extends
        AbstractIdParameterMethod<Long> {

    private static final long serialVersionUID = 1L;

    public LoadProjectIdForFileMethod(Integer id, Caseless userId) {
        super("load.project.id.for.file", userId, id);
    }

}
