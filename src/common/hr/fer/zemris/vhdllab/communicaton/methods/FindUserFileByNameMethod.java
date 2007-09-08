/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class FindUserFileByNameMethod extends AbstractIdParameterMethod<Long> {

	private static final long serialVersionUID = 1L;

	public FindUserFileByNameMethod(Long id, String fileName) {
		super("find.user.file.by.name", id);
		setParameter(PROP_FILE_NAME, fileName);
	}

}
