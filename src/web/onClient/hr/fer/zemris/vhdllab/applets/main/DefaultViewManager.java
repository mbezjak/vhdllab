package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.ComponentIdentifierFactory;
import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier;
import hr.fer.zemris.vhdllab.applets.main.conf.ComponentConfiguration;
import hr.fer.zemris.vhdllab.applets.main.conf.ViewProperties;
import hr.fer.zemris.vhdllab.applets.main.constant.LanguageConstants;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager;
import hr.fer.zemris.vhdllab.client.core.bundle.ResourceBundleProvider;
import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.client.core.prefs.UserPreferences;
import hr.fer.zemris.vhdllab.constants.UserFileConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JComponent;

/**
 * This is a default implementation of {@link IViewManager}.
 * 
 * @author Miro Bezjak
 */
public class DefaultViewManager implements IViewManager {

	/**
	 * A view storage.
	 */
	private IComponentStorage storage;

	/**
	 * A view configuration.
	 */
	private ComponentConfiguration conf;

	/**
	 * A system container for accessing more information.
	 */
	private ISystemContainer container;

	/**
	 * A resource bundle.
	 */
	private ResourceBundle bundle;

	/**
	 * A component group.
	 */
	private ComponentGroup group;

	/**
	 * Constructs a default view manager out of specified view storage and
	 * configuration.
	 * 
	 * @param storage
	 *            a view storage
	 * @param conf
	 *            a view configuration
	 * @param container
	 *            a system container for accessing more information
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public DefaultViewManager(IComponentStorage storage,
			ComponentConfiguration conf, ISystemContainer container) {
		if (storage == null) {
			throw new NullPointerException("View storage cant be null");
		}
		if (conf == null) {
			throw new NullPointerException("View configuration cant be null");
		}
		if (container == null) {
			throw new NullPointerException("User preferences cant be null");
		}
		this.storage = storage;
		this.conf = conf;
		this.container = container;
		this.bundle = ResourceBundleProvider
				.getBundle(LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN);
		group = ComponentGroup.VIEW;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#openProjectExplorer()
	 */
	public void openProjectExplorer() {
		IComponentIdentifier<?> identifier = ComponentIdentifierFactory
				.createProjectExplorerIdentifier();
		openView(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#openView(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier)
	 */
	public IView openView(IComponentIdentifier<?> identifier) {
		if (identifier == null) {
			throw new NullPointerException("View identifier cant be null");
		}
		if (storage.contains(identifier)) {
			storage.setSelectedComponent(identifier);
			IView view = getOpenedView(identifier);
			return view;
		}

		// Initialization of a view
		IView view = getNewViewInstance(identifier);
		view.setSystemContainer(container);
		view.init();
		// End of initialization

		String type = identifier.getComponentType();
		String title = bundle.getString(LanguageConstants.TITLE_FOR + type);
		String tooltip = bundle.getString(LanguageConstants.TOOLTIP_FOR + type);
		ComponentPlacement placement = getPlacement(identifier);
		storage.add(identifier, group, title, (JComponent) view, placement);
		storage.setToolTipText(identifier, tooltip);
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#getOpenedView(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier)
	 */
	public IView getOpenedView(IComponentIdentifier<?> identifier) {
		if (identifier == null) {
			throw new NullPointerException("View identifier cant be null");
		}
		return (IView) storage.getComponent(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#closeView(hr.fer.zemris.vhdllab.applets.main.interfaces.IView)
	 */
	public void closeView(IView view) {
		if (view == null) {
			return;
		}
		List<IView> editorsToClose = new ArrayList<IView>(1);
		editorsToClose.add(view);
		closeViews(editorsToClose);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#closeAllViews()
	 */
	public void closeAllViews() {
		List<IView> openedEditors = getAllOpenedViews();
		closeViews(openedEditors);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#closeAllButThisView(hr.fer.zemris.vhdllab.applets.main.interfaces.IView)
	 */
	public void closeAllButThisView(IView viewToKeepOpened) {
		if (viewToKeepOpened == null)
			return;
		List<IView> openedViews = getAllOpenedViews();
		openedViews.remove(viewToKeepOpened);
		closeViews(openedViews);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#closeViews(java.util.List)
	 */
	public void closeViews(List<IView> viewsToClose) {
		if (viewsToClose == null)
			return;
		viewsToClose = pickOpenedViews(viewsToClose);
		for (IView view : viewsToClose) {
			// Clean up of a view
			view.dispose();
			// End of clean up

			storage.remove((JComponent) view);
		}
	}

	/**
	 * Returns only opened views from a list of specified views.
	 * 
	 * @param views
	 *            a list of specified views
	 * @return opened views from a list of specified views
	 */
	private List<IView> pickOpenedViews(List<IView> views) {
		List<IView> openedViews = new ArrayList<IView>();
		for (IView e : views) {
			if (isViewOpened(e)) {
				openedViews.add(e);
			}
		}
		return openedViews;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#isViewOpened(hr.fer.zemris.vhdllab.applets.main.interfaces.IView)
	 */
	public boolean isViewOpened(IView view) {
		if (view == null) {
			throw new NullPointerException("View cant be null");
		}
		IComponentIdentifier<?> identifier = getIdentifierFor(view);
		return isViewOpened(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#isViewOpened(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier)
	 */
	public boolean isViewOpened(IComponentIdentifier<?> identifier) {
		return storage.contains(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#findAllViewsAssociatedWith(java.lang.Object)
	 */
	public List<IView> findAllViewsAssociatedWith(Object instanceModifier) {
		if (instanceModifier == null) {
			throw new NullPointerException("Instance modifier cant be null");
		}
		List<IView> associatedViews = new ArrayList<IView>();
		for (IView v : getAllOpenedViews()) {
			IComponentIdentifier<?> id = getIdentifierFor(v);
			Object im = id.getInstanceModifier();
			if (im != null) {
				if (im.equals(instanceModifier)) {
					associatedViews.add(v);
				}
			}
		}
		return associatedViews;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#getAllOpenedViews()
	 */
	public List<IView> getAllOpenedViews() {
		Collection<JComponent> components = storage.getComponents(group);
		List<IView> openedViews = new ArrayList<IView>(components.size());
		for (JComponent c : components) {
			openedViews.add((IView) c);
		}
		return openedViews;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#getSelectedView()
	 */
	public IView getSelectedView() {
		return (IView) storage.getSelectedComponent(group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#getTitle(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier)
	 */
	public String getTitle(IComponentIdentifier<?> identifier) {
		return storage.getTitleFor(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#setTitle(hr.fer.zemris.vhdllab.applets.main.interfaces.IView,
	 *      java.lang.String)
	 */
	public void setTitle(IView view, String title) {
		if (view == null) {
			throw new NullPointerException("View cant be null");
		}
		IComponentIdentifier<?> id = getIdentifierFor(view);
		setTitle(id, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#setTitle(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier,
	 *      java.lang.String)
	 */
	public void setTitle(IComponentIdentifier<?> identifier, String title) {
		storage.setTitle(identifier, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager#getIdentifierFor(hr.fer.zemris.vhdllab.applets.main.interfaces.IView)
	 */
	public IComponentIdentifier<?> getIdentifierFor(IView view) {
		return storage.getIdentifierFor((JComponent) view);
	}

	/**
	 * Returns a component placement out of specified identifier.
	 * 
	 * @param identifier
	 *            an identifier for whom to determine placement
	 * @return a component placement
	 */
	private ComponentPlacement getPlacement(IComponentIdentifier<?> identifier) {
		String id = identifier.getComponentType();
		UserPreferences pref = UserPreferences.instance();
		String name = UserFileConstants.SYSTEM_COMPONENT_PLACEMENT_FOR;
		String property = pref.get(name + id, null);
		if (property == null) {
			name = UserFileConstants.SYSTEM_DEFAULT_VIEW_PLACEMENT;
			property = pref.get(name, null);
			if (property == null) {
				property = ComponentPlacement.BOTTOM.name();
			}
		}
		return ComponentPlacement.valueOf(property);
	}

	/**
	 * Returns a new instance of specified view.
	 * 
	 * @param identifier
	 *            an identifier of a view
	 * @return a new view instance
	 * @throws NullPointerException
	 *             if <code>id</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if can't find view for given <code>id</code>
	 * @throws IllegalStateException
	 *             if view can't be instantiated
	 */
	private IView getNewViewInstance(IComponentIdentifier<?> identifier) {
		if (identifier == null) {
			throw new NullPointerException("Identifier can not be null.");
		}
		ViewProperties vp = conf.getViewProperties(identifier);
		if (vp == null) {
			throw new IllegalArgumentException(
					"Can not find view for given identifier.");
		}
		IView view = null;
		try {
			view = (IView) Class.forName(vp.getClazz()).newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Cant instantiate view", e);
		}
		return view;
	}

	/**
	 * An alias method for
	 * {@link ISystemContainer#echoStatusText(String, MessageType)} method.
	 * 
	 * @param text
	 *            a text to echo
	 * @param type
	 *            a message type
	 */
	private void echoStatusText(String text, MessageType type) {
		SystemLog.instance().addSystemMessage(text, type);
	}

}
