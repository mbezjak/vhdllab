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
public class FindGlobalFilesByTypeMethod extends AbstractMethod<ArrayList<Long>> {

	public FindGlobalFilesByTypeMethod(String type) {
		super("find.global.files.by.type");
		setParameter(PROP_FILE_TYPE, type);
	}

}
