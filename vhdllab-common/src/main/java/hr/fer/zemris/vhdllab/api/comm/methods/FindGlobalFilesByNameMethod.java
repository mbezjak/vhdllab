package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

import java.util.ArrayList;

/**
 * @author Miro Bezjak
 * 
 */
public final class FindGlobalFilesByNameMethod extends
        AbstractMethod<ArrayList<Long>> {

    private static final long serialVersionUID = 1L;

    public FindGlobalFilesByNameMethod(Caseless fileName, Caseless userId) {
        super("find.global.files.by.name", userId);
        setParameter(PROP_FILE_NAME, fileName);
    }

}
