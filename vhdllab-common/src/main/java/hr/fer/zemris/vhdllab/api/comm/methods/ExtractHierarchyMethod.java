package hr.fer.zemris.vhdllab.api.comm.methods;

import hr.fer.zemris.vhdllab.api.comm.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.entities.Caseless;

/**
 * @author Miro Bezjak
 * 
 */
public final class ExtractHierarchyMethod extends
        AbstractIdParameterMethod<Hierarchy> {

    private static final long serialVersionUID = 1L;

    public ExtractHierarchyMethod(Integer id, Caseless userId) {
        super("extract.hierarchy", userId, id);
    }

}
