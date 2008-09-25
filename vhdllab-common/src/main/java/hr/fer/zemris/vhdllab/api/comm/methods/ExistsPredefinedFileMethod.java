package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class ExistsPredefinedFileMethod extends AbstractMethod<String> {

    private static final long serialVersionUID = 1L;

    public ExistsPredefinedFileMethod(Caseless fileName, Caseless userId) {
        super("exists.predefined.file", userId);
        setParameter(PROP_FILE_NAME, fileName);
    }

}
