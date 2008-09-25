package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class LoadUserFileContentMethod extends
        AbstractIdParameterMethod<String> {

    private static final long serialVersionUID = 1L;

    public LoadUserFileContentMethod(Integer id, Caseless userId) {
        super("load.user.file.content", userId, id);
    }

}
