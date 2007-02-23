package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.model.ModelUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTabbedPane;

public class EditorPane extends JTabbedPane {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 8072275070218439960L;
	
	/**
     * Creates an empty <code>EditorPane</code> with a default
     * tab placement of <code>EditorPane.TOP</code>.
     */
	public EditorPane() {
		super();
	}

	/**
     * Creates an empty <code>EditorPane</code> with the specified tab placement
     * of either: <code>EditorPane.TOP</code>, <code>EditorPane.BOTTOM</code>,
     * <code>EditorPane.LEFT</code>, or <code>EditorPane.RIGHT</code>.
     *
     * @param tabPlacement the placement for the tabs relative to the content
     */
	public EditorPane(int tabPlacement) {
		super(tabPlacement);
	}

	/**
     * Creates an empty <code>EditorPane</code> with the specified tab placement
     * and tab layout policy.  Tab placement may be either: 
     * <code>EditorPane.TOP</code>, <code>EditorPane.BOTTOM</code>,
     * <code>EditorPane.LEFT</code>, or <code>EditorPane.RIGHT</code>.
     * Tab layout policy may be either: <code>EditorPane.WRAP_TAB_LAYOUT</code>
     * or <code>EditorPane.SCROLL_TAB_LAYOUT</code>.
     *
     * @param tabPlacement the placement for the tabs relative to the content
     * @param tabLayoutPolicy the policy for laying out tabs when all tabs will not fit on one run
     * @throws IllegalArgumentException if tab placement or tab layout policy are not
     *            one of the above supported values
     */
	public EditorPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
	}
	
	/**
     * Returns the currently selected editor for this editor pane.
     * Returns <code>null</code> if there is no currently selected tab.
     *
     * @return the editor corresponding to the selected tab
     * @see #setSelectedComponent
     */
	public IEditor getSelectedEditor() {
		return (IEditor) getSelectedComponent();
	}
	
    /**
     * Sets the selected editor for this editor pane. This will automatically
     * set the <code>selectedIndex</code> to the index corresponding to the
     * specified editor.
     *
     * @throws IllegalArgumentException if editor not found in editor pane
     * @see #getSelectedComponent
     */
    public void setSelectedEditor(IEditor e) {
        if(isEditorOpened(e)) {
            int index = indexOfEditor(e);
            setSelectedIndex(index);
        } else {
        	throw new IllegalArgumentException("editor not found in editor pane");
        }
    }
    
    /**
     * Returns the editor at <code>index</code>.
     *
     * @param index the index of the item being queried
     * @return the <code>IEditor</code> at <code>index</code>
     * @throws IndexOutOfBoundsException if index is out of range
     *            (index < 0 || index >= tab count)
     */
	public IEditor getEditorAt(int index) {
		return (IEditor) getComponentAt(index);
	}
	
	/**
	 * Returns <code>true</code> is editor pane contains no tabs.
	 * @return <code>true</code> is editor pane contains no tabs; <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return getTabCount() == 0;
	}

	public int indexOfEditor(IEditor editor) {
		if(editor == null) return -1;
		String projectName = editor.getProjectName();
		String fileName = editor.getFileName();
		return indexOfEditor(projectName, fileName);
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

	public boolean isEditorOpened(IEditor editor) {
		if(editor == null) return false;
		return indexOfEditor(editor) != -1;
	}

	public boolean isEditorOpened(String projectName, String fileName) {
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
	
	/**
	 * This method should be depecated.
	 * 
	 * Solution:
	 * IEditor must have method "requestFocusInWindow()" and that one should be invoked!
	 */
	@Deprecated
	public boolean requestFocusToSelectedEditor() {
		return getSelectedComponent().requestFocusInWindow();
	}
	
}