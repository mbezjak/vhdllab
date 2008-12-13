/**
 * 
 */
package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.event.EditorListener;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.entities.Caseless;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

/**
 * An abstract implementation of {@link IEditor} interface. This class greatly
 * eases creation of new editors by implementing many common methods.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 12/9/2007
 */
public abstract class AbstractEditor extends JPanel implements IEditor {

	protected ISystemContainer container;
	protected IResourceManager resourceManager;
	protected FileContent content;
	private boolean savable;
	private boolean readonly;
	private boolean modified;
	private EventListenerList listeners;

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#setSystemContainer(hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer)
	 */
	@Override
	public void setSystemContainer(ISystemContainer container) {
		this.container = container;
		this.resourceManager = container.getResourceManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#addEditorListener(hr.fer.zemris.vhdllab.applets.main.event.EditorListener)
	 */
	@Override
	public void addEditorListener(EditorListener l) {
		if (l == null) {
			throw new NullPointerException("Editor listener cant be null");
		}
		listeners.add(EditorListener.class, l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#getEditorListeners()
	 */
	@Override
	public EditorListener[] getEditorListeners() {
		return listeners.getListeners(EditorListener.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#removeAllEditorListeners()
	 */
	@Override
	public void removeAllEditorListeners() {
		for (EditorListener l : getEditorListeners()) {
			removeEditorListener(l);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#removeEditorListener(hr.fer.zemris.vhdllab.applets.main.event.EditorListener)
	 */
	@Override
	public void removeEditorListener(EditorListener l) {
		if (l == null) {
			throw new NullPointerException("Editor listener cant be null");
		}
		listeners.remove(EditorListener.class, l);
	}

	/**
	 * Fires a modified event in all editor listeners.
	 * 
	 * @param flag
	 *            <code>true</code> if editor has been modified or
	 *            <code>false</code> otherwise (i.e. an editor has just been
	 *            saved)
	 */
	private void fireModified(boolean flag) {
		for (EditorListener l : getEditorListeners()) {
			l.modified(getProjectName(), getFileName(), flag);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#setModified(boolean)
	 */
	@Override
	public boolean setModified(boolean flag) {
		if (flag != modified) {
			modified = flag;
			fireModified(flag);
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#isModified()
	 */
	@Override
	public boolean isModified() {
		return modified;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#undo()
	 */
	@Override
	public void undo() {
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#redo()
	 */
	@Override
	public void redo() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#setSavable(boolean)
	 */
	@Override
	public void setSavable(boolean flag) {
		savable = flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#isSavable()
	 */
	@Override
	public boolean isSavable() {
		return savable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean flag) {
		readonly = flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return readonly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#setFileContent(hr.fer.zemris.vhdllab.applets.main.model.FileContent)
	 */
	@Override
	public void setFileContent(final FileContent content) {
		this.content = content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#getFileName()
	 */
	@Override
	public Caseless getFileName() {
		return content.getFileName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#getProjectName()
	 */
	@Override
	public Caseless getProjectName() {
		return content.getProjectName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#highlightLine(int)
	 */
	@Override
	public void highlightLine(int line) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#init()
	 */
	@Override
	public void init() {
		listeners = new EventListenerList();
		modified = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor#dispose()
	 */
	@Override
	public void dispose() {
		removeAllEditorListeners();
	}
	
   @Override
    public Component getComponent() {
        return this;
    }

}
