/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class LoadFileNameMethod extends AbstractIdParameterMethod<String> {

	public LoadFileNameMethod(Long id) {
		super("load.file.name", id);
	}

}
