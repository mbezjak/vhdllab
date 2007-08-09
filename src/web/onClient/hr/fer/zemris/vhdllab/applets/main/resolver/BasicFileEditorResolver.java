package hr.fer.zemris.vhdllab.applets.main.resolver;

import javax.swing.JComponent;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IIDResolver;

public class BasicFileEditorResolver implements IIDResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IIDResolver#resolveFromComponent(java.lang.String,
	 *      javax.swing.JComponent)
	 */
	@Override
	public String resolveFromComponent(String originalId, JComponent component) {
		IEditor e = (IEditor) component;
		return resolveFromData(originalId, new Object[] { e.getProjectName(),
				e.getFileName() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IIDResolver#resolveFromData(java.lang.String,
	 *      java.lang.Object[])
	 */
	@Override
	public String resolveFromData(String originalId, Object... data) {
		if (data.length != 2) {
			throw new IllegalArgumentException(
					"Data must contain a project and a file name");
		}
		String projectName = (String) data[0];
		String fileName = (String) data[1];
		StringBuilder sb = new StringBuilder(originalId.length() + 20);
		sb.append(originalId).append("#").append(projectName).append("/")
				.append(fileName);
		return sb.toString();
	}

}
