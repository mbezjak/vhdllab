package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

/**
 * @author Miro Bezjak
 * 
 */
public final class LoadFileTypeMethod extends AbstractIdParameterMethod<FileType> {

    private static final long serialVersionUID = 1L;

    public LoadFileTypeMethod(Integer id, Caseless userId) {
        super("load.file.type", userId, id);
    }

}
