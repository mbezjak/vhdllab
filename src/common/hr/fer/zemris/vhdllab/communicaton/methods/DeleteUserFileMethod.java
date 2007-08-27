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
public class DeleteUserFileMethod extends AbstractIdParameterMethod<Void> {

	public DeleteUserFileMethod(Long id) {
		super("delete.user.file", id);
	}

}
