package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class ExistsUserFileMethod extends
        AbstractIdParameterMethod<Boolean> {

    private static final long serialVersionUID = 1L;

    public ExistsUserFileMethod(Integer id, Caseless userId) {
        super("exists.user.file", userId, id);
    }

}
