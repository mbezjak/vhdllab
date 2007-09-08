/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class LoadProjectIdForFileMethod extends AbstractIdParameterMethod<Long> {

	private static final long serialVersionUID = 1L;

	public LoadProjectIdForFileMethod(Long id) {
		super("load.project.id.for.file", id);
	}

}
