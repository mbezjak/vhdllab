package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.NotOpenedException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

public class MulticastEditorManager implements EditorManager {

    protected final List<EditorManager> managers;

    public MulticastEditorManager(List<EditorManager> managers) {
        Validate.notNull(managers, "Editor managers can't be null");
        this.managers = new ArrayList<EditorManager>(managers);
        for (EditorManager man : this.managers) {
            if (!man.isOpened()) {
                throw new IllegalArgumentException(
                        "All components must be opened");
            }
        }
    }

    @Override
    public void open() {
    }

    @Override
    public boolean isOpened() {
        return !managers.isEmpty();
    }

    @Override
    public void select() {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

    @Override
    public boolean isSelected() {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

    @Override
    public void close() {
        save(true);
        for (EditorManager man : managers) {
            man.close();
        }
    }

    @Override
    public boolean save(boolean withDialog) {
        boolean editorsSaved = true;
        /*
         * Should show save dialog!
         */
        for (EditorManager em : managers) {
            editorsSaved &= em.save(withDialog);
        }
        return editorsSaved;
    }

    @Override
    public void undo() throws NotOpenedException {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

    @Override
    public void redo() throws NotOpenedException {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

    @Override
    public EditorIdentifier getIdentifier() {
        throw new UnsupportedOperationException(
                "This method isn't supported by " + getClass().getSimpleName());
    }

}
