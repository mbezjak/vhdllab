/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;

import java.util.List;

/**
 * Defines method for manipulating editor.
 * 
 * @author Miro Bezjak
 */
public interface IEditorManager {

	/**
	 * Opens a preferences editor.
	 * 
	 * @return opened editor or <code>null</code> if any error occurs
	 */
	IEditor openPreferences();

	/**
	 * Opens a text editor where a vhdl code for a specified file can be viewed.
	 * If specified editor can't generate vhdl code then this method will return
	 * <code>null</code>.
	 * 
	 * @param editor
	 *            an editor
	 * @return opened editor or <code>null</code> if any error occurs
	 * @throws NullPointerException
	 *             if <code>editor</code> is <code>null</code>
	 */
	IEditor viewVHDLCode(IEditor editor);

	/**
	 * Opens a text editor where a vhdl code for a specified file can be viewed.
	 * If specified editor can't generate vhdl code then this method will return
	 * <code>null</code>.
	 * 
	 * @param identifier
	 *            an editor identifier
	 * @return opened editor or <code>null</code> if any error occurs
	 * @throws NullPointerException
	 *             if <code>identifier</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if instance modifier of <code>identifier</code> is
	 *             <code>null</code>
	 */
	IEditor viewVHDLCode(IComponentIdentifier<FileIdentifier> identifier);

	/**
	 * Opens a specified editor. If an editor is already opened then this method
	 * will set specified <code>data</code> and simply return that editor.
	 * <p>
	 * Note that <code>projectName</code> and <code>fileName</code> in
	 * {@link IEditor#setFileContent(FileContent)} method will be set to
	 * <code>null</code>.
	 * </p>
	 * 
	 * @param identifier
	 *            an editor identifier
	 * @param data
	 *            a data to set
	 * @return opened editor or <code>null</code> if any error occurs
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @see IComponentIdentifier
	 */
	IEditor openEditor(IComponentIdentifier<?> identifier, String data);

	/**
	 * Opens an appropriate editor for specified resource. If an editor is
	 * already opened then this method will simply return it.
	 * 
	 * @param identifier
	 *            an editor identifier
	 * @return opened editor or <code>null</code> if any error occurs
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if instance modifier of <code>identifier</code> is
	 *             <code>null</code>
	 */
	IEditor openEditorByResource(IComponentIdentifier<FileIdentifier> identifier);

	/**
	 * Opens a specified editor. If an editor is already opened then this method
	 * will set specified <code>content</code> and simply return an editor.
	 * 
	 * @param identifier
	 *            an editor identifier
	 * @param content
	 *            a content to set
	 * @return opened editor or <code>null</code> if any error occurs
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	IEditor openEditor(IComponentIdentifier<?> identifier, FileContent content);

	/**
	 * Returns an editor represented by <code>identifier</code> or
	 * <code>null</code> if such editor is not opened.
	 * 
	 * @param identifier
	 *            an editor identifier
	 * @return an editor represented by an identifier
	 * @throws NullPointerException
	 *             is <code>identifier</code> is <code>null</code>
	 */
	IEditor getOpenedEditor(IComponentIdentifier<?> identifier);

	/**
	 * Saves a specified editor.
	 * 
	 * @param editor
	 *            an editor to save
	 * @throws NullPointerException
	 *             if <code>editor</code> is <code>null</code>
	 */
	void saveEditor(IEditor editor);

	/**
	 * Explicitly saves a specified editor. Explicit saving will try to save an
	 * editor even if it is declared a not savable by using declared explicit
	 * save class (if such a class exists).
	 * 
	 * @param editor
	 *            an editor to save
	 * @throws NullPointerException
	 *             if <code>editor</code> is <code>null</code>
	 * @throws IllegalStateException
	 *             if explicit save class can't be instantiated
	 */
	void saveEditorExplicitly(IEditor editor);

	/**
	 * Saves all opened editors.
	 */
	void saveAllEditors();

	/**
	 * Saves specified editors.
	 * 
	 * @param editorsToSave
	 *            editors to save
	 * @throws NullPointerException
	 *             is <code>editorsToSave</code> is <code>null</code>
	 */
	void saveEditors(List<IEditor> editorsToSave);

	/**
	 * Closes a specified editor. If <code>editor</code> is <code>null</code>
	 * no exception will be thrown and this method will simply return.
	 * 
	 * @param editor
	 *            an editor to close
	 */
	void closeEditor(IEditor editor);

	/**
	 * Closes a specified editor. If <code>editor</code> is <code>null</code>
	 * no exception will be thrown and this method will simply return. This
	 * method will not try to save an editor before closing it. This is useful
	 * when a file is to be deleted and an editor should be closed.
	 * 
	 * @param editor
	 *            an editor to close
	 */
	void closeEditorWithoutSaving(IEditor editor);

	/**
	 * Closes all opened editors.
	 */
	void closeAllEditors();

	/**
	 * Closes all opened editors except specified one. If <code>editor</code>
	 * is <code>null</code> no exception will be thrown and this method will
	 * simply return.
	 * 
	 * @param editorToKeepOpened
	 *            an editor to keep open
	 */
	void closeAllButThisEditor(IEditor editorToKeepOpened);

	/**
	 * Closes all specified editors. If <code>editorsToClose</code> is
	 * <code>null</code> no exception will be thrown and this method will
	 * simply return.
	 * 
	 * @param editorsToClose
	 *            an editors to close
	 */
	void closeEditors(List<IEditor> editorsToClose);

	/**
	 * Resets an editor title to make it visible to a user that an editor's
	 * content has been changed.
	 * 
	 * @param contentChanged
	 *            <code>true</code> if an editor content has been changed;
	 *            <code>false</code> otherwise.
	 * @param identifier
	 *            an identifier of an editor
	 */
	void resetEditorTitle(boolean contentChanged,
			IComponentIdentifier<?> identifier);

	/**
	 * Opens a dialog to save specified resources. If <code>openedEditors</code>
	 * is <code>null</code> then this method will simply return
	 * <code>false</code>.
	 * 
	 * @param openedEditors
	 *            editors specifying resource to save
	 * @param title
	 *            a title to set
	 * @param message
	 *            a message to set
	 * @return <code>true</code> if user canceled save dialog
	 */
	boolean saveResourcesWithDialog(List<IEditor> openedEditors, String title,
			String message);

	/**
	 * Returns <code>true</code> if specified editor is opened or
	 * <code>false</code> otherwise.
	 * 
	 * @param editor
	 *            an editor to check
	 * @return <code>true</code> if specified editor is opened;
	 *         <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if <code>editor</code> is <code>null</code>
	 */
	boolean isEditorOpened(IEditor editor);

	/**
	 * Returns <code>true</code> if specified editor is opened or
	 * <code>false</code> otherwise.
	 * 
	 * @param identifier
	 *            an identifier specifying an editor
	 * @return <code>true</code> if specified editor is opened;
	 *         <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if <code>identifier</code> is <code>null</code>
	 */
	boolean isEditorOpened(IComponentIdentifier<?> identifier);

	/**
	 * Returns all editors associated with specified instance modifier.
	 * 
	 * @param instanceModifier
	 *            an instance modifier to compare to
	 * @return all editors associated with specified instance modifier
	 * @throws NullPointerException
	 *             if <code>instanceModifier</code> is <code>null</code>
	 */
	List<IEditor> findAllEditorsAssociatedWith(Object instanceModifier);

	/**
	 * Returns all opened editors. Return value will never be <code>null</code>
	 * although it can be empty list.
	 * 
	 * @return all opened editors
	 */
	List<IEditor> getAllOpenedEditors();

	/**
	 * Return a collection of all editors that are currently opened and that
	 * have specified <code>projectName</code>. Returned value will never be
	 * <code>null</code> although it can be empty collection.
	 * 
	 * @param projectName
	 *            a project name
	 * @return a collection of all editors that are currently opened and that
	 *         have specified <code>projectName</code>
	 * @throws NullPointerException
	 *             is <code>projectName</code> is <code>null</code>
	 */
	List<IEditor> getOpenedEditorsThatHave(String projectName);

	/**
	 * Returns a selected editor or <code>null</code> if no editor is
	 * selected.
	 * <p>
	 * Selected editor is an editor that currently has focus (either editor
	 * itself or any of his subcomponents) or editor that had focus last (if,
	 * for example, any non-editor gained focus).
	 * </p>
	 * 
	 * @return a currently selected editor
	 */
	IEditor getSelectedEditor();

	/**
	 * Returns a title for a specified editor.
	 * 
	 * @param identifier
	 *            an identifier specifying an editor
	 * @return a title for a specified editor
	 * @throws NullPointerException
	 *             if <code>identifier</code> is <code>null</code>
	 */
	String getTitle(IComponentIdentifier<?> identifier);

	/**
	 * Sets a new title for a specified editor.
	 * 
	 * @param editor
	 *            an editor to set title to
	 * @param title
	 *            a title to set
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	void setTitle(IEditor editor, String title);

	/**
	 * Sets a new title for a specified editor.
	 * 
	 * @param identifier
	 *            an identifier specifying an editor
	 * @param title
	 *            a title to set
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	void setTitle(IComponentIdentifier<?> identifier, String title);

}