/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public final class FindFileByNameMethod extends AbstractIdParameterMethod<Long> {

	private static final long serialVersionUID = 1L;

	public FindFileByNameMethod(Long id, String fileName) {
		super("find.file.by.name", id);
		setParameter(PROP_FILE_NAME, fileName);
	}

}
