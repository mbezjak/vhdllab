package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;
import hr.fer.zemris.vhdllab.platform.listener.StandaloneEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorListener;
import hr.fer.zemris.vhdllab.platform.manager.editor.PlatformContainer;

import javax.swing.JPanel;

public abstract class AbstractEditor extends JPanel implements Editor {

    private static final long serialVersionUID = 1L;

    private final EventPublisher<EditorListener> publisher;
    private boolean modified;
    private FileInfo file;
    protected PlatformContainer container;

    public AbstractEditor() {
        this.publisher = new StandaloneEventPublisher<EditorListener>(
                EditorListener.class);
        modified = false;
    }

    @Override
    public PlatformContainer getContainer() {
        return container;
    }

    @Override
    public void setContainer(PlatformContainer container) {
        this.container = container;
    }

    @Override
    public JPanel getPanel() {
        return this;
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
    public void undo() {
    }

    @Override
    public void redo() {
    }

    @Override
    public void setFile(FileInfo file) {
        this.file = (file != null ? new FileInfo(file, true) : null);
        doInitWithData(this.file);
        setModified(false);
    }

    @Override
    public FileInfo getFile() {
        String data = getData();
        if (file != null && data != null) {
            file.setData(data);
        }
        return file != null ? new FileInfo(file, true) : null;
    }

    @Override
    public Caseless getFileName() {
        return (file != null ? file.getName() : null);
    }

    @Override
    public Caseless getProjectName() {
        if (file != null) {
            ProjectInfo project = container.getMapper().getProject(
                    file.getProjectId());
            return project.getName();
        }
        return null;
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

    @Override
    public void setEditable(boolean flag) {
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
