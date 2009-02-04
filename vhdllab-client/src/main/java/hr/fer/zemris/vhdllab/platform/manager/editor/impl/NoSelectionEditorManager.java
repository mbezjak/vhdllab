package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.NotOpenedException;
import hr.fer.zemris.vhdllab.platform.manager.editor.SaveContext;

public class NoSelectionEditorManager implements EditorManager {

    @Override
    public void open() {
    }

    @Override
    public boolean isOpened() {
        return false;
    }

    @Override
    public void select() throws NotOpenedException {
    }

    @Override
    public boolean isSelected() throws NotOpenedException {
        return false;
    }

    @Override
    public void close() throws NotOpenedException {
    }

    @Override
    public void close(boolean saveFirst) throws NotOpenedException {
    }

    @Override
    public boolean save(boolean withDialog) throws NotOpenedException {
        return true;
    }

    @Override
    public boolean save(boolean withDialog, SaveContext context)
            throws NotOpenedException {
        return true;
    }

    @Override
    public void undo() throws NotOpenedException {
    }

    @Override
    public void redo() throws NotOpenedException {
    }

    @Override
    public boolean isModified() throws NotOpenedException {
        return false;
    }

    @Override
    public EditorIdentifier getIdentifier() {
        return null;
    }
}
