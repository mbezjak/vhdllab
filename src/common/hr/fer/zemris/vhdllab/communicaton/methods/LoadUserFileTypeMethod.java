/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class LoadUserFileTypeMethod extends AbstractIdParameterMethod<String> {

	public LoadUserFileTypeMethod(Long id) {
		super("load.user.file.type", id);
	}

}
