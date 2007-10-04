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
public final class FindProjectsByUserMethod extends AbstractMethod<ArrayList<Long>> {

	private static final long serialVersionUID = 1L;

	public FindProjectsByUserMethod() {
		super("find.projects.by.user");
	}

	public FindProjectsByUserMethod(String userId) {
		super("find.projects.by.user", userId);
	}
	
}
