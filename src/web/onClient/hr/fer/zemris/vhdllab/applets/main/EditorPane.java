package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.utilities.ModelUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

/**
 * This is a wrapper of tabbed pane and provides functionality of editor pane (a
 * pane where editors are displayed to a user).
 * 
 * @author Miro Bezjak
 */
public class EditorPane {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 8072275070218439960L;

	/**
	 * A component where editors (as components) are put.
	 */
	private final JTabbedPane tabbedPane;

	/**
	 * Creates an empty <code>EditorPane</code> wrapper around actual
	 * <code>tabbedPane</code>.
	 * 
	 * @param tabbedPane
	 *            a component where editors (as components) are put
	 * @throws NullPointerException
	 *             if <code>tabbedPane</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>tabbedPane</code> is not empty (contains opened
	 *             tabs)
	 */
	public EditorPane(JTabbedPane tabbedPane) {
		if (tabbedPane == null) {
			throw new NullPointerException("Tabbed pane cant be null.");
		}
		if (tabbedPane.getTabCount() != 0) {
			throw new IllegalArgumentException(
					"Tabbed pane is not empty. Contains "
							+ tabbedPane.getTabCount() + " tabs.");
		}
		this.tabbedPane = tabbedPane;
	}
	
	public boolean add(String identifier, String title, IEditor editor) {
		tabbedPane.add(title, (JComponent) editor);
		return true;
	}

	/**
	 * Returns the number of tabs in this editor pane.
	 * 
	 * @return an integer specifying the number of editors opened
	 */
	public int getTabCount() {
		return tabbedPane.getTabCount();
	}

	/**
	 * Returns <code>true</code> is editor pane contains no tabs.
	 * 
	 * @return <code>true</code> is editor pane contains no opened editors;
	 *         <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return getTabCount() == 0;
	}

	/**
	 * Returns the currently selected editor for this editor pane. Returns
	 * <code>null</code> if no tab is selected.
	 * 
	 * @return the editor corresponding to the selected tab
	 * @see #setSelectedEditor(IEditor)
	 */
	public IEditor getSelectedEditor() {
		return (IEditor) tabbedPane.getSelectedComponent();
	}

	/**
	 * Sets the selected editor for this editor pane. This will automatically
	 * set the <code>selectedIndex</code> to the index corresponding to the
	 * specified editor.
	 * 
	 * @throws IllegalArgumentException
	 *             if editor not found in editor pane
	 * @see #getSelectedComponent
	 */
	public void setSelectedEditor(IEditor e) {
		if (isEditorOpened(e)) {
			int index = indexOfEditor(e);
			tabbedPane.setSelectedIndex(index);
		} else {
			throw new IllegalArgumentException(
					"editor not found in editor pane");
		}
	}

	/**
	 * Returns the currently selected index for this editor pane. Returns -1 if
	 * there is no currently selected tab.
	 * 
	 * @return the index of the selected tab
	 * @see #setSelectedIndex(int)
	 */
	public int getSelectedIndex() {
		return tabbedPane.getSelectedIndex();
	}

	/**
	 * Sets the selected index for this editor pane. The index must be a valid
	 * tab index or -1, which indicates that no tab should be selected (can also
	 * be used when there are no tabs in the editor pane). If a -1 value is
	 * specified when the editor pane contains one or more tabs, then the
	 * results will be implementation defined.
	 * 
	 * @param index
	 *            the index to be selected
	 * @throws IndexOutOfBoundsException
	 *             if index is out of range (index < -1 || index >= tab count)
	 * @see #getSelectedIndex()
	 */
	public void setSelectedIndex(int index) {
		tabbedPane.setSelectedIndex(index);
	}

	/**
	 * Sets the tooltip text at index to toolTipText which can be null. An
	 * internal exception is raised if there is no tab at that index.
	 * 
	 * @param index
	 *            the tab index where the tooltip text should be set
	 * @param toolTipText
	 *            the tooltip text to be displayed for the tab
	 * @throws IndexOutOfBoundsException
	 *             if index is out of range (index < 0 || index >= tab count)
	 */
	public void setToolTipTextAt(int index, String toolTipText) {
		tabbedPane.setToolTipTextAt(index, toolTipText);
	}

	/**
	 * Sets the title at index to title which can be null. The title is not
	 * shown if a tab component for this tab was specified. An internal
	 * exception is raised if there is no tab at that index.
	 * 
	 * @param index
	 *            the tab index where the title should be set
	 * @param title
	 *            the title to be displayed in the tab
	 * @throws IndexOutOfBoundsException
	 *             if index is out of range (index < 0 || index >= tab count)
	 */
	public void setTitleAt(int index, String title) {
		tabbedPane.setTitleAt(index, title);
	}

	/**
	 * Returns the editor at <code>index</code>.
	 * 
	 * @param index
	 *            the index of the item being queried
	 * @return the <code>IEditor</code> at <code>index</code>
	 * @throws IndexOutOfBoundsException
	 *             if index is out of range (index < 0 || index >= tab count)
	 */
	public IEditor getEditorAt(int index) {
		return (IEditor) tabbedPane.getComponentAt(index);
	}

	public int indexOfEditor(IEditor editor) {
		if (editor == null)
			return -1;
		String projectName = editor.getProjectName();
		String fileName = editor.getFileName();
		return indexOfEditor(projectName, fileName);
	}

	public int indexOfEditor(String projectName, String fileName) {
		if (projectName == null || fileName == null)
			return -1;
		for (int i = 0; i < getTabCount(); i++) {
			IEditor editor = getEditorAt(i);
			if (ModelUtil.projectNamesAreEqual(projectName, editor
					.getProjectName())
					&& ModelUtil.fileNamesAreEqual(fileName, editor
							.getFileName())) {

				return i;
			}
		}
		return -1;
	}

	public boolean isEditorOpened(IEditor editor) {
		if (editor == null)
			return false;
		return indexOfEditor(editor) != -1;
	}

	public boolean isEditorOpened(String projectName, String fileName) {
		if (projectName == null || fileName == null)
			return false;
		return indexOfEditor(projectName, fileName) != -1;
	}

	public List<IEditor> getAllOpenedEditors() {
		List<IEditor> openedEditors = new ArrayList<IEditor>(getTabCount());
		for (int i = 0; i < getTabCount(); i++) {
			IEditor editor = getEditorAt(i);
			openedEditors.add(editor);
		}
		return openedEditors;
	}

	public List<IEditor> getOpenedEditorsThatHave(String projectName) {
		if (projectName == null)
			return Collections.emptyList();
		List<IEditor> openedEditors = getAllOpenedEditors();
		List<IEditor> editorsHavingSpecifiedProject = new ArrayList<IEditor>();
		for (IEditor e : openedEditors) {
			String editorProjectName = e.getProjectName();
			if (ModelUtil.projectNamesAreEqual(editorProjectName, projectName)) {
				editorsHavingSpecifiedProject.add(e);
			}
		}
		return editorsHavingSpecifiedProject;
	}

	public IEditor getOpenedEditor(String projectName, String fileName) {
		if (projectName == null || fileName == null)
			return null;
		int index = indexOfEditor(projectName, fileName);
		if (index == -1)
			return null;
		return getEditorAt(index);
	}

	public void closeEditor(IEditor editor) {
		int index = indexOfEditor(editor);
		tabbedPane.remove(index);
	}

	/**
	 * This method should be depecated.
	 * 
	 * Solution: IEditor must have method "requestFocusInWindow()" and that one
	 * should be invoked!
	 */
	@Deprecated
	public boolean requestFocusToSelectedEditor() {
		return tabbedPane.getSelectedComponent().requestFocusInWindow();
	}

}