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
public final class GetAllGlobalFilesMethod extends AbstractMethod<ArrayList<Long>> {

	private static final long serialVersionUID = 1L;

	public GetAllGlobalFilesMethod() {
		super("get.all.global.files");
	}

}
