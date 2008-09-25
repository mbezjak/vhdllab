package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractMethod;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class CreateProjectMethod extends AbstractMethod<Integer> {

    private static final long serialVersionUID = 1L;

    public CreateProjectMethod(Caseless projectName, Caseless userId) {
        super("create.project", userId);
        setParameter(PROP_PROJECT_NAME, projectName);
    }

}
