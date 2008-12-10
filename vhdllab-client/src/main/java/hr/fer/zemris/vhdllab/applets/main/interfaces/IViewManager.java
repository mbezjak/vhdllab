package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier;

import java.util.List;

/**
 * Defines methods for manipulating views.
 * 
 * @author Miro Bezjak
 */
public interface IViewManager {

	/**
	 * Openes a project explorer.
	 */
	void openProjectExplorer();

	/**
	 * Opens a specified view. If a view is already opened then this method will
	 * simply return that view.
	 * 
	 * @param identifier
	 *            a view identifier
	 * @return opened view or <code>null</code> if any error occurs
	 * @throws NullPointerException
	 *             if <code>identifier</code> is <code>null</code>
	 */
	IView openView(IComponentIdentifier<?> identifier);

	/**
	 * Returns a view represented by <code>identifier</code> or
	 * <code>null</code> if such view is not opened.
	 * 
	 * @param identifier
	 *            a view identifier
	 * @return a view represented by an identifier
	 * @throws NullPointerException
	 *             is <code>identifier</code> is <code>null</code>
	 */
	IView getOpenedView(IComponentIdentifier<?> identifier);

	/**
	 * Closes a specified view. If <code>view</code> is <code>null</code> no
	 * exception will be thrown and this method will simply return.
	 * 
	 * @param view
	 *            a view to close
	 */
	void closeView(IView view);

	/**
	 * Closes all opened views.
	 */
	void closeAllViews();

	/**
	 * Closes all opened views except specified one. If <code>view</code> is
	 * <code>null</code> no exception will be thrown and this method will
	 * simply return.
	 * 
	 * @param viewToKeepOpened
	 *            a view to keep open
	 */
	void closeAllButThisView(IView viewToKeepOpened);

	/**
	 * Closes all specified views. If <code>viewsToClose</code> is
	 * <code>null</code> no exception will be thrown and this method will
	 * simply return.
	 * 
	 * @param viewsToClose
	 *            a views to close
	 */
	void closeViews(List<IView> viewsToClose);

	/**
	 * Returns <code>true</code> if specified view is opened or
	 * <code>false</code> otherwise.
	 * 
	 * @param view
	 *            a view to check
	 * @return <code>true</code> if specified view is opened;
	 *         <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if <code>view</code> is <code>null</code>
	 */
	boolean isViewOpened(IView view);

	/**
	 * Returns <code>true</code> if specified view is opened or
	 * <code>false</code> otherwise.
	 * 
	 * @param identifier
	 *            an identifier specifying a view
	 * @return <code>true</code> if specified view is opened;
	 *         <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if <code>identifier</code> is <code>null</code>
	 */
	boolean isViewOpened(IComponentIdentifier<?> identifier);

	/**
	 * Returns all views associated with specified instance modifier.
	 * 
	 * @param instanceModifier
	 *            an instance modifier to compare to
	 * @return all views associated with specified instance modifier
	 * @throws NullPointerException
	 *             if <code>instanceModifier</code> is <code>null</code>
	 */
	List<IView> findAllViewsAssociatedWith(Object instanceModifier);

	/**
	 * Returns all opened views. Return value will never be <code>null</code>
	 * although it can be empty list.
	 * 
	 * @return all opened views
	 */
	List<IView> getAllOpenedViews();

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
	 * Returns a title for a specified view.
	 * 
	 * @param identifier
	 *            an identifier specifying a view
	 * @return a title for a specified view
	 * @throws NullPointerException
	 *             if <code>identifier</code> is <code>null</code>
	 */
	String getTitle(IComponentIdentifier<?> identifier);

	/**
	 * Sets a new title for a specified view.
	 * 
	 * @param view
	 *            a view to set title to
	 * @param title
	 *            a title to set
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	void setTitle(IView view, String title);

	/**
	 * Sets a new title for a specified view.
	 * 
	 * @param identifier
	 *            an identifier specifying a view
	 * @param title
	 *            a title to set
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	void setTitle(IComponentIdentifier<?> identifier, String title);

	/**
	 * Returns an identifier for an opened <code>view</code> or
	 * <code>null</code> if an <code>view</code> is not stored.
	 * 
	 * @param view
	 *            a view for whom to return identifier
	 * @return an identifier for a specified view
	 * @throws NullPointerException
	 *             if <code>view</code> is <code>null</code>
	 */
	IComponentIdentifier<?> getIdentifierFor(IView view);

}