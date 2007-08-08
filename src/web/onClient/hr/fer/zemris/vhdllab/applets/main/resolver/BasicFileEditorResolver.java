package hr.fer.zemris.vhdllab.applets.main.resolver;

import javax.swing.JComponent;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IIDResolver;

public class BasicFileEditorResolver implements IIDResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IIDResolver#resolve(java.lang.String,
	 *      javax.swing.JComponent)
	 */
	@Override
	public String resolve(String originalId, JComponent component) {
		IEditor editor = (IEditor) component;
		StringBuilder sb = new StringBuilder(originalId.length() + 20);
		sb.append(originalId).append("#").append(editor.getProjectName())
				.append("/").append(editor.getFileName());
		return sb.toString();
	}

}
