/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class ExistsFile2Method extends AbstractIdParameterMethod<Boolean> {

	private static final long serialVersionUID = 1L;

	public ExistsFile2Method(Long id, String fileName) {
		super("exists.file2", id);
		setParameter(PROP_FILE_NAME, fileName);
	}

}
