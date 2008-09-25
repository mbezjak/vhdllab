package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class LogoutMethod extends AbstractMethod<Integer> {

    private static final long serialVersionUID = 1L;

    public LogoutMethod(Caseless userId) {
        super("logout", userId);
    }

}
