package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.api.comm.results.Void;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class SaveFileMethod extends AbstractIdParameterMethod<Void> {

    private static final long serialVersionUID = 1L;

    public SaveFileMethod(Integer id, String content, Caseless userId) {
        super("save.file", userId, id);
        setParameter(PROP_FILE_CONTENT, content);
    }

}
