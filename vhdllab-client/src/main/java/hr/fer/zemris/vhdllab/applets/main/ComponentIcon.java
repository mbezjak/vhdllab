package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.utilities.FileUtil;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.digester.Digester;

/**
 * Provides icons for any component.
 * 
 * @author Miro Bezjak
 */
public class ComponentIcon {

	private static final Map<ComponentGroup, Icons> icons;

	static {
		icons = new HashMap<ComponentGroup, Icons>();
		InputStream is = ComponentIcon.class
				.getResourceAsStream("componentIcons.xml");
		String data = FileUtil.readFile(is);
		ComponentIcons ci = ComponentIconParser.getConfiguration(data);
		for (ComponentIconItem i : ci.getIcons()) {
			ComponentGroup group = ComponentGroup.valueOf(i.getGroup());
			Icon normal = loadIcon(i.getNormal());
			Icon active = loadIcon(i.getActive());
			icons.put(group, new Icons(normal, active));
		}
	}

	/**
	 * Loads an icon and returns it.
	 * 
	 * @param path
	 *            a path to an icon
	 * @return an icon
	 */
	private static Icon loadIcon(String path) {
		URL url = ComponentIcon.class.getResource(path);
		return new ImageIcon(url);
	}

	/**
	 * Returns a normal (presented to a user at all times) icon for a specified
	 * component group or <code>null</code> if no such icon can be found.
	 * 
	 * @param group
	 *            a component group
	 * @return a normal icon for a specified component group
	 * @throws NullPointerException
	 *             if <code>group</code> is <code>null</code>
	 */
	public static Icon getNormalIcon(ComponentGroup group) {
		if (group == null) {
			throw new NullPointerException("Component group cant be null");
		}
		Icons i = icons.get(group);
		if (i == null) {
			return null;
		}
		return i.getNormal();
	}

	/**
	 * Returns an active (presented to a user when a component is active - has
	 * focus) icon for a specified component group or <code>null</code> if no
	 * such icon can be found.
	 * 
	 * @param group
	 *            a component group
	 * @return an active icon for a specified component group
	 * @throws NullPointerException
	 *             if <code>group</code> is <code>null</code>
	 */
	public static Icon getActiveIcon(ComponentGroup group) {
		if (group == null) {
			throw new NullPointerException("Component group cant be null");
		}
		Icons i = icons.get(group);
		if (i == null) {
			return null;
		}
		return i.getActive();
	}

	/**
	 * Parses a component icons configurations file.
	 * 
	 * @author Miro Bezjak
	 */
	public static class ComponentIconParser {
		/**
		 * Returns a component icons.
		 * 
		 * @param data
		 *            a data that contains xml configuration.
		 * @return a component icons
		 */
		public static ComponentIcons getConfiguration(String data) {
			Digester digester = new Digester();
			digester.addObjectCreate("components", ComponentIcons.class);
			digester.addObjectCreate("components/component",
					ComponentIconItem.class);
			digester.addSetNestedProperties("components/component");
			digester.addSetNext("components/component", "addIcon");

			try {
				return (ComponentIcons) digester.parse(new StringReader(data));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * Contains a collection of icons.
	 * 
	 * @author Miro Bezjak
	 */
	public static class ComponentIcons {
		private Collection<ComponentIconItem> icons;

		/**
		 * Default constructor.
		 */
		public ComponentIcons() {
			icons = new LinkedList<ComponentIconItem>();
		}

		/**
		 * Adds a component icon.
		 * 
		 * @param item
		 *            an icon to add
		 */
		public void addIcon(ComponentIconItem item) {
			icons.add(item);
		}

		/**
		 * Getter for icons.
		 * 
		 * @return icons
		 */
		public Collection<ComponentIconItem> getIcons() {
			return icons;
		}
	}

	/**
	 * Contains a normal and active icon for a component. A normal icon is one
	 * presented at all times while an active is one when a component in certain
	 * component group is active (he or his child has a focus). It also contains
	 * a component group.
	 * 
	 * @author Miro Bezjak
	 */
	public static class ComponentIconItem {

		/**
		 * A component group.
		 */
		private String group;
		/**
		 * A normal icon.
		 */
		private String normal;
		/**
		 * An active icon.
		 */
		private String active;

		/**
		 * Default constructor.
		 */
		public ComponentIconItem() {
		}

		/**
		 * Getter for a component group.
		 * 
		 * @return a component group
		 */
		public String getGroup() {
			return group;
		}

		/**
		 * Setter for a component group.
		 * 
		 * @param group
		 *            a component group
		 */
		public void setGroup(String group) {
			this.group = group;
		}

		/**
		 * Getter for a normal icon.
		 * 
		 * @return a normal icon
		 */
		public String getNormal() {
			return normal;
		}

		/**
		 * Setter for a normal icon.
		 * 
		 * @param normal
		 *            a normal icon
		 */
		public void setNormal(String normal) {
			this.normal = normal;
		}

		/**
		 * Getter for an active icon.
		 * 
		 * @return an active icon
		 */
		public String getActive() {
			return active;
		}

		/**
		 * Setter for an active icon.
		 * 
		 * @param active
		 *            an active icon
		 */
		public void setActive(String active) {
			this.active = active;
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
			result = prime * result
					+ ((active == null) ? 0 : active.hashCode());
			result = prime * result + ((group == null) ? 0 : group.hashCode());
			result = prime * result
					+ ((normal == null) ? 0 : normal.hashCode());
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
			final ComponentIconItem other = (ComponentIconItem) obj;
			if (active == null) {
				if (other.active != null)
					return false;
			} else if (!active.equals(other.active))
				return false;
			if (group == null) {
				if (other.group != null)
					return false;
			} else if (!group.equals(other.group))
				return false;
			if (normal == null) {
				if (other.normal != null)
					return false;
			} else if (!normal.equals(other.normal))
				return false;
			return true;
		}

	}

	/**
	 * Contains a normal and active icon for a component. A normal icon is one
	 * presented at all times while an active is one when a component in certain
	 * component group is active (he or his child has a focus).
	 * <p>
	 * This class is immutable and therefor thread-safe.
	 * 
	 * @author Miro Bezjak
	 */
	public static class Icons {

		/**
		 * A normal icon.
		 */
		private Icon normal;
		/**
		 * An active icon.
		 */
		private Icon active;

		/**
		 * Default constructor.
		 * 
		 * @param normal
		 *            a normal icon
		 * @param active
		 *            an active icon
		 */
		public Icons(Icon normal, Icon active) {
			super();
			this.normal = normal;
			this.active = active;
		}

		/**
		 * Getter for a normal icon.
		 * 
		 * @return a normal icon
		 */
		public Icon getNormal() {
			return normal;
		}

		/**
		 * Getter for an active icon.
		 * 
		 * @return an active icon
		 */
		public Icon getActive() {
			return active;
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
			result = prime * result
					+ ((active == null) ? 0 : active.hashCode());
			result = prime * result
					+ ((normal == null) ? 0 : normal.hashCode());
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
			final Icons other = (Icons) obj;
			if (active == null) {
				if (other.active != null)
					return false;
			} else if (!active.equals(other.active))
				return false;
			if (normal == null) {
				if (other.normal != null)
					return false;
			} else if (!normal.equals(other.normal))
				return false;
			return true;
		}

	}

}
