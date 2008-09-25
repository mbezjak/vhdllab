package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class CreateGlobalFileMethod extends AbstractMethod<Long> {

    private static final long serialVersionUID = 1L;

    public CreateGlobalFileMethod(Caseless fileName, Caseless userId) {
        super("create.global.file", userId);
        setParameter(PROP_FILE_NAME, fileName);
    }

}
