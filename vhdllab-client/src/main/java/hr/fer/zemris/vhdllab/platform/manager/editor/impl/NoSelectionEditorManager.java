package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.view.NotOpenedException;
import hr.fer.zemris.vhdllab.platform.manager.view.impl.NoSelectionViewManager;

public class NoSelectionEditorManager extends NoSelectionViewManager implements
        EditorManager {

    @Override
    public void save(boolean withDialog) throws NotOpenedException {
    }

}
