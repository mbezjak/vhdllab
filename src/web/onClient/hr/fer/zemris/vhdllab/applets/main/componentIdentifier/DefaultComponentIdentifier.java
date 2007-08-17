package hr.fer.zemris.vhdllab.applets.main.componentIdentifier;

import hr.fer.zemris.vhdllab.applets.main.ComponentGroup;

/**
 * This is a default implementation of {@link IComponentIdentifier}.
 * 
 * @author Miro Bezjak
 * @param <T> a type of an instance modifier parameter
 */
public class DefaultComponentIdentifier<T> implements IComponentIdentifier<T> {

	/**
	 * A group that component belongs to.
	 */
	private ComponentGroup group;
	/**
	 * A string that uniquely identifies a component.
	 */
	private String type;
	/**
	 * A modifier that allows (and identifies) multiple instance of specified
	 * component; may be <code>null</code>.
	 */
	private T modifier;

	/**
	 * Constructs a default component identifier with a single instance
	 * modifier.
	 * 
	 * @param group
	 *            a group that component belongs to
	 * @param componentType
	 *            a string that uniquely identifies a component
	 * @throws NullPointerException
	 *             if <code>group</code> or <code>componentType</code> is
	 *             <code>null</code>
	 */
	public DefaultComponentIdentifier(ComponentGroup group, String componentType) {
		this(group, componentType, null);
	}

	/**
	 * Constructs a default component identifier out of specified parameters.
	 * <code>instanceModifier</code> may be <code>null</code> and in that
	 * case it indicates that only one instance of specified component may
	 * exist.
	 * 
	 * @param group
	 *            a group that component belongs to
	 * @param componentType
	 *            a string that uniquely identifies a component
	 * @param instanceModifier
	 *            a modifier that allows (and identifies) multiple instance of
	 *            specified component; may be <code>null</code>
	 * @throws NullPointerException
	 *             if <code>group</code> or <code>componentType</code> is
	 *             <code>null</code>
	 */
	public DefaultComponentIdentifier(ComponentGroup group,
			String componentType, T instanceModifier) {
		if (group == null) {
			throw new NullPointerException("Component group cant be null");
		}
		if (componentType == null) {
			throw new NullPointerException("Component type cant be null");
		}
		this.group = group;
		this.type = componentType;
		this.modifier = instanceModifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier#getComponentGroup()
	 */
	@Override
	public ComponentGroup getComponentGroup() {
		return group;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier#getComponentType()
	 */
	@Override
	public String getComponentType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier#getInstanceModifier()
	 */
	@Override
	public T getInstanceModifier() {
		return modifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier#isSingletonInstance()
	 */
	@Override
	public boolean isSingletonInstance() {
		return modifier != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + group.hashCode();
		result = prime * result + type.hashCode();
		result = prime * result
				+ ((modifier == null) ? 0 : modifier.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DefaultComponentIdentifier<T> other = (DefaultComponentIdentifier<T>) obj;
		if (!group.equals(other.group))
			return false;
		if (!type.equals(other.type))
			return false;
		if (modifier == null) {
			if (other.modifier != null)
				return false;
		} else if (!modifier.equals(other.modifier))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		sb.append(group.toString()).append("$").append(type).append("#");
		if (isSingletonInstance()) {
			sb.append(modifier.toString());
		} else {
			sb.append("SINGLETON-INSTANCE");
		}
		return sb.toString();
	}

}
