package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.platform.manager.view.NotOpenedException;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewManager;

public interface EditorManager extends ViewManager {

    void save(boolean withDialog) throws NotOpenedException;

}
