package hr.fer.zemris.vhdllab.platform.manager.editor;

public interface EditorManager {

    void open();

    boolean isOpened();

    void select() throws NotOpenedException;

    boolean isSelected() throws NotOpenedException;

    void close() throws NotOpenedException;

    void close(boolean saveFirst) throws NotOpenedException;

    boolean save(boolean withDialog) throws NotOpenedException;

    boolean save(boolean withDialog, SaveContext context)
            throws NotOpenedException;

    void undo() throws NotOpenedException;

    void redo() throws NotOpenedException;

    void highlightLine(int line) throws NotOpenedException;

    boolean isModified() throws NotOpenedException;

    EditorIdentifier getIdentifier();

}
