/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class LoadGlobalFileTypeMethod extends AbstractIdParameterMethod<String> {

	public LoadGlobalFileTypeMethod(Long id) {
		super("load.global.file.type", id);
	}

}
