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
public final class DeleteFileMethod extends AbstractIdParameterMethod<Void> {

	private static final long serialVersionUID = 1L;

	public DeleteFileMethod(Long id) {
		super("delete.file", id);
	}

}
