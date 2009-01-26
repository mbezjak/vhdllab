package hr.fer.zemris.vhdllab.platform.manager.view;

import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;

public interface ViewManager {

    void open(Class<? extends View> viewClass);

    void select(Class<? extends View> viewClass);

    IProjectExplorer getProjectExplorer();

}
