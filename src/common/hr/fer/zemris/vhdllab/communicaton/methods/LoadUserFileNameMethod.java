/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class LoadUserFileNameMethod extends AbstractIdParameterMethod<String> {

	public LoadUserFileNameMethod(Long id) {
		super("load.user.file.name", id);
	}

}
