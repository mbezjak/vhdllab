package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage;

/**
 * This is a default implementation of {@link IEditorStorage}.
 * 
 * @author Miro Bezjak
 */
public class DefaultEditorStorage {

	/**
	 * An extended component storage.
	 */
	private IComponentStorage storage;
	/**
	 * An editor group.
	 */
	private ComponentGroup group;

	/**
	 * Constructs an editor storage as an extension to {@link IComponentStorage}.
	 * 
	 * @param storage
	 *            a component storage to extend
	 * @throws NullPointerException
	 *             is <code>storage</code> is <code>null</code>
	 */
	public DefaultEditorStorage(IComponentStorage storage) {
		if (storage == null) {
			throw new NullPointerException("Component storage cant be null.");
		}
		this.group = ComponentGroup.EDITOR;
		this.storage = storage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#move(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentPlacement)
	 */
	public void move(IEditor editor, ComponentPlacement placement) {
//		move(createEditorIdentifierFor(editor), placement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#move(java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentPlacement)
	 */
	public void move(String identifier, ComponentPlacement placement) {
//		storage.moveComponent(identifier, placement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#setSelectedEditor(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	public void setSelectedEditor(IEditor editor) {
//		setSelectedEditor(createEditorIdentifierFor(editor));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#setSelectedEditor(java.lang.String)
	 */
	public void setSelectedEditor(String identifier) {
//		storage.setSelectedComponent(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#getSelectedEditor()
	 */
	public IEditor getSelectedEditor() {
//		return (IEditor) storage.getSelectedComponent(group);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#setTitle(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor,
	 *      java.lang.String)
	 */
	public void setTitle(IEditor editor, String title) {
//		setTitle(createEditorIdentifierFor(editor), title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#setTitle(java.lang.String,
	 *      java.lang.String)
	 */
	public void setTitle(String identifier, String title) {
//		storage.setTitle(identifier, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#setToolTipText(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor,
	 *      java.lang.String)
	 */
	public void setToolTipText(IEditor editor, String tooltip) {
//		setToolTipText(createEditorIdentifierFor(editor), tooltip);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#setToolTipText(java.lang.String,
	 *      java.lang.String)
	 */
	public void setToolTipText(String identifier, String tooltip) {
//		storage.setToolTipText(identifier, tooltip);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#getEditorCount()
	 */
	public int getEditorCount() {
		return storage.getComponentCountFor(group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#isEmpty()
	 */
	public boolean isEmpty() {
		return getEditorCount() == 0;
	}

}
