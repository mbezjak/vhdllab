/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class LoadGlobalFileContentMethod extends AbstractIdParameterMethod<String> {

	public LoadGlobalFileContentMethod(Long id) {
		super("load.global.file.content", id);
	}

}
