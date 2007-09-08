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
public final class FindFilesByProjectMethod extends AbstractIdParameterMethod<ArrayList<Long>> {

	private static final long serialVersionUID = 1L;

	public FindFilesByProjectMethod(Long id) {
		super("find.files.by.project", id);
	}

}
