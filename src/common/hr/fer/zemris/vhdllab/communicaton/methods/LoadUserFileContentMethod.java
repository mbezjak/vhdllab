/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class LoadUserFileContentMethod extends AbstractIdParameterMethod<String> {

	private static final long serialVersionUID = 1L;

	public LoadUserFileContentMethod(Long id) {
		super("load.user.file.content", id);
	}

}
