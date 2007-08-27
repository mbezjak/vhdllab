/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class LoadFileContentMethod extends AbstractIdParameterMethod<String> {

	public LoadFileContentMethod(Long id) {
		super("load.file.content", id);
	}

}
