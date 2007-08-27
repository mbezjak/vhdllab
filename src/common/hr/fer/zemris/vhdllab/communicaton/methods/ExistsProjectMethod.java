/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class ExistsProjectMethod extends AbstractIdParameterMethod<Boolean> {

	public ExistsProjectMethod(Long id) {
		super("exists.project", id);
	}

}
