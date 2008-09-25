package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class ExistsUserFile2Method extends AbstractMethod<Boolean> {

    private static final long serialVersionUID = 1L;

    public ExistsUserFile2Method(Caseless fileName, Caseless userId) {
        super("exists.user.file2", userId);
        setParameter(PROP_FILE_NAME, fileName);
    }

}
