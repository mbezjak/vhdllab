package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

import java.util.ArrayList;

/**
 * @author Miro Bezjak
 * 
 */
public final class FindFilesByProjectMethod extends
        AbstractIdParameterMethod<ArrayList<Integer>> {

    private static final long serialVersionUID = 1L;

    public FindFilesByProjectMethod(Integer id, Caseless userId) {
        super("find.files.by.project", userId, id);
    }

}
