package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

import java.util.ArrayList;

/**
 * @author Miro Bezjak
 * 
 */
public final class FindProjectsByUserMethod extends
        AbstractMethod<ArrayList<Integer>> {

    private static final long serialVersionUID = 1L;

    public FindProjectsByUserMethod(Caseless userId) {
        super("find.projects.by.user", userId);
    }

}
