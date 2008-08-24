package hr.fer.zemris.vhdllab.applets.main.componentIdentifier;

import hr.fer.zemris.vhdllab.applets.main.ComponentGroup;
import hr.fer.zemris.vhdllab.applets.main.constant.ComponentTypes;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;

/**
 * Represents a unique identifier of an opened component (a component presented
 * to a user). It contains a group that a component belongs to, a unique string
 * that identifies a component (a component type) and an instance modifier to
 * allow multiple instances of a component.
 * <p>
 * For example: an instance modifier of a text editor will be a resource that is
 * opened (a {@link FileIdentifier}), a component type will be one specified in
 * {@link ComponentTypes#EDITOR_VHDL_SOURCE} and a component group will be
 * {@link ComponentGroup#EDITOR}.
 * </p>
 * 
 * @author Miro Bezjak
 * @param <T> a type of an instance modifier parameter
 * @see ComponentGroup
 * @see ComponentTypes
 */
public interface IComponentIdentifier<T> {

	/**
	 * Returns a group that component belongs to.
	 * 
	 * @return a group that component belongs to
	 */
	ComponentGroup getComponentGroup();

	/**
	 * Returns a string that uniquely identifies a component.
	 * 
	 * @return a string that uniquely identifies a component
	 */
	String getComponentType();

	/**
	 * Returns a modifier of this component's instance. Return value may be
	 * <code>null</code> and in that case it indicates that only one instance
	 * of this component exist.
	 * 
	 * @return a modifier of this component's instance; may be <code>null</code>
	 */
	T getInstanceModifier();

	/**
	 * Returns <code>true</code> if this component identifier has no instance
	 * modifier or <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this component identifier has no instance
	 *         modifier; <code>false</code> otherwise
	 */
	boolean isSingletonInstance();

	/**
	 * Follow a general contract of {@link Object#equals(Object)}.
	 * 
	 * @param o
	 *            a component identifier to compare
	 * @return <code>true</code> if two component identifiers are equal;
	 *         <code>false</code> otherwise
	 */
	boolean equals(Object o);

	/**
	 * Follow a general contract of {@link Object#hashCode()}.
	 * 
	 * @return a hash code of this component identifier
	 */
	int hashCode();

}
