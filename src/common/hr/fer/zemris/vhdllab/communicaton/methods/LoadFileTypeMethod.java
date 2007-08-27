/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class LoadFileTypeMethod extends AbstractIdParameterMethod<String> {

	public LoadFileTypeMethod(Long id) {
		super("load.file.type", id);
	}

}
