/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class ExistsUserFileMethod extends AbstractIdParameterMethod<Boolean> {

	public ExistsUserFileMethod(Long id) {
		super("exists.user.file", id);
	}

}
