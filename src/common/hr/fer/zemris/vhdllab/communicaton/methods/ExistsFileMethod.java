/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class ExistsFileMethod extends AbstractIdParameterMethod<Boolean> {

	public ExistsFileMethod(Long id) {
		super("exists.file", id);
	}

}
