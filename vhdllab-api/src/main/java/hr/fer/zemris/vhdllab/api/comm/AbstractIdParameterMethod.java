package hr.fer.zemris.vhdllab.api.comm;

import java.io.Serializable;

/**
 * @author Miro Bezjak
 * @param <T> 
 */
public abstract class AbstractIdParameterMethod<T extends Serializable> extends
		AbstractMethod<T> {

    private static final long serialVersionUID = 1L;

    public AbstractIdParameterMethod(String method, String userId, Long id) {
		super(method, userId);
		setParameter(PROP_ID, id);
	}

}
