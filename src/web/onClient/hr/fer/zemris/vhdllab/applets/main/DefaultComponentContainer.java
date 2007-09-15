/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentProvider;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

/**
 * @author Miro Bezjak
 * 
 */
public class DefaultComponentContainer implements IComponentContainer {

	private IComponentProvider provider;
	private Map<JComponent, ComponentInformation> components;
	private Map<ComponentGroup, JComponent> selectedComponentsByGroup;
	private JComponent selectedComponent;

	public DefaultComponentContainer(IComponentProvider provider) {
		if (provider == null) {
			throw new NullPointerException("Provider cant be null");
		}
		this.provider = provider;
		components = new HashMap<JComponent, ComponentInformation>();
		selectedComponentsByGroup = new HashMap<ComponentGroup, JComponent>();
		selectedComponent = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#addComponent(java.lang.String,
	 *      javax.swing.JComponent,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentGroup,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentPlacement)
	 */
	@Override
	public void addComponent(String title, JComponent component,
			ComponentGroup group, ComponentPlacement placement) {
		components.put(component, new ComponentInformation(group, placement));
		JTabbedPane pane = getTabbedPane(placement);
		pane.add(title, component);
		activate(component);
		addListenersFor(component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#containsComponent(javax.swing.JComponent)
	 */
	@Override
	public boolean containsComponent(JComponent component) {
		return components.containsKey(component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#getComponentGroup(javax.swing.JComponent)
	 */
	@Override
	public ComponentGroup getComponentGroup(JComponent component) {
		if (!containsComponent(component)) {
			return null;
		}
		return components.get(component).getGroup();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#getComponentPlacement(javax.swing.JComponent)
	 */
	@Override
	public ComponentPlacement getComponentPlacement(JComponent component) {
		if (!containsComponent(component)) {
			return null;
		}
		return components.get(component).getPlacement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#getComponentTitle(javax.swing.JComponent)
	 */
	@Override
	public String getComponentTitle(JComponent component) {
		if (!containsComponent(component)) {
			return null;
		}
		JTabbedPane pane = getTabbedPane(component);
		int index = pane.indexOfComponent(component);
		return pane.getTitleAt(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#getSelectedComponent()
	 */
	@Override
	public JComponent getSelectedComponent() {
		return selectedComponent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#getSelectedComponent(hr.fer.zemris.vhdllab.applets.main.ComponentGroup)
	 */
	@Override
	public JComponent getSelectedComponent(ComponentGroup group) {
		return selectedComponentsByGroup.get(group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#removeComponent(javax.swing.JComponent)
	 */
	@Override
	public void removeComponent(JComponent component) {
		if (!containsComponent(component)) {
			return;
		}
		deactivate(component, false);
		JTabbedPane pane = getTabbedPane(component);
		components.remove(component);
		pane.remove(component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#setComponentTitle(javax.swing.JComponent,
	 *      java.lang.String)
	 */
	@Override
	public void setComponentTitle(JComponent component, String title) {
		if (!containsComponent(component)) {
			return;
		}
		JTabbedPane pane = getTabbedPane(component);
		int index = pane.indexOfComponent(component);
		pane.setTitleAt(index, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#setComponentToolTipText(javax.swing.JComponent,
	 *      java.lang.String)
	 */
	@Override
	public void setComponentToolTipText(JComponent component, String tooltip) {
		if (!containsComponent(component)) {
			return;
		}
		JTabbedPane pane = getTabbedPane(component);
		int index = pane.indexOfComponent(component);
		pane.setToolTipTextAt(index, tooltip);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentContainer#setSelectedComponent(javax.swing.JComponent)
	 */
	@Override
	public void setSelectedComponent(JComponent component) {
		if (!containsComponent(component)) {
			return;
		}
		activate(component);
	}

	/**
	 * Sets an icon for a specified component.
	 * 
	 * @param component
	 *            a component to set icon to
	 */
	private void setIcon(JComponent component) {
		ComponentInformation info = components.get(component);
		JTabbedPane pane = getTabbedPane(info.getPlacement());
		Icon componentIcon = ComponentIcon.getNormalIcon(info.getGroup());
		int index = pane.indexOfComponent(component);
		pane.setIconAt(index, componentIcon);
	}

	/**
	 * Sets an active icon for a specified component.
	 * 
	 * @param component
	 *            a component to set icon to
	 */
	private void setActiveIcon(JComponent component) {
		ComponentInformation info = components.get(component);
		JTabbedPane pane = getTabbedPane(info.getPlacement());
		Icon componentIcon = ComponentIcon.getActiveIcon(info.getGroup());
		int index = pane.indexOfComponent(component);
		pane.setIconAt(index, componentIcon);
	}

	private void addListenersFor(JComponent component) {
		addListenersFor(component, component);
	}

	private void addListenersFor(Container container,
			JComponent originalComponent) {
		if (container == null) {
			return;
		}
		for (Component c : container.getComponents()) {
			if (c instanceof JTabbedPane) {
				/*
				 * This is a workaround for a bug in scrollable layout tabbed
				 * pane. It seams that when a mouse listener is added to some of
				 * its components it stops responding.
				 */
				JTabbedPane pane = (JTabbedPane) c;
				for (int i = 0; i < pane.getTabCount(); i++) {
					addListenersFor((JComponent) pane.getComponentAt(i),
							originalComponent);
				}
			} else {
				if (c instanceof Container) {
					addListenersFor((Container) c, originalComponent);
				}
			}
			// c.addFocusListener(new MyFocus(originalComponent));
			c.addMouseListener(new MyMouse(originalComponent));
		}
	}

	private void activate(JComponent component) {
		if (component == null) {
			return;
		}
		if (selectedComponent == null || !selectedComponent.equals(component)) {
			selectedComponent = component;
		}
		ComponentGroup group = components.get(component).getGroup();
		JComponent previousComponent = selectedComponentsByGroup.get(group);
		if (previousComponent != null && previousComponent.equals(component)) {
			return;
		}
		deactivate(previousComponent);
		selectedComponentsByGroup.put(group, component);
		selectedComponent = component;
		setActiveIcon(component);
		JTabbedPane pane = getTabbedPane(component);
		pane.setSelectedComponent(component);
	}

	private void deactivate(JComponent component) {
		deactivate(component, true);
	}

	private void deactivate(JComponent component, boolean setIcon) {
		if (component == null) {
			return;
		}
		ComponentInformation info = components.get(component);
		selectedComponentsByGroup.remove(info.getGroup());
		if (selectedComponent != null && selectedComponent.equals(component)) {
			selectedComponent = null;
		}
		if (setIcon) {
			setIcon(component);
		}
	}

	private class MyMouse extends MouseAdapter {
		private JComponent c;

		public MyMouse(JComponent c) {
			this.c = c;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			activate(c);
		}
	}

	class MyFocus extends FocusAdapter {
		private JComponent c;

		public MyFocus(JComponent c) {
			this.c = c;
		}

		@Override
		public void focusGained(FocusEvent e) {
			activate(c);
		}

	}

	private JTabbedPane getTabbedPane(JComponent component) {
		ComponentPlacement placement = components.get(component).getPlacement();
		return getTabbedPane(placement);
	}

	private JTabbedPane getTabbedPane(ComponentPlacement placement) {
		return provider.getTabbedPane(placement);
	}

	/**
	 * Stores a component group and placement.
	 * 
	 * @author Miro Bezjak
	 */
	private static class ComponentInformation {
		private ComponentGroup group;
		private ComponentPlacement placement;

		/**
		 * Constructs a component information using specified parameters.
		 * 
		 * @param group
		 *            a component group
		 * @param placement
		 *            a component placement
		 */
		public ComponentInformation(ComponentGroup group,
				ComponentPlacement placement) {
			super();
			this.group = group;
			this.placement = placement;
		}

		/**
		 * A component group.
		 * 
		 * @return a component group
		 */
		public ComponentGroup getGroup() {
			return group;
		}

		/**
		 * A component placement.
		 * 
		 * @return a component placement
		 */
		public ComponentPlacement getPlacement() {
			return placement;
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
			result = prime * result + ((group == null) ? 0 : group.hashCode());
			result = prime * result
					+ ((placement == null) ? 0 : placement.hashCode());
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
			final ComponentInformation other = (ComponentInformation) obj;
			if (group == null) {
				if (other.group != null)
					return false;
			} else if (!group.equals(other.group))
				return false;
			if (placement == null) {
				if (other.placement != null)
					return false;
			} else if (!placement.equals(other.placement))
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
			return "group: " + group + " / placement: " + placement;
		}
	}

}
