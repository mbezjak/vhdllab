package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.ComponentPlacement;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;

import java.util.List;

/**
 * This interface is a wrapper around {@link IComponentStorage}. It contains
 * methods for manipulating only editors, not components as is the case with
 * <code>IComponentStorage</code>. However it has a higher lever of
 * abstraction then <code>IComponentStorage</code> and is preferred why for
 * manipulating editors. Much like <code>IComponentStorage</code> does not
 * explicitly extend <code>IComponentContainer</code> this interface does not
 * explicitly extend <code>IComponentStorage</code>. The reason is not to
 * contain methods with lower level of abstraction.
 * 
 * @author Miro Bezjak
 * @see IViewStorage
 * @see IProjectExplorerStorage
 * @see IComponentStorage
 * @see IComponentContainer
 */
public interface IEditorStorage {

	/**
	 * Adds an editor to a storage (ultimately to be presented to a user). This
	 * method can be used only if an editor has a valid both project and file
	 * name (non-null) otherwise it will throw {@link IllegalArgumentException}.
	 * <p>
	 * Editor identifier and title will be created using
	 * {@link #createEditorIdentifierFor(IEditor)} and
	 * {@link #createTitleFor(IEditor)} respectfully.
	 * </p>
	 * 
	 * @param editor
	 *            an editor to add
	 * @return <code>true</code> if <code>editor</code> was added to storage
	 *         or <code>false</code> if it is already in a storage (and thus
	 *         will not be added)
	 * @throws NullPointerException
	 *             if <code>editor</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if editor does not have a valid project and file name
	 * @see #add(String, String, IEditor)
	 */
	boolean add(IEditor editor);

	/**
	 * Adds an editor to a storage (ultimately to be presented to a user).
	 * 
	 * @param identifier
	 *            a unique editor identifier
	 * @param title
	 *            a title of an editor
	 * @param editor
	 *            an editor to add
	 * @return <code>true</code> if <code>editor</code> was added to storage
	 *         or <code>false</code> if it is already in a storage (and thus
	 *         will not be added)
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @see #add(IEditor)
	 */
	boolean add(String identifier, String title, IEditor editor);

	/**
	 * Closes opened editor. This method can be used only if an editor has a
	 * valid both project and file name (non-null) otherwise it will throw
	 * {@link IllegalArgumentException}.
	 * <p>
	 * Editor identifier will be created using
	 * {@link #createEditorIdentifierFor(IEditor)}.
	 * </p>
	 * 
	 * @param editor
	 *            an editor to close
	 * @return an editor previously stored or <code>null</code> if it was not
	 *         stored at all
	 * @throws NullPointerException
	 *             if <code>editor</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if editor does not have a valid project and file name
	 * @see #close(String)
	 */
	IEditor close(IEditor editor);

	/**
	 * Closes opened editor.
	 * 
	 * @param identifier
	 *            a unique editor identifier
	 * @return an editor previously stored or <code>null</code> if it was not
	 *         stored at all
	 * @throws NullPointerException
	 *             if <code>identifier</code> is <code>null</code>
	 * @see #close(IEditor)
	 */
	IEditor close(String identifier);

	/**
	 * Moves editor from one tabbed pane to another. This method can be used
	 * only if an editor has a valid both project and file name (non-null)
	 * otherwise it will throw {@link IllegalArgumentException}.
	 * <p>
	 * Editor identifier will be created using
	 * {@link #createEditorIdentifierFor(IEditor)}.
	 * </p>
	 * 
	 * @param editor
	 *            an editor to move
	 * @param placement
	 *            a location to destination tabbed pane
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if specified editor is not opened or if editor does not have
	 *             a valid project and file name
	 * @see #move(String, ComponentPlacement)
	 */
	void move(IEditor editor, ComponentPlacement placement);

	/**
	 * Moves editor from one tabbed pane to another.
	 * 
	 * @param identifier
	 *            a unique editor identifier
	 * @param placement
	 *            a location to destination tabbed pane
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if specified editor is not opened
	 * @see #move(IEditor, ComponentPlacement)
	 */
	void move(String identifier, ComponentPlacement placement);

	/**
	 * Returns an editor represented by <code>projectName</code> and
	 * <code>fileName</code> or <code>null</code> if such editor is not
	 * opened.
	 * <p>
	 * Editor identifier will be created using
	 * {@link #createEditorIdentifierFor(FileIdentifier)}.
	 * </p>
	 * 
	 * @param projectName
	 *            a project name
	 * @param fileName
	 *            a file name
	 * @return an editor represented by <code>projectName</code> and
	 *         <code>fileName</code>
	 * @throws NullPointerException
	 *             is either parameter is <code>null</code>
	 * @see #getOpenedEditor(String)
	 */
	IEditor getOpenedEditor(String projectName, String fileName);

	/**
	 * Returns an editor represented by <code>identifier</code> or
	 * <code>null</code> if such editor is not opened.
	 * 
	 * @param identifier
	 *            a unique editor identifier
	 * @return an editor represented by <code>identifier</code>
	 * @throws NullPointerException
	 *             is <code>identifier</code> is <code>null</code>
	 * @see #getOpenedEditor(String, String)
	 */
	IEditor getOpenedEditor(String identifier);

	/**
	 * Returns a collection of all editors that are currently opened. Returned
	 * value will never be <code>null</code> although it can be empty
	 * collection.
	 * 
	 * @return a collection of all editors that are currently opened
	 * @see #getOpenedEditorsThatHave(String)
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
	 * @see #getOpenedEditorsThatHave(String)
	 */
	List<IEditor> getOpenedEditorsThatHave(String projectName);

	/**
	 * Selects an editor to be visible in tabbed pane. This method can be used
	 * only if an editor has a valid both project and file name (non-null)
	 * otherwise it will throw {@link IllegalArgumentException}.
	 * <p>
	 * Editor identifier will be created using
	 * {@link #createEditorIdentifierFor(IEditor)}.
	 * </p>
	 * 
	 * @param editor
	 *            an editor to select
	 * @throws NullPointerException
	 *             if <code>editor</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if specified editor is not opened or if editor does not have
	 *             a valid project and file name
	 * @see #setSelectedEditor(String, IEditor)
	 */
	void setSelectedEditor(IEditor editor);

	/**
	 * Selects an editor to be visible in tabbed pane.
	 * 
	 * @param identifier
	 *            a unique editor identifier
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if specified editor is not opened
	 * @see #setSelectedEditor(IEditor)
	 */
	void setSelectedEditor(String identifier);

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
	 * Returns <code>true</code> if <code>editor</code> is opened or
	 * <code>false</code> otherwise. This method can be used only if an editor
	 * has a valid both project and file name (non-null) otherwise it will throw
	 * {@link IllegalArgumentException}.
	 * <p>
	 * Editor identifier will be created using
	 * {@link #createEditorIdentifierFor(IEditor)}.
	 * </p>
	 * 
	 * @param editor
	 *            an editor to check
	 * @return <code>true</code> if <code>editor</code> is opened;
	 *         <code>false</code> otherwise
	 * @throws NullPointerException
	 *             is <code>editor</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if editor does not have a valid project and file name
	 * @see #isEditorOpened(String)
	 * @see #isEditorOpened(String, String)
	 */
	boolean isEditorOpened(IEditor editor);

	/**
	 * Returns <code>true</code> if editor represented with
	 * <code>projectName</code> and <code>fileName</code> is opened or
	 * <code>false</code> otherwise.
	 * <p>
	 * Editor identifier will be created using
	 * {@link #createEditorIdentifierFor(FileIdentifier)}.
	 * </p>
	 * 
	 * @param projectName
	 *            a project name
	 * @param fileName
	 *            a file name
	 * @return <code>true</code> if editor with specified
	 *         <code>projectName</code> and <code>fileName</code> is opened;
	 *         <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @see #isEditorOpened(IEditor)
	 * @see #isEditorOpened(String, String)
	 */
	boolean isEditorOpened(String projectName, String fileName);

	/**
	 * Returns <code>true</code> if <code>editor</code> is opened or
	 * <code>false</code> otherwise.
	 * 
	 * @param identifier
	 *            a unique editor identifier
	 * @return <code>true</code> if <code>editor</code> is opened;
	 *         <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if <code>identifier</code> is <code>null</code>
	 * @see #isEditorOpened(IEditor)
	 * @see #isEditorOpened(String)
	 */
	boolean isEditorOpened(String identifier);

	/**
	 * Sets a title for an editor. This method can be used only if an editor has
	 * a valid both project and file name (non-null) otherwise it will throw
	 * {@link IllegalArgumentException}.
	 * <p>
	 * Editor identifier will be created using
	 * {@link #createEditorIdentifierFor(IEditor)}.
	 * </p>
	 * 
	 * @param editor
	 *            an editor to set title to
	 * @param title
	 *            a title to set
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if specified editor is not opened or if editor does not have
	 *             a valid project and file name
	 * @see #setTitle(String, IEditor, String)
	 */
	void setTitle(IEditor editor, String title);

	/**
	 * Sets a title for an editor.
	 * 
	 * @param identifier
	 *            a unique editor identifier
	 * @param title
	 *            a title to set
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if specified editor is not opened
	 * @see #setTitle(IEditor, String)
	 */
	void setTitle(String identifier, String title);

	/**
	 * Sets a tooltip text for an editor. This method can be used only if an
	 * editor has a valid both project and file name (non-null) otherwise it
	 * will throw {@link IllegalArgumentException}.
	 * <p>
	 * Editor identifier will be created using
	 * {@link #createEditorIdentifierFor(IEditor)}.
	 * </p>
	 * 
	 * @param editor
	 *            an editor to set tooltip text to
	 * @param tooltip
	 *            a tooltip text to set
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if specified editor is not opened or if editor does not have
	 *             a valid project and file name
	 * @see #setToolTipText(String, IEditor, String)
	 */
	void setToolTipText(IEditor editor, String tooltip);

	/**
	 * Sets a tooltip text for an editor.
	 * 
	 * @param identifier
	 *            a unique editor identifier
	 * @param tooltip
	 *            a tooltip text to set
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if specified editor is not opened
	 * @see #setToolTipText(IEditor, String)
	 */
	void setToolTipText(String identifier, String tooltip);

	/**
	 * Returns the number of opened editors.
	 * 
	 * @return the number of opened editors
	 * @see #isEmpty()
	 */
	int getEditorCount();

	/**
	 * Returns <code>true</code> if there is no opened editors or
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if there is no opened editors;
	 *         <code>false</code> otherwise
	 * @see #getEditorCount()
	 */
	boolean isEmpty();

	/**
	 * Creates a title for a specified editor. This method can be used only if
	 * an editor has a valid both project and file name (non-null) otherwise it
	 * will throw {@link IllegalArgumentException}.
	 * 
	 * @param editor
	 *            an editor
	 * @return created title
	 * @throws NullPointerException
	 *             if <code>editor</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if editor does not have a valid project and file name
	 * @see #createTitleFor(FileIdentifier)
	 */
	String createTitleFor(IEditor editor);

	/**
	 * Creates a title for a specified editor. This method can be used only if
	 * an file identifier has a valid both project and file name (non-null)
	 * otherwise it will throw {@link IllegalArgumentException}.
	 * 
	 * @param identifier
	 *            an identifier of resource contained in an editor
	 * @return created title
	 * @throws NullPointerException
	 *             if <code>identifier</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if file identifier does not have a valid project and file
	 *             name
	 * @see #createTitleFor(String, String)
	 */
	String createTitleFor(FileIdentifier identifier);

	/**
	 * Creates a title for a specified resource.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @param fileName
	 *            a name of a file
	 * @return created title
	 * @throws IllegalArgumentException
	 *             if either parameter is <code>null</code>
	 */
	String createTitleFor(String projectName, String fileName);

	/**
	 * Creates a unique identifier for a specified editor. This method can be
	 * used only if an editor has a valid both project and file name (non-null)
	 * otherwise it will throw {@link IllegalArgumentException}.
	 * 
	 * @param editor
	 *            an editor
	 * @return created unique identifier
	 * @throws NullPointerException
	 *             if <code>editor</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if editor does not have a valid project and file name
	 * @see #createEditorIdentifierFor(FileIdentifier)
	 */
	String createEditorIdentifierFor(IEditor editor);

	/**
	 * Creates a unique identifier for a specified editor. This method can be
	 * used only if an file identifier has a valid both project and file name
	 * (non-null) otherwise it will throw {@link IllegalArgumentException}.
	 * 
	 * @param identifier
	 *            an identifier of resource contained in an editor
	 * @return created unique identifier
	 * @throws NullPointerException
	 *             if <code>identifier</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if file identifier does not have a valid project and file
	 *             name
	 * @see #createEditorIdentifierFor(IEditor)
	 */
	String createEditorIdentifierFor(FileIdentifier identifier);

}