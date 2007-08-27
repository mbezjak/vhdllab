/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class LoadUserFileContentMethod extends AbstractIdParameterMethod<String> {

	public LoadUserFileContentMethod(Long id) {
		super("load.user.file.content", id);
	}

}
