/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class LoadGlobalFileNameMethod extends AbstractIdParameterMethod<String> {

	public LoadGlobalFileNameMethod(Long id) {
		super("load.global.file.name", id);
	}

}
