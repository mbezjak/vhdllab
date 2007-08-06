package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;
import hr.fer.zemris.vhdllab.preferences.PropertyAccessException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

/**
 * This is a default implementation of {@link IComponentStorage}.
 * 
 * @author Miro Bezjak
 */
public class DefaultComponentStorage implements IComponentStorage {

	/**
	 * All stored components.
	 */
	private Map<ComponentIdentifier, ComponentInformation> components;
	/**
	 * This is a other directional map for <code>components</code> field.
	 */
	private Map<JComponent, ComponentIdentifier> bidiComponents;
	/**
	 * A grouped components.
	 */
	private Map<ComponentGroup, Collection<JComponent>> groups;
	/**
	 * A container where all components are presented to a user.
	 */
	private IComponentContainer componentContainer;
	/**
	 * A system container for a method of getting preferences.
	 */
	private ISystemContainer systemContainer;

	/**
	 * Constructs a component storage.
	 * 
	 * @param componentContainer
	 *            a container where all component are presented to a user
	 * @param systemContainer
	 *            a system container for a method of getting preferences
	 * @throws NullPointerException
	 *             if <code>componentContainer</code> or
	 *             <code>projectContainer</code> is <code>null</code>
	 */
	public DefaultComponentStorage(IComponentContainer componentContainer,
			ISystemContainer systemContainer) {
		if (componentContainer == null) {
			throw new NullPointerException("Component container cant be null.");
		}
		if (systemContainer == null) {
			throw new NullPointerException("System container cant be null.");
		}
		components = new HashMap<ComponentIdentifier, ComponentInformation>();
		bidiComponents = new HashMap<JComponent, ComponentIdentifier>();
		groups = new HashMap<ComponentGroup, Collection<JComponent>>();
		this.componentContainer = componentContainer;
		this.systemContainer = systemContainer;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#add(java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentGroup, java.lang.String,
	 *      javax.swing.JComponent)
	 */
	@Override
	public boolean add(String identifier, ComponentGroup group, String title,
			JComponent component) {
		if (group == null) {
			throw new NullPointerException("Component group cant be null.");
		}
		IUserPreferences preferences = systemContainer.getPreferences();
		String property;
		switch (group) {
		case EDITOR:
			try {
				property = preferences
						.getProperty(UserFileConstants.SYSTEM_DEFAULT_EDITOR_PLACEMENT);
			} catch (PropertyAccessException e) {
				property = ComponentPlacement.CENTER.name();
			}
			break;
		case VIEW:
			try {
				property = preferences
						.getProperty(UserFileConstants.SYSTEM_DEFAULT_VIEW_PLACEMENT);
			} catch (PropertyAccessException e) {
				property = ComponentPlacement.BOTTOM.name();
			}
			break;
		case PROJECT_EXPLORER:
			try {
				property = preferences
						.getProperty(UserFileConstants.SYSTEM_DEFAULT_PROJECT_EXPLORER_PLACEMENT);
			} catch (PropertyAccessException e) {
				property = ComponentPlacement.LEFT.name();
			}
			break;
		default:
			property = ComponentPlacement.CENTER.name();
			break;
		}
		ComponentPlacement placement;
		try {
			placement = ComponentPlacement.valueOf(property);
		} catch (RuntimeException e) {
			placement = ComponentPlacement.CENTER;
		}
		return add(identifier, group, title, component, placement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#add(java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentGroup, java.lang.String,
	 *      javax.swing.JComponent,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentPlacement)
	 */
	@Override
	public boolean add(String identifier, ComponentGroup group, String title,
			JComponent component, ComponentPlacement placement) {
		if (title == null) {
			throw new NullPointerException("Title cant be null.");
		}
		if (component == null) {
			throw new NullPointerException("Component cant be null.");
		}
		if (placement == null) {
			throw new NullPointerException("Component placement cant be null.");
		}
		/*
		 * Component identifier takes care of checking if identifier or group is
		 * null.
		 */
		ComponentIdentifier ci = new ComponentIdentifier(identifier, group);
		return add(ci, title, component, placement);
	}

	/**
	 * This is an actual implementation of <code>add</code> method.
	 * 
	 * @param ci
	 *            an identifier of a component
	 * @param title
	 *            a title to use
	 * @param component
	 *            a component to add
	 * @param placement
	 *            where to add a component
	 * @return see public add methods
	 */
	private boolean add(ComponentIdentifier ci, String title,
			JComponent component, ComponentPlacement placement) {
		if (contains(ci)) {
			ComponentInformation info = components.get(ci);
			if (!placement.equals(info.getPlacement())) {
				moveComponent(ci, placement);
			}
			setTitle(ci, title); // always after possible move
			return false;
		} else {
			ComponentInformation info = new ComponentInformation(component,
					placement);
			components.put(ci, info);
			bidiComponents.put(component, ci);
			ComponentGroup group = ci.getGroup();
			Collection<JComponent> groupedComponents = groups.get(group);
			if (groupedComponents == null) {
				groupedComponents = new ArrayList<JComponent>();
			}
			groupedComponents.add(component);
			groups.put(group, groupedComponents);
			componentContainer.addComponent(title, component, group, placement);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#remove(java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentGroup)
	 */
	@Override
	public JComponent remove(String identifier, ComponentGroup group) {
		/*
		 * Component identifier takes care of checking if identifier or group is
		 * null.
		 */
		ComponentIdentifier ci = new ComponentIdentifier(identifier, group);
		ComponentInformation info = remove(ci);
		return info != null ? info.getComponent() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#remove(javax.swing.JComponent)
	 */
	@Override
	public JComponent remove(JComponent component) {
		if (component == null) {
			throw new NullPointerException("Component cant be null");
		}
		if (!bidiComponents.containsKey(component)) {
			return null;
		}
		ComponentIdentifier ci = bidiComponents.get(component);
		ComponentInformation info = remove(ci);
		return info != null ? info.getComponent() : null;
	}

	/**
	 * This is an actual implementation of
	 * {@link #remove(String, ComponentGroup)} method.
	 * 
	 * @param ci
	 *            a component identifier
	 * @return a ComponentInformation that was previously stored or
	 *         <code>null</code> if component was not stored at all
	 */
	private ComponentInformation remove(ComponentIdentifier ci) {
		if (!contains(ci)) {
			return null;
		}
		ComponentInformation info = components.remove(ci);
		bidiComponents.remove(info.getComponent());
		Collection<JComponent> groupedComponents = groups.get(ci.getGroup());
		groupedComponents.remove(info.getComponent());
		if (groupedComponents.isEmpty()) {
			groups.remove(ci.getGroup());
		}
		componentContainer.removeComponent(info.getComponent(), info
				.getPlacement());
		return info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#moveComponent(java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentGroup,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentPlacement)
	 */
	@Override
	public void moveComponent(String identifier, ComponentGroup group,
			ComponentPlacement placement) {
		if (placement == null) {
			throw new NullPointerException("Component placement cant be null.");
		}
		/*
		 * Component identifier takes care of checking if identifier or group is
		 * null.
		 */
		ComponentIdentifier ci = new ComponentIdentifier(identifier, group);
		moveComponent(ci, placement);
	}

	/**
	 * This is an actual implementation of
	 * {@link #moveComponent(String, ComponentGroup, ComponentPlacement)}
	 * method.
	 * 
	 * @param ci
	 *            a component identifier
	 * @param placement
	 *            a location of a component
	 * @throws IllegalArgumentException
	 *             is specified component is not stored
	 */
	private void moveComponent(ComponentIdentifier ci,
			ComponentPlacement placement) {
		if (!contains(ci)) {
			throw new IllegalArgumentException("Component " + ci.toString()
					+ " not stored");
		}
		String title = getTitleFor(ci);
		JComponent component = remove(ci).getComponent();
		add(ci, title, component, placement);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#getComponentPlacement(javax.swing.JComponent)
	 */
	@Override
	public ComponentPlacement getComponentPlacement(JComponent component) {
		if(component == null) {
			throw new NullPointerException("Component cant be null");
		}
		if(!bidiComponents.containsKey(component)) {
			return null;
		}
		ComponentIdentifier ci = bidiComponents.get(component);
		return components.get(ci).getPlacement();
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#getIdentifierFor(javax.swing.JComponent)
	 */
	@Override
	public String getIdentifierFor(JComponent component) {
		if(component == null) {
			throw new NullPointerException("Component cant be null");
		}
		if(!bidiComponents.containsKey(component)) {
			return null;
		}
		ComponentIdentifier ci = bidiComponents.get(component);
		return ci.getIdentifier();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#getComponent(java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentGroup)
	 */
	@Override
	public JComponent getComponent(String identifier, ComponentGroup group) {
		/*
		 * Component identifier takes care of checking if identifier or group is
		 * null.
		 */
		ComponentIdentifier ci = new ComponentIdentifier(identifier, group);
		return getComponent(ci);
	}

	/**
	 * This is an actual implementation of
	 * {@link #getComponent(String, ComponentGroup)} method.
	 * 
	 * @param ci
	 *            a component identifier
	 * @return a component
	 */
	private JComponent getComponent(ComponentIdentifier ci) {
		if (!contains(ci)) {
			return null;
		}
		return components.get(ci).getComponent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#getComponents(hr.fer.zemris.vhdllab.applets.main.ComponentGroup)
	 */
	@Override
	public Collection<JComponent> getComponents(ComponentGroup group) {
		if (group == null) {
			throw new NullPointerException("Component group cant be null.");
		}
		Collection<JComponent> groupedComponents = groups.get(group);
		if (groupedComponents == null) {
			return Collections.emptySet();
		} else {
			return groupedComponents;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#setSelectedComponent(java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentGroup)
	 */
	@Override
	public void setSelectedComponent(String identifier, ComponentGroup group) {
		/*
		 * Component identifier takes care of checking if identifier or group is
		 * null.
		 */
		ComponentIdentifier ci = new ComponentIdentifier(identifier, group);
		setSelectedComponent(ci);
	}

	/**
	 * This is an actual implementation of
	 * {@link #setSelectedComponent(String, ComponentGroup)} method.
	 * 
	 * @param ci
	 *            a component identifier
	 * @throws IllegalArgumentException
	 *             is specified component is not stored
	 */
	private void setSelectedComponent(ComponentIdentifier ci) {
		if (!contains(ci)) {
			throw new IllegalArgumentException("Component " + ci.toString()
					+ " not stored");
		}
		ComponentInformation info = components.get(ci);
		componentContainer.setSelectedComponent(info.getComponent(), info
				.getPlacement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#getSelectedComponent()
	 */
	@Override
	public JComponent getSelectedComponent() {
		return componentContainer.getSelectedComponent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#getSelectedComponent(hr.fer.zemris.vhdllab.applets.main.ComponentGroup)
	 */
	@Override
	public JComponent getSelectedComponent(ComponentGroup group) {
		if (group == null) {
			throw new NullPointerException("Component group cant be null.");
		}
		return componentContainer.getSelectedComponent(group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#contains(java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentGroup)
	 */
	@Override
	public boolean contains(String identifier, ComponentGroup group) {
		/*
		 * Component identifier takes care of checking if identifier or group is
		 * null.
		 */
		ComponentIdentifier ci = new ComponentIdentifier(identifier, group);
		return contains(ci);
	}

	/**
	 * This is an actual implementation of
	 * {@link #contains(String, ComponentGroup)} method.
	 * 
	 * @param ci
	 *            a component identifier
	 * @return <code>true</code> if this storage contains specified component;
	 *         <code>false</code> otherwise
	 */
	private boolean contains(ComponentIdentifier ci) {
		return components.containsKey(ci);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#setTitle(java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentGroup, java.lang.String)
	 */
	@Override
	public void setTitle(String identifier, ComponentGroup group, String title) {
		if (title == null) {
			throw new NullPointerException("Title cant be null.");
		}
		/*
		 * Component identifier takes care of checking if identifier or group is
		 * null.
		 */
		ComponentIdentifier ci = new ComponentIdentifier(identifier, group);
		setTitle(ci, title);
	}

	/**
	 * This is an actual implementation of
	 * {@link #setTitle(String, ComponentGroup, String)} method.
	 * 
	 * @param ci
	 *            a component identifier
	 * @param title
	 *            a title to set
	 * @throws IllegalArgumentException
	 *             is specified component is not stored
	 */
	private void setTitle(ComponentIdentifier ci, String title) {
		if (!contains(ci)) {
			throw new IllegalArgumentException("Component " + ci.toString()
					+ " not stored");
		}
		ComponentInformation info = components.get(ci);
		componentContainer.setComponentTitle(info.getComponent(), info
				.getPlacement(), title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#getTitleFor(java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentGroup)
	 */
	@Override
	public String getTitleFor(String identifier, ComponentGroup group) {
		/*
		 * Component identifier takes care of checking if identifier or group is
		 * null.
		 */
		ComponentIdentifier ci = new ComponentIdentifier(identifier, group);
		return getTitleFor(ci);
	}

	/**
	 * This is an actual implementation of
	 * {@link #getTitleFor(String, ComponentGroup)} method.
	 * 
	 * @param ci
	 *            a component identifier
	 * @return a title
	 * @throws IllegalArgumentException
	 *             is specified component is not stored
	 */
	private String getTitleFor(ComponentIdentifier ci) {
		if (!contains(ci)) {
			throw new IllegalArgumentException("Component " + ci.toString()
					+ " not stored");
		}
		ComponentInformation info = components.get(ci);
		return componentContainer.getComponentTitle(info.getComponent(), info
				.getPlacement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#setToolTipText(java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentGroup, java.lang.String)
	 */
	@Override
	public void setToolTipText(String identifier, ComponentGroup group,
			String tooltip) {
		if (tooltip == null) {
			throw new NullPointerException("Title cant be null.");
		}
		/*
		 * Component identifier takes care of checking if identifier or group is
		 * null.
		 */
		ComponentIdentifier ci = new ComponentIdentifier(identifier, group);
		setToolTipText(ci, tooltip);
	}

	/**
	 * This is an actual implementation of
	 * {@link #setToolTipText(String, ComponentGroup, String)} method.
	 * 
	 * @param ci
	 *            a component identifier
	 * @param tooltip
	 *            a title to set
	 * @throws IllegalArgumentException
	 *             is specified component is not stored
	 */
	private void setToolTipText(ComponentIdentifier ci, String tooltip) {
		if (!contains(ci)) {
			throw new IllegalArgumentException("Component " + ci.toString()
					+ " not stored");
		}
		ComponentInformation info = components.get(ci);
		componentContainer.setComponentToolTipText(info.getComponent(), info
				.getPlacement(), tooltip);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#getComponentCount()
	 */
	@Override
	public int getComponentCount() {
		return components.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#getComponentCountFor(hr.fer.zemris.vhdllab.applets.main.ComponentGroup)
	 */
	@Override
	public int getComponentCountFor(ComponentGroup group) {
		if (group == null) {
			throw new NullPointerException("Component group cant be null.");
		}
		Collection<JComponent> groupedComponents = groups.get(group);
		if (groupedComponents == null) {
			return 0;
		} else {
			return groupedComponents.size();
		}
	}

	/**
	 * A private class used to identify components.
	 * 
	 * @author Miro Bezjak
	 */
	private static class ComponentIdentifier {

		private String identifier;
		private ComponentGroup group;

		/**
		 * Constructs a new component identifier out of <code>identifier</code>
		 * and <code>group</code>.
		 * 
		 * @param identifier
		 *            an identifier of a component in specified
		 *            <code>group</code>
		 * @param group
		 *            a component group
		 * @throws NullPointerException
		 *             if <code>identifier</code> or <code>group</code> is
		 *             <code>null</code>
		 */
		public ComponentIdentifier(String identifier, ComponentGroup group) {
			if (identifier == null) {
				throw new NullPointerException(
						"Component identifier cant be null.");
			}
			if (group == null) {
				throw new NullPointerException("Component group cant be null.");
			}
			this.identifier = identifier;
			this.group = group;
		}

		/**
		 * Getter for component identifier.
		 * 
		 * @return a component identifier
		 */
		public String getIdentifier() {
			return identifier;
		}

		/**
		 * Getter for component group.
		 * 
		 * @return a component group
		 */
		public ComponentGroup getGroup() {
			return group;
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
			result = prime * result + identifier.hashCode();
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final ComponentIdentifier other = (ComponentIdentifier) obj;
			if (!group.equals(other.group))
				return false;
			if (!identifier.equals(other.identifier))
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
			return identifier + "/" + group.name();
		}

	}

	/**
	 * Contains all neccessary information regarding components.
	 * 
	 * @author Miro Bezjak
	 */
	private static class ComponentInformation {

		/**
		 * A component in question.
		 */
		private JComponent component;
		/**
		 * A location where component is added.
		 */
		private ComponentPlacement placement;

		/**
		 * Constructs all information regarding <code>component</code>.
		 * 
		 * @param component
		 *            a component in question
		 * @param placement
		 *            a location where component is added
		 */
		public ComponentInformation(JComponent component,
				ComponentPlacement placement) {
			this.component = component;
			this.placement = placement;
		}

		/**
		 * Getter for a component.
		 * 
		 * @return a component
		 */
		public JComponent getComponent() {
			return component;
		}

		/**
		 * Getter for a component placement.
		 * 
		 * @return a component placement
		 */
		public ComponentPlacement getPlacement() {
			return placement;
		}

	}

}
