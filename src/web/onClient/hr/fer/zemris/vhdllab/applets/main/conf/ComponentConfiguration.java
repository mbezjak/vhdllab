package hr.fer.zemris.vhdllab.applets.main.conf;

import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier;

import java.util.HashMap;
import java.util.Map;

public class ComponentConfiguration {

	private Map<String, EditorProperties> editors;
	private Map<String, EditorProperties> editorsByFileType;
	private Map<String, ViewProperties> views;
	
	public ComponentConfiguration() {
		editors = new HashMap<String, EditorProperties>();
		editorsByFileType = new HashMap<String, EditorProperties>();
		views = new HashMap<String, ViewProperties>();
	}
	
	public void addEditor(EditorProperties editor) {
		editors.put(editor.getId(), editor);
		String fileType = editor.getFileType();
		if(fileType != null) {
			editorsByFileType.put(fileType, editor);
		}
	}
	
	public void addView(ViewProperties view) {
		views.put(view.getId(), view);
	}
	
	public EditorProperties getEditorProperties(IComponentIdentifier<?> id) {
		return getEditorProperties(id.getComponentType());
	}
	
	public EditorProperties getEditorProperties(String id) {
		return editors.get(id);
	}
	
	public EditorProperties getEditorPropertiesByFileType(String type) {
		return editorsByFileType.get(type);
	}
	
	public ViewProperties getViewProperties(String id) {
		return views.get(id);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(2000);
		for(EditorProperties ep : editors.values()) {
			sb.append(ep).append("\n");
		}
		sb.append("\n");
		for(ViewProperties vp : views.values()) {
			sb.append(vp).append("\n");
		}
		return sb.toString();
	}
	
}
