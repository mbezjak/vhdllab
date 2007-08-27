/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;
import hr.fer.zemris.vhdllab.communicaton.results.Void;

/**
 * @author Miro Bezjak
 *
 */
public class DeleteGlobalFileMethod extends AbstractIdParameterMethod<Void> {

	public DeleteGlobalFileMethod(Long id) {
		super("delete.global.file", id);
	}

}
