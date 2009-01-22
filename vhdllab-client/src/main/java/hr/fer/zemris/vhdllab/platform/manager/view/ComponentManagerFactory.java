package hr.fer.zemris.vhdllab.platform.manager.view;

public interface ComponentManagerFactory<T extends ViewManager> {

    T get(ViewIdentifier identifier);

    T getSelected();

    T getAll();

    T getAllButSelected();

}
