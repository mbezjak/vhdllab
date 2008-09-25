package hr.fer.zemris.vhdllab.api.comm;

import hr.fer.zemris.vhdllab.entities.Caseless;

import java.io.Serializable;

/**
 * @author Miro Bezjak
 * @param <T> 
 */
public abstract class AbstractIdParameterMethod<T extends Serializable> extends
		AbstractMethod<T> {

    private static final long serialVersionUID = 1L;

    public AbstractIdParameterMethod(String method, Caseless userId, Integer id) {
		super(method, userId);
		setParameter(PROP_ID, id);
	}

}
