package hr.fer.zemris.vhdllab.platform.manager.editor;

public interface EditorManager {

    void open();

    boolean isOpened();

    void select() throws NotOpenedException;

    boolean isSelected() throws NotOpenedException;

    void close() throws NotOpenedException;

    boolean save(boolean withDialog) throws NotOpenedException;

    void undo() throws NotOpenedException;

    void redo() throws NotOpenedException;

    EditorIdentifier getIdentifier();

}
