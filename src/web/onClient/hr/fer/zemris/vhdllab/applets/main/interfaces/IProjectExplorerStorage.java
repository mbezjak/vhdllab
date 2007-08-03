package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.ComponentPlacement;

/**
 * This interface is a wrapper around {@link IComponentStorage}. It contains
 * methods for manipulating only project explorer, not components as is the case
 * with <code>IComponentStorage</code>. However it has a higher lever of
 * abstraction then <code>IComponentStorage</code> and is preferred why for
 * manipulating project explorer. Much like <code>IComponentStorage</code>
 * does not explicitly extend <code>IComponentContainer</code> this interface
 * does not explicitly extend <code>IComponentStorage</code>. The reason is
 * not to contain methods with lower level of abstraction.
 * 
 * @author Miro Bezjak
 * @see IEditorStorage
 * @see IViewStorage
 * @see IComponentStorage
 * @see IComponentContainer
 */
public interface IProjectExplorerStorage {

	/**
	 * Adds a project explorer to a storage (ultimately to be presented to a
	 * user).
	 * 
	 * @param title
	 *            a title of a project explorer
	 * @param view
	 *            an project explorer to add
	 * @return <code>true</code> if <code>projectExplorer</code> was added
	 *         to storage or <code>false</code> if it is already in a storage
	 *         (and thus will not be added)
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	boolean add(String title, IProjectExplorer projectExplorer);

	/**
	 * Closes opened project explorer.
	 * 
	 * @return a closed project explorer
	 */
	IView close();

	/**
	 * Moves project explorer from one tabbed pane to another.
	 * 
	 * @param placement
	 *            a location to destination tabbed pane
	 * @throws NullPointerException
	 *             if <code>placement</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if project explorer is not opened
	 */
	void move(ComponentPlacement placement);

	/**
	 * Returns opened project explorer or <code>null</code> if project
	 * explorer is not opened.
	 * 
	 * @return a project explorer
	 */
	IProjectExplorer getProjectExplorer();

	/**
	 * Returns <code>true</code> if <code>project explorer</code> is opened
	 * or <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if <code>project explorer</code> is opened;
	 *         <code>false</code> otherwise
	 */
	boolean isProjectExplorerOpened();

	/**
	 * Sets a title for a project explorer.
	 * 
	 * @param title
	 *            a title to set
	 * @throws NullPointerException
	 *             if <code>title</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if project explorer is not opened
	 */
	void setTitle(String title);

	/**
	 * Sets a tooltip text for a project explorer.
	 * 
	 * @param tooltip
	 *            a tooltip text to set
	 * @throws NullPointerException
	 *             if <code>tooltip</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if project explorer is not opened
	 */
	void setToolTipText(String tooltip);

}