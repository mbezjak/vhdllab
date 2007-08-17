package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage;

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
	private Map<IComponentIdentifier<?>, JComponent> components;
	/**
	 * This is a other directional map for <code>components</code> field.
	 */
	private Map<JComponent, IComponentIdentifier<?>> bidiComponents;
	/**
	 * A grouped components.
	 */
	private Map<ComponentGroup, Collection<JComponent>> groups;
	/**
	 * A container where all components are presented to a user.
	 */
	private IComponentContainer componentContainer;

	/**
	 * Constructs a component storage.
	 * 
	 * @param componentContainer
	 *            a container where all component are presented to a user
	 * @throws NullPointerException
	 *             if <code>componentContainer</code> is <code>null</code>
	 */
	public DefaultComponentStorage(IComponentContainer componentContainer) {
		if (componentContainer == null) {
			throw new NullPointerException("Component container cant be null.");
		}
		components = new HashMap<IComponentIdentifier<?>, JComponent>();
		bidiComponents = new HashMap<JComponent, IComponentIdentifier<?>>();
		groups = new HashMap<ComponentGroup, Collection<JComponent>>();
		this.componentContainer = componentContainer;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#add(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentGroup, java.lang.String,
	 *      javax.swing.JComponent,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentPlacement)
	 */
	@Override
	public boolean add(IComponentIdentifier<?> identifier, ComponentGroup group,
			String title, JComponent component, ComponentPlacement placement) {
		if (identifier == null) {
			throw new NullPointerException("Identifier cant be null.");
		}
		if (title == null) {
			throw new NullPointerException("Title cant be null.");
		}
		if (component == null) {
			throw new NullPointerException("Component cant be null.");
		}
		if (placement == null) {
			throw new NullPointerException("Component placement cant be null.");
		}
		if (contains(identifier)) {
			if (!placement.equals(componentContainer
					.getComponentPlacement(component))) {
				moveComponent(identifier, placement);
			}
			// must only be set after component has moved!
			setTitle(identifier, title);
			return false;
		} else {
			components.put(identifier, component);
			bidiComponents.put(component, identifier);
			Collection<JComponent> groupedComponents = groups.get(group);
			if (groupedComponents == null) {
				groupedComponents = new ArrayList<JComponent>();
			}
			groupedComponents.add(component);
			groups.put(group, groupedComponents);
			componentContainer.addComponent(title, component, group, placement);
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#remove(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier)
	 */
	@Override
	public JComponent remove(IComponentIdentifier<?> identifier) {
		if (identifier == null) {
			throw new NullPointerException("Identifier cant be null");
		}
		if (!contains(identifier)) {
			return null;
		}
		JComponent component = components.remove(identifier);
		bidiComponents.remove(component);
		ComponentGroup group = componentContainer.getComponentGroup(component);
		Collection<JComponent> groupedComponents = groups.get(group);
		groupedComponents.remove(component);
		if (groupedComponents.isEmpty()) {
			groups.remove(group);
		}
		componentContainer.removeComponent(component);
		return component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#remove(javax.swing.JComponent)
	 */
	@Override
	public void remove(JComponent component) {
		if (component == null) {
			throw new NullPointerException("Component cant be null");
		}
		IComponentIdentifier<?> identifier = bidiComponents.get(component);
		remove(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#moveComponent(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentPlacement)
	 */
	@Override
	public void moveComponent(IComponentIdentifier<?> identifier,
			ComponentPlacement placement) {
		if (identifier == null) {
			throw new NullPointerException("Identifier cant be null");
		}
		if (placement == null) {
			throw new NullPointerException("Component placement cant be null.");
		}
		if (!contains(identifier)) {
			throw new IllegalArgumentException("Component " + identifier
					+ " not stored");
		}
		String title = getTitleFor(identifier);
		JComponent component = getComponent(identifier);
		ComponentGroup group = componentContainer.getComponentGroup(component);
		remove(identifier);
		add(identifier, group, title, component, placement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#getComponentPlacement(javax.swing.JComponent)
	 */
	@Override
	public ComponentPlacement getComponentPlacement(JComponent component) {
		if (component == null) {
			throw new NullPointerException("Component cant be null");
		}
		return componentContainer.getComponentPlacement(component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#getIdentifierFor(javax.swing.JComponent)
	 */
	@Override
	public IComponentIdentifier<?> getIdentifierFor(JComponent component) {
		if (component == null) {
			throw new NullPointerException("Component cant be null");
		}
		if (!bidiComponents.containsKey(component)) {
			return null;
		}
		return bidiComponents.get(component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#getComponent(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier)
	 */
	@Override
	public JComponent getComponent(IComponentIdentifier<?> identifier) {
		if (identifier == null) {
			throw new NullPointerException("Identifier cant be null");
		}
		if (!contains(identifier)) {
			return null;
		}
		return components.get(identifier);
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#setSelectedComponent(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier)
	 */
	@Override
	public void setSelectedComponent(IComponentIdentifier<?> identifier) {
		if (identifier == null) {
			throw new NullPointerException("Identifier cant be null");
		}
		if (!contains(identifier)) {
			throw new IllegalArgumentException("Component " + identifier
					+ " not stored");
		}
		JComponent component = getComponent(identifier);
		componentContainer.setSelectedComponent(component);
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#contains(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier)
	 */
	@Override
	public boolean contains(IComponentIdentifier<?> identifier) {
		if (identifier == null) {
			throw new NullPointerException("Identifier cant be null");
		}
		return components.containsKey(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#setTitle(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier,
	 *      java.lang.String)
	 */
	@Override
	public void setTitle(IComponentIdentifier<?> identifier, String title) {
		if (identifier == null) {
			throw new NullPointerException("Identifier cant be null");
		}
		if (title == null) {
			throw new NullPointerException("Title cant be null.");
		}
		if (!contains(identifier)) {
			throw new IllegalArgumentException("Component " + identifier
					+ " not stored");
		}
		JComponent component = getComponent(identifier);
		componentContainer.setComponentTitle(component, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#getTitleFor(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier)
	 */
	@Override
	public String getTitleFor(IComponentIdentifier<?> identifier) {
		if (identifier == null) {
			throw new NullPointerException("Identifier cant be null");
		}
		if (!contains(identifier)) {
			throw new IllegalArgumentException("Component " + identifier
					+ " not stored");
		}
		JComponent component = getComponent(identifier);
		return componentContainer.getComponentTitle(component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage#setToolTipText(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier,
	 *      java.lang.String)
	 */
	@Override
	public void setToolTipText(IComponentIdentifier<?> identifier, String tooltip) {
		if (identifier == null) {
			throw new NullPointerException("Identifier cant be null");
		}
		if (!contains(identifier)) {
			throw new IllegalArgumentException("Component " + identifier
					+ " not stored");
		}
		JComponent component = getComponent(identifier);
		componentContainer.setComponentToolTipText(component, tooltip);
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

}
