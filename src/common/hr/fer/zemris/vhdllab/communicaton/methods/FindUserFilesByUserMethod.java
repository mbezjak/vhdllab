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
public class FindUserFilesByUserMethod extends AbstractMethod<ArrayList<Long>> {

	public FindUserFilesByUserMethod(String userId) {
		super("find.user.files.by.user");
		setParameter(PROP_USER_ID, userId);
	}

}
