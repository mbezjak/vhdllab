/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class ExistsUserFile2Method extends AbstractMethod<Boolean> {

	private static final long serialVersionUID = 1L;

	public ExistsUserFile2Method(String fileName) {
		super("exists.user.file2");
		setParameter(PROP_FILE_NAME, fileName);
	}

}