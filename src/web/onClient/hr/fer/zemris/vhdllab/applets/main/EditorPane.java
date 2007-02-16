package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.model.ModelUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTabbedPane;

public final class EditorPane extends JTabbedPane {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 8072275070218439960L;

	public EditorPane() {
		super();
	}

	public EditorPane(int tabPlacement) {
		super(tabPlacement);
	}

	public EditorPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
	}

	public IEditor getEditorAt(int index) {
		return (IEditor) getComponentAt(index);
	}

	public int indexOfEditor(String projectName, String fileName) {
		if(projectName == null || fileName == null) return -1;
		for(int i = 0; i < getTabCount(); i++) {
			IEditor editor = getEditorAt(i);
			if(ModelUtil.projectNamesAreEqual(projectName, editor.getProjectName()) &&
					ModelUtil.fileNamesAreEqual(fileName, editor.getFileName())) {

				return i;
			}
		}
		return -1;
	}

	public int indexOfEditor(IEditor editor) {
		if(editor == null) return -1;
		String projectName = editor.getProjectName();
		String fileName = editor.getFileName();
		return indexOfEditor(projectName, fileName);
	}

	public boolean editorIsOpened(IEditor editor) {
		if(editor == null) return false;
		return indexOfEditor(editor) != -1;
	}

	public boolean editorIsOpened(String projectName, String fileName) {
		if(projectName == null || fileName == null) return false;
		return indexOfEditor(projectName, fileName) != -1;
	}

	public List<IEditor> getAllOpenedEditors() {
		List<IEditor> openedEditors = new ArrayList<IEditor>(getTabCount());
		for(int i = 0; i < getTabCount(); i++) {
			IEditor editor = getEditorAt(i);
			openedEditors.add(editor);
		}
		return openedEditors;
	}

	public List<IEditor> getOpenedEditorsThatHave(String projectName) {
		if(projectName == null) return Collections.emptyList();
		List<IEditor> openedEditors = getAllOpenedEditors();
		List<IEditor> editorsHavingSpecifiedProject = new ArrayList<IEditor>();
		for(IEditor e : openedEditors) {
			String editorProjectName = e.getProjectName();
			if(ModelUtil.projectNamesAreEqual(editorProjectName, projectName)) {
				editorsHavingSpecifiedProject.add(e);
			}
		}
		return editorsHavingSpecifiedProject;
	}

	public IEditor getOpenedEditor(String projectName, String fileName) {
		if(projectName == null || fileName == null) return null;
		int index = indexOfEditor(projectName, fileName);
		if(index == -1) return null;
		return getEditorAt(index);
	}

	public void closeEditor(IEditor editor) {
		int index = indexOfEditor(editor);
		remove(index);
	}

}