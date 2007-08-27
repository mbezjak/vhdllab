/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class LoadProjectNameMethod extends AbstractIdParameterMethod<String> {

	public LoadProjectNameMethod(Long id) {
		super("load.project.name", id);
	}

}
