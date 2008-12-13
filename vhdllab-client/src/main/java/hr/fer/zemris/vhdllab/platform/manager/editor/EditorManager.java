package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.platform.manager.component.ComponentManager;
import hr.fer.zemris.vhdllab.platform.manager.component.NotOpenedException;

public interface EditorManager extends ComponentManager {

    void save() throws NotOpenedException;

}
