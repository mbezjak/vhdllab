package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.ComponentPlacement;

import java.util.List;

/**
 * This interface is a wrapper around {@link IComponentStorage}. It contains
 * methods for manipulating only views, not components as is the case with
 * <code>IComponentStorage</code>. However it has a higher lever of
 * abstraction then <code>IComponentStorage</code> and is preferred why for
 * manipulating views. Much like <code>IComponentStorage</code> does not
 * explicitly extend <code>IComponentContainer</code> this interface does not
 * explicitly extend <code>IComponentStorage</code>. The reason is not to
 * contain methods with lower level of abstraction.
 * 
 * @author Miro Bezjak
 * @see IEditorStorage
 * @see IProjectExplorerStorage
 * @see IComponentStorage
 * @see IComponentContainer
 */
public interface IViewStorage {

	/**
	 * Adds a view to a storage (ultimately to be presented to a user).
	 * 
	 * @param identifier
	 *            a unique view identifier
	 * @param title
	 *            a title of a view
	 * @param view
	 *            an view to add
	 * @return <code>true</code> if <code>view</code> was added to storage
	 *         or <code>false</code> if it is already in a storage (and thus
	 *         will not be added)
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	boolean add(String identifier, String title, IView view);

	/**
	 * Closes opened view.
	 * 
	 * @param identifier
	 *            a unique view identifier
	 * @return a view previously stored or <code>null</code> if it was not
	 *         opened at all
	 * @throws NullPointerException
	 *             if <code>identifier</code> is <code>null</code>
	 */
	IView close(String identifier);

	/**
	 * Closes specified view.
	 * 
	 * @param view
	 *            a view to close
	 * @return a closed view or <code>null</code> if it was not opened at all
	 * @throws NullPointerException
	 *             if <code>view</code> is <code>null</code>
	 */
	IView close(IView view);

	/**
	 * Moves view from one tabbed pane to another.
	 * 
	 * @param identifier
	 *            a unique view identifier
	 * @param placement
	 *            a location to destination tabbed pane
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if specified view is not opened
	 */
	void move(String identifier, ComponentPlacement placement);

	/**
	 * Returns an identifier for opened <code>view</code> or
	 * <code>null</code> if such view is not opened.
	 * 
	 * @param view a view to get identifier for
	 * @return an identifier for opened <code>view</code>
	 * @throws NullPointerException
	 *             if <code>view</code> is <code>null</code>
	 */
	String getIdentifierFor(IView view);

	/**
	 * Returns a view represented by <code>identifier</code> or
	 * <code>null</code> if such view is not opened.
	 * 
	 * @param identifier
	 *            a unique view identifier
	 * @return a view represented by <code>identifier</code>
	 * @throws NullPointerException
	 *             is <code>identifier</code> is <code>null</code>
	 */
	IView getOpenedView(String identifier);

	/**
	 * Returns a collection of all views that are currently opened. Returned
	 * value will never be <code>null</code> although it can be empty
	 * collection.
	 * 
	 * @return a collection of all views that are currently opened
	 */
	List<IView> getAllOpenedViews();

	/**
	 * Selects a view to be visible in tabbed pane.
	 * 
	 * @param identifier
	 *            a unique view identifier
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if specified view is not opened
	 */
	void setSelectedView(String identifier);

	/**
	 * Returns a selected view or <code>null</code> if no view is selected.
	 * <p>
	 * Selected view is a view that currently has focus (either view itself or
	 * any of his subcomponents) or view that had focus last (if, for example,
	 * any non-view gained focus).
	 * </p>
	 * 
	 * @return a currently selected view
	 */
	IView getSelectedView();

	/**
	 * Returns <code>true</code> if <code>view</code> is opened or
	 * <code>false</code> otherwise.
	 * 
	 * @param identifier
	 *            a unique view identifier
	 * @return <code>true</code> if <code>view</code> is opened;
	 *         <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if <code>identifier</code> is <code>null</code>
	 */
	boolean isViewOpened(String identifier);

	/**
	 * Sets a title for a view.
	 * 
	 * @param identifier
	 *            a unique view identifier
	 * @param title
	 *            a title to set
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if specified view is not opened
	 */
	void setTitle(String identifier, String title);

	/**
	 * Sets a tooltip text for a view.
	 * 
	 * @param identifier
	 *            a unique view identifier
	 * @param tooltip
	 *            a tooltip text to set
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if specified view is not opened
	 */
	void setToolTipText(String identifier, String tooltip);

	/**
	 * Returns the number of opened views.
	 * 
	 * @return the number of opened views
	 * @see #isEmpty()
	 */
	int getViewCount();

	/**
	 * Returns <code>true</code> if there is no opened views or
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if there is no opened views;
	 *         <code>false</code> otherwise
	 * @see #getEditorCount()
	 */
	boolean isEmpty();

}