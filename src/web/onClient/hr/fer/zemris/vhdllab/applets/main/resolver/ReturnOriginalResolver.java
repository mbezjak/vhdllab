package hr.fer.zemris.vhdllab.applets.main.resolver;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IIDResolver;

import javax.swing.JComponent;

public class ReturnOriginalResolver implements IIDResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IIDResolver#resolve(java.lang.String,
	 *      javax.swing.JComponent)
	 */
	@Override
	public String resolve(String originalId, JComponent component) {
		return originalId;
	}

}
