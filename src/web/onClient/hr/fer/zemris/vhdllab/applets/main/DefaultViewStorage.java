package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;

/**
 * This is a default implementation of {@link IViewStorage}.
 * 
 * @author Miro Bezjak
 */
public class DefaultViewStorage implements IViewStorage {

	/**
	 * An extended component storage.
	 */
	private IComponentStorage storage;
	/**
	 * A view group.
	 */
	private ComponentGroup group;

	/**
	 * Constructs a view storage as an extension to {@link IComponentStorage}.
	 * 
	 * @param storage
	 *            a component storage to extend
	 * @throws NullPointerException
	 *             is <code>storage</code> is <code>null</code>
	 */
	public DefaultViewStorage(IComponentStorage storage) {
		if (storage == null) {
			throw new NullPointerException("Component storage cant be null.");
		}
		this.group = ComponentGroup.VIEW;
		this.storage = storage;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage#add(java.lang.String, java.lang.String, hr.fer.zemris.vhdllab.applets.main.interfaces.IView)
	 */
	@Override
	public boolean add(String identifier, String title, IView view) {
		return storage.add(identifier, group, title, getComponentFor(view));
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage#close(java.lang.String)
	 */
	@Override
	public IView close(String identifier) {
		return (IView) storage.remove(identifier, group);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage#close(hr.fer.zemris.vhdllab.applets.main.interfaces.IView)
	 */
	@Override
	public void close(IView view) {
		storage.remove(getComponentFor(view));
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage#move(java.lang.String, hr.fer.zemris.vhdllab.applets.main.ComponentPlacement)
	 */
	@Override
	public void move(String identifier, ComponentPlacement placement) {
		storage.moveComponent(identifier, group, placement);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage#getIdentifierFor(hr.fer.zemris.vhdllab.applets.main.interfaces.IView)
	 */
	@Override
	public String getIdentifierFor(IView view) {
		if(view == null) {
			throw new NullPointerException("View cant be null");
		}
		return storage.getIdentifierFor(getComponentFor(view));
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage#getOpenedView(java.lang.String)
	 */
	@Override
	public IView getOpenedView(String identifier) {
		return (IView) storage.getComponent(identifier, group);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage#getAllOpenedViews()
	 */
	@Override
	public List<IView> getAllOpenedViews() {
		Collection<JComponent> components = storage.getComponents(group);
		List<IView> openedViews = new ArrayList<IView>(components.size());
		for (JComponent c : components) {
			openedViews.add((IView) c);
		}
		return openedViews;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage#setSelectedView(java.lang.String)
	 */
	@Override
	public void setSelectedView(String identifier) {
		storage.setSelectedComponent(identifier, group);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage#getSelectedView()
	 */
	@Override
	public IView getSelectedView() {
		return (IView) storage.getSelectedComponent(group);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage#isViewOpened(java.lang.String)
	 */
	@Override
	public boolean isViewOpened(String identifier) {
		return storage.contains(identifier, group);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage#setTitle(java.lang.String, java.lang.String)
	 */
	@Override
	public void setTitle(String identifier, String title) {
		storage.setTitle(identifier, group, title);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage#setToolTipText(java.lang.String, java.lang.String)
	 */
	@Override
	public void setToolTipText(String identifier, String tooltip) {
		storage.setToolTipText(identifier, group, tooltip);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage#getViewCount()
	 */
	@Override
	public int getViewCount() {
		return storage.getComponentCountFor(group);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return getViewCount() == 0;
	}

	/**
	 * Converts a view to a component.
	 * 
	 * @param view
	 *            a view to convert
	 * @return view as a component
	 */
	private JComponent getComponentFor(IView view) {
		return (JComponent) view;
	}

}
