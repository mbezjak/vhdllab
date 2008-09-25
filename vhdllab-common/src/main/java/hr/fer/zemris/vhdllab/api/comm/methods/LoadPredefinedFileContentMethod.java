package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class LoadPredefinedFileContentMethod extends
        AbstractMethod<String> {

    private static final long serialVersionUID = 1L;

    public LoadPredefinedFileContentMethod(Caseless fileName, Caseless userId) {
        super("load.predefined.file.content", userId);
        setParameter(PROP_FILE_NAME, fileName);
    }

}
