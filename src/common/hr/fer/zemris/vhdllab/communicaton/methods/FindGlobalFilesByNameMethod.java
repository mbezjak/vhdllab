/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

import java.util.ArrayList;

/**
 * @author Miro Bezjak
 *
 */
public final class FindGlobalFilesByNameMethod extends AbstractMethod<ArrayList<Long>> {

	private static final long serialVersionUID = 1L;

	public FindGlobalFilesByNameMethod(String fileName) {
		super("find.global.files.by.name");
		setParameter(PROP_FILE_NAME, fileName);
	}

}
