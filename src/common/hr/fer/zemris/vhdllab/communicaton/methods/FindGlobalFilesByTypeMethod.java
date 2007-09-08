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
public final class FindGlobalFilesByTypeMethod extends AbstractMethod<ArrayList<Long>> {

	private static final long serialVersionUID = 1L;

	public FindGlobalFilesByTypeMethod(String type) {
		super("find.global.files.by.type");
		setParameter(PROP_FILE_TYPE, type);
	}

}
