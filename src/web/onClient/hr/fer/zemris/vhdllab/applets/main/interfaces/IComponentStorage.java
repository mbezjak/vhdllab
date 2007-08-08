package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.ComponentGroup;
import hr.fer.zemris.vhdllab.applets.main.ComponentPlacement;

import java.util.Collection;

import javax.swing.JComponent;

/**
 * A component storage defines methods for manipulating with components stored
 * in {@link IComponentContainer}. This interface has a higher level of
 * abstraction and is an extension to <code>IComponentContainer</code>.
 * However this interface does not explicitly extends
 * <code>IComponentContainer</code> because the purpose is to hide all methods
 * of <code>IComponentContainer</code> interface. Such methods may void
 * protection or not cache important information regarding added components.
 * Therefor this interface is a preferred way of manipulating components. This
 * interface, unlike <code>IComponentContainer</code>, does not contain
 * methods for adding them to panels (for users to notice difference). Instead
 * it uses <code>IComponentContainer</code> for such purposes.
 * <p>
 * Based upon <code>IComponentContainer</code>,
 * <code>IComponentStorage</code> also has the ability to store components in
 * 4 equal tabbed panes. Which one is chosen when adding a component is
 * determined by {@link ComponentPlacement} enum parameter.
 * 
 * @author Miro Bezjak
 * @see IEditorStorage
 * @see IViewStorage
 * @see IProjectExplorerStorage
 * @see IComponentContainer
 */
public interface IComponentStorage {

	/**
	 * Adds a component to storage (ultimately to be presented to a user).
	 * Component placement that will be used is determined from a property of
	 * component's group.
	 * <p>
	 * If component already exists in storage then this method will reset the
	 * title.
	 * </p>
	 * 
	 * @param identifier
	 *            a unique identifier for a <code>component</code> in a
	 *            specified <code>group</code>
	 * @param group
	 *            a group where <code>component</code> belong to
	 * @param title
	 *            a title that is used in tabbed pane when component is
	 *            displayed
	 * @param component
	 *            a component to add
	 * @return <code>true</code> if <code>component</code> was added to
	 *         storage or <code>false</code> if it is already in a storage
	 *         (and thus will not be added)
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @see #add(String, ComponentGroup, String, JComponent, ComponentPlacement)
	 */
	boolean add(String identifier, ComponentGroup group, String title,
			JComponent component);

	/**
	 * Adds a <code>component</code> to storage (ultimately to be presented to
	 * a user) with a unique <code>identifier</code> in a <code>group</code>.
	 * Component will be added in a location specified by <code>placement</code>.
	 * <p>
	 * For example: if <code>placement</code> is <code>LEFT</code> then it
	 * will be added in left tabbed pane.
	 * </p>
	 * <p>
	 * If component already exists in storage then this method will reset the
	 * title and depending on the <code>placement</code> move the
	 * <code>component</code>.
	 * </p>
	 * 
	 * @param identifier
	 *            a unique identifier for a <code>component</code> in a
	 *            specified <code>group</code>
	 * @param group
	 *            a group where <code>component</code> belong to
	 * @param title
	 *            a title that is used in tabbed pane when component is
	 *            displayed
	 * @param component
	 *            a component to add
	 * @param placement
	 *            a placement of a <code>component</code> (where to add a
	 *            component)
	 * @return <code>true</code> if <code>component</code> was added to
	 *         storage or <code>false</code> if it is already in a storage
	 *         (and thus will not be added)
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	boolean add(String identifier, ComponentGroup group, String title,
			JComponent component, ComponentPlacement placement);

	/**
	 * Removes a component from storage.
	 * 
	 * @param identifier
	 *            a component identifier
	 * @param group
	 *            a component group
	 * @return a component previously stored or <code>null</code> if it was
	 *         not stored at all
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	JComponent remove(String identifier, ComponentGroup group);

	/**
	 * Removes a specified component from storage.
	 * 
	 * @param component
	 *            a component to remove
	 * @throws NullPointerException
	 *             is <code>component</code> is <code>null</code>
	 */
	void remove(JComponent component);

	/**
	 * Moves component from one tabbed pane to another.
	 * 
	 * @param identifier
	 *            a component identifier
	 * @param group
	 *            a component group
	 * @param placement
	 *            a location to destination tabbed pane
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             is specified component is not stored
	 */
	void moveComponent(String identifier, ComponentGroup group,
			ComponentPlacement placement);

	/**
	 * Returns a location where a component is stored or <code>null</code> if
	 * a <code>component</code> is not stored.
	 * 
	 * @param component
	 *            a component for whom to return placement
	 * @return a location where a component is stored
	 * @throws NullPointerException
	 *             if <code>component</code> is <code>null</code>
	 */
	ComponentPlacement getComponentPlacement(JComponent component);

	/**
	 * Returns an identifier for a stored <code>component</code> or
	 * <code>null</code> if a <code>component</code> is not stored.
	 * 
	 * @param component
	 *            a component for whom to return identifier
	 * @return an identifier for a specified component
	 * @throws NullPointerException
	 *             if <code>component</code> is <code>null</code>
	 */
	String getIdentifierFor(JComponent component);

	/**
	 * Returns a component with specified <code>identifier</code> that belongs
	 * to specified <code>group</code> or <code>null</code> if no such
	 * component exists.
	 * 
	 * @param identifier
	 *            a component identifier
	 * @param group
	 *            a component group
	 * @return a component
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	JComponent getComponent(String identifier, ComponentGroup group);

	/**
	 * Returns all components that belong to specified <code>group</code>.
	 * Returned value will never be <code>null</code> although it can be empty
	 * collection.
	 * 
	 * @param group
	 *            a component group
	 * @return all component with specified <code>group</code>
	 * @throws NullPointerException
	 *             if <code>group</code> is <code>null</code>
	 */
	Collection<JComponent> getComponents(ComponentGroup group);

	/**
	 * Sets a specified component as selected.
	 * 
	 * @param identifier
	 *            a component identifier
	 * @param group
	 *            a component group
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             is specified component is not stored
	 */
	void setSelectedComponent(String identifier, ComponentGroup group);

	/**
	 * Returns a selected component or <code>null</code> if no component is
	 * selected.
	 * <p>
	 * Selected component is a component that currently has focus (either
	 * component itself or any of his subcomponents) or a component that had
	 * focus last (if, for example, any component not part of
	 * {@link ComponentGroup} gained focus).
	 * </p>
	 * 
	 * @return a selected component
	 */
	JComponent getSelectedComponent();

	/**
	 * Returns a selected component having specified <code>group</code> or
	 * <code>null</code> if no such component is selected.
	 * <p>
	 * Selected component is a component that currently has focus (either
	 * component itself or any of his subcomponents) or a component that had
	 * focus last (if, for example, any component not part of specified
	 * <code>group</code> gained focus).
	 * </p>
	 * 
	 * @param group
	 *            a component group
	 * @return a selected component having specified <code>group</code>
	 * @throws NullPointerException
	 *             if <code>group</code> is <code>null</code>
	 */
	JComponent getSelectedComponent(ComponentGroup group);

	/**
	 * Returns <code>true</code> if this storage contains specified component
	 * or <code>false</code> otherwise.
	 * 
	 * @param identifier
	 *            a component identifier
	 * @param group
	 *            a component group
	 * @return <code>true</code> if this storage contains specified component;
	 *         <code>false</code> otherwise
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	boolean contains(String identifier, ComponentGroup group);

	/**
	 * Sets a title for a component.
	 * 
	 * @param identifier
	 *            a component identifier
	 * @param group
	 *            a component group
	 * @param title
	 *            a title to set
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             is specified component is not stored
	 */
	void setTitle(String identifier, ComponentGroup group, String title);

	/**
	 * Returns a title for a component.
	 * 
	 * @param identifier
	 *            a component identifier
	 * @param group
	 *            a component group
	 * @return a title
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             is specified component is not stored
	 */
	String getTitleFor(String identifier, ComponentGroup group);

	/**
	 * Sets a tooltip for a component.
	 * 
	 * @param identifier
	 *            a component identifier
	 * @param group
	 *            a component group
	 * @param tooltip
	 *            a tooltip to set
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             is specified component is not stored
	 */
	void setToolTipText(String identifier, ComponentGroup group, String tooltip);

	/**
	 * Returns the number of stored components.
	 * 
	 * @return a component count
	 * @see #getComponentCountFor(ComponentGroup)
	 */
	int getComponentCount();

	/**
	 * Returns the number of stored components that have specified
	 * <code>group</code>.
	 * 
	 * @param group
	 *            a component group
	 * @return the number of stored components that have specified
	 *         <code>group</code>
	 * @throws NullPointerException
	 *             is <code>group</code> is <code>null</code>
	 * @see #getComponentCount()
	 */
	int getComponentCountFor(ComponentGroup group);

}