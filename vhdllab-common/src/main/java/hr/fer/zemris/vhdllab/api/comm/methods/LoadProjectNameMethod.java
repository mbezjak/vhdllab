package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class LoadProjectNameMethod extends
        AbstractIdParameterMethod<Caseless> {

    private static final long serialVersionUID = 1L;

    public LoadProjectNameMethod(Integer id, Caseless userId) {
        super("load.project.name", userId, id);
    }

}
