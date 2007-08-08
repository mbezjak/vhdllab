package hr.fer.zemris.vhdllab.applets.main.resolver;

import hr.fer.zemris.vhdllab.applets.main.ComponentGroup;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IIDResolver;

import javax.swing.JComponent;

public class BasicEditorResolver implements IIDResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IIDResolver#resolve(java.lang.String,
	 *      javax.swing.JComponent)
	 */
	@Override
	public String resolve(String originalId, JComponent component) {
		StringBuilder sb = new StringBuilder(originalId.length() + 20);
		sb.append(ComponentGroup.EDITOR.name()).append("$").append(originalId);
		return sb.toString();
	}

}
