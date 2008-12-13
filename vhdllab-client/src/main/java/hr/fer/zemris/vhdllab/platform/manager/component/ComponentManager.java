package hr.fer.zemris.vhdllab.platform.manager.component;

public interface ComponentManager {

    void open();

    boolean isOpened();

    void select() throws NotOpenedException;

    boolean isSelected() throws NotOpenedException;

    void close() throws NotOpenedException;

}
