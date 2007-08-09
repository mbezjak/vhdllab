package hr.fer.zemris.vhdllab.applets.main.resolver;

import hr.fer.zemris.vhdllab.applets.main.ComponentGroup;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IIDResolver;

import javax.swing.JComponent;

public class BasicEditorResolver implements IIDResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IIDResolver#resolveFromComponent(java.lang.String,
	 *      javax.swing.JComponent)
	 */
	@Override
	public String resolveFromComponent(String originalId, JComponent component) {
		return resolveFromData(originalId, (Object[]) null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IIDResolver#resolveFromData(java.lang.String,
	 *      java.lang.Object[])
	 */
	@Override
	public String resolveFromData(String originalId, Object... data) {
		StringBuilder sb = new StringBuilder(originalId.length() + 20);
		sb.append(ComponentGroup.EDITOR.name()).append("$").append(originalId);
		return sb.toString();
	}

}
