/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import java.util.ArrayList;

import hr.fer.zemris.vhdllab.communicaton.AbstractIdParameterMethod;

/**
 * @author Miro Bezjak
 *
 */
public class FindFilesByProjectMethod extends AbstractIdParameterMethod<ArrayList<Long>> {

	public FindFilesByProjectMethod(Long id) {
		super("find.files.by.project", id);
	}

}
