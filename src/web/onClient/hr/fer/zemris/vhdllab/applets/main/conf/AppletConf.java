package hr.fer.zemris.vhdllab.applets.main.conf;

import java.util.HashMap;
import java.util.Map;

public class AppletConf {

	private Map<String, EditorProperties> editors;
	private Map<String, ViewProperties> views;
	
	public AppletConf() {
		editors = new HashMap<String, EditorProperties>();
		views = new HashMap<String, ViewProperties>();
	}
	
	public void addEditor(EditorProperties editor) {
		editors.put(editor.getFileType(), editor);
	}
	
	public void addView(ViewProperties view) {
		views.put(view.getViewType(), view);
	}
	
	public EditorProperties getEditorProperties(String type) {
		return editors.get(type);
	}
	
	public ViewProperties getViewProperties(String type) {
		return views.get(type);
	}
	
}
