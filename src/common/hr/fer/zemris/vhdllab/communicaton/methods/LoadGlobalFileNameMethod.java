/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class LoadGlobalFileNameMethod extends AbstractIdParameterMethod<String> {

	private static final long serialVersionUID = 1L;

	public LoadGlobalFileNameMethod(Long id) {
		super("load.global.file.name", id);
	}

}
