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
public class FindGlobalFilesByNameMethod extends AbstractMethod<ArrayList<Long>> {

	public FindGlobalFilesByNameMethod(String fileName) {
		super("find.global.files.by.name");
		setParameter(PROP_FILE_NAME, fileName);
	}

}
