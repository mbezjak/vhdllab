package hr.fer.zemris.vhdllab.platform.manager.view;

public interface ViewManager {

    void open();

    boolean isOpened();

    void select() throws NotOpenedException;

    boolean isSelected() throws NotOpenedException;

    void close() throws NotOpenedException;

}
