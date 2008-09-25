package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class FindProjectByNameMethod extends AbstractMethod<Long> {

    private static final long serialVersionUID = 1L;

    public FindProjectByNameMethod(Caseless projectName, Caseless userId) {
        super("find.project.by.name", userId);
        setParameter(PROP_PROJECT_NAME, projectName);
    }

}
