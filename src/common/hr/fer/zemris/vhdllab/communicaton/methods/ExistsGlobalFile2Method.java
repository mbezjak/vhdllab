/**
 * 
 */
package hr.fer.zemris.vhdllab.communicaton.methods;

import hr.fer.zemris.vhdllab.communicaton.AbstractMethod;

/**
 * @author Miro Bezjak
 *
 */
public class ExistsGlobalFile2Method extends AbstractMethod<Boolean> {

	public ExistsGlobalFile2Method(String fileName) {
		super("exists.global.file2");
		setParameter(PROP_FILE_NAME, fileName);
	}

}
