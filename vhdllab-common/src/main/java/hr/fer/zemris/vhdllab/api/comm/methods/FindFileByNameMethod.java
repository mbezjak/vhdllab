package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class FindFileByNameMethod extends AbstractIdParameterMethod<Integer> {

    private static final long serialVersionUID = 1L;

    public FindFileByNameMethod(Integer id, Caseless fileName, Caseless userId) {
        super("find.file.by.name", userId, id);
        setParameter(PROP_FILE_NAME, fileName);
    }

}
