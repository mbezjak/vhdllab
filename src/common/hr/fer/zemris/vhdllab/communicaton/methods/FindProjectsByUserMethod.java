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
public class FindProjectsByUserMethod extends AbstractMethod<ArrayList<Long>> {

	public FindProjectsByUserMethod(String userId) {
		super("find.projects.by.user");
		setParameter(PROP_USER_ID, userId);
	}

}
