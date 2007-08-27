/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton;

import java.io.Serializable;

/**
 * @author Miro Bezjak
 * 
 */
public abstract class AbstractIdParameterMethod<T extends Serializable> extends
		AbstractMethod<T> {

	public AbstractIdParameterMethod(String method, Long id) {
		super(method);
		setParameter(PROP_ID, id);
	}

}
