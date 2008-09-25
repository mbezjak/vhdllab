package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

import java.util.ArrayList;

/**
 * @author Miro Bezjak
 * 
 */
public final class GetAllGlobalFilesMethod extends
        AbstractMethod<ArrayList<Long>> {

    private static final long serialVersionUID = 1L;

    public GetAllGlobalFilesMethod(Caseless userId) {
        super("get.all.global.files", userId);
    }

}
