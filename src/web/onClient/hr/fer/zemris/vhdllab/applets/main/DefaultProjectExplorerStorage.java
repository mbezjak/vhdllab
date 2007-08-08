package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorerStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;

import javax.swing.JComponent;

/**
 * This is a default implementation of {@link IProjectExplorerStorage}.
 * 
 * @author Miro Bezjak
 */
public class DefaultProjectExplorerStorage implements IProjectExplorerStorage {

	/**
	 * An extended component storage.
	 */
	private IComponentStorage storage;
	/**
	 * A project explorer group.
	 */
	private ComponentGroup group;
	/**
	 * A project explorer identifier.
	 */
	private String identifier;

	/**
	 * Constructs a view storage as an extension to {@link IComponentStorage}.
	 * 
	 * @param storage
	 *            a component storage to extend
	 * @throws NullPointerException
	 *             is <code>storage</code> is <code>null</code>
	 */
	public DefaultProjectExplorerStorage(IComponentStorage storage) {
		if (storage == null) {
			throw new NullPointerException("Component storage cant be null.");
		}
		this.group = ComponentGroup.PROJECT_EXPLORER;
		this.identifier = group.name();
		this.storage = storage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorerStorage#add(java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorer)
	 */
	@Override
	public boolean add(String title, IProjectExplorer projectExplorer) {
		return storage.add(identifier, group, title,
				getComponentFor(projectExplorer));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorerStorage#close()
	 */
	@Override
	public IView close() {
		return (IView) storage.remove(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorerStorage#move(hr.fer.zemris.vhdllab.applets.main.ComponentPlacement)
	 */
	@Override
	public void move(ComponentPlacement placement) {
		storage.moveComponent(identifier, placement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorerStorage#getProjectExplorer()
	 */
	@Override
	public IProjectExplorer getProjectExplorer() {
		return (IProjectExplorer) storage.getComponent(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorerStorage#isProjectExplorerOpened()
	 */
	@Override
	public boolean isProjectExplorerOpened() {
		return storage.contains(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorerStorage#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		storage.setTitle(identifier, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorerStorage#setToolTipText(java.lang.String)
	 */
	@Override
	public void setToolTipText(String tooltip) {
		storage.setToolTipText(identifier, tooltip);
	}

	/**
	 * Converts a project explorer to a component.
	 * 
	 * @param projectExplorer
	 *            a project explorer to convert
	 * @return project explorer as a component
	 */
	private JComponent getComponentFor(IProjectExplorer projectExplorer) {
		return (JComponent) projectExplorer;
	}

}
