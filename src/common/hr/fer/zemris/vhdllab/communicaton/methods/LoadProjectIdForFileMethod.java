/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class LoadProjectIdForFileMethod extends AbstractIdParameterMethod<Long> {

	public LoadProjectIdForFileMethod(Long id) {
		super("load.project.id.for.file", id);
	}

}
