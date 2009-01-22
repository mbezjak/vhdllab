package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;
import hr.fer.zemris.vhdllab.platform.listener.StandaloneEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorListener;

import javax.swing.JPanel;

public abstract class AbstractEditor extends JPanel implements Editor {

    private static final long serialVersionUID = 1L;

    private final EventPublisher<EditorListener> publisher;
    private boolean modified;
    private FileInfo file;

    // ONLY TEMPORARY!!!!
    // --------------------------
    protected ISystemContainer systemContainer;

    public void setSystemContainer(ISystemContainer systemContainer) {
        this.systemContainer = systemContainer;
    }

    // --------------------------

    public AbstractEditor() {
        this.publisher = new StandaloneEventPublisher<EditorListener>(
                EditorListener.class);
        modified = false;
    }

    @Override
    public void init() {
        doInitWithoutData();
    }

    @Override
    public void dispose() {
        publisher.removeListeners();
        doDispose();
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void undo() {
    }

    @Override
    public void redo() {
    }

    @Override
    public void setFile(FileInfo file) {
        this.file = file;
        doInitWithData(this.file);
        setModified(false);
    }

    @Override
    public FileInfo getFile() {
        if (file != null) {
            file.setData(getData());
        }
        return file;
    }

    @Override
    public boolean setModified(boolean flag) {
        if (flag != modified) {
            modified = flag;
            fireModified(getFile(), flag);
            return true;
        }
        return false;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    /**
     * Fires a modified event in all editor listeners.
     * 
     * @param f
     *            a file containing modified data
     * @param flag
     *            <code>true</code> if editor has been modified or
     *            <code>false</code> otherwise (i.e. an editor has just been
     *            saved)
     */
    private void fireModified(FileInfo f, boolean flag) {
        for (EditorListener l : publisher.getListeners()) {
            l.modified(f, flag);
        }
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public void highlightLine(int line) {
    }

    @Override
    public EventPublisher<EditorListener> getEventPublisher() {
        return publisher;
    }

    protected abstract void doInitWithoutData();

    protected abstract void doInitWithData(FileInfo f);

    protected abstract void doDispose();

    protected abstract String getData();

}
