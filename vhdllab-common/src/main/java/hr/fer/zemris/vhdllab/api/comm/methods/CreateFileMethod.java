package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

/**
 * @author Miro Bezjak
 * 
 */
public final class CreateFileMethod extends AbstractIdParameterMethod<Integer> {

    private static final long serialVersionUID = 1L;

    public CreateFileMethod(Integer id, Caseless fileName, FileType fileType,
            String content, Caseless userId) {
        super("create.file", userId, id);
        setParameter(PROP_FILE_NAME, fileName);
        setParameter(PROP_FILE_TYPE, fileType);
        setParameter(PROP_FILE_CONTENT, content);
    }

}
