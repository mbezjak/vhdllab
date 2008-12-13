package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.manager.component.NotOpenedException;
import hr.fer.zemris.vhdllab.platform.manager.component.impl.AbstractNoSelectionComponentManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;

public class NoSelectionEditorManager extends
        AbstractNoSelectionComponentManager implements EditorManager {

    @Override
    public void save() throws NotOpenedException {
    }

}
