/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class ExistsGlobalFileMethod extends AbstractIdParameterMethod<Boolean> {

	public ExistsGlobalFileMethod(Long id) {
		super("exists.global.file", id);
	}

}
