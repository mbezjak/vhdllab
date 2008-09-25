package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

/**
 * @author Miro Bezjak
 * 
 */
public final class CreateUserFileMethod extends AbstractMethod<Long> {

    private static final long serialVersionUID = 1L;

    public CreateUserFileMethod(Caseless fileName, FileType fileType,
            Caseless userId) {
        super("create.user.file", userId);
        setParameter(PROP_FILE_NAME, fileName);
        setParameter(PROP_FILE_TYPE, fileType);
    }

}
