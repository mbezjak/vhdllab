package hr.fer.zemris.vhdllab.platform.manager.component;

public interface ComponentManagerFactory<T extends ComponentManager> {

    T get(ComponentIdentifier identifier);

    T getSelected();

    T getAll();

    T getAllButSelected();

}
