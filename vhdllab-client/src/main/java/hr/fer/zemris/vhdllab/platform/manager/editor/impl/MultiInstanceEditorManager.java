package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;
import hr.fer.zemris.vhdllab.platform.manager.component.impl.AbstractComponentManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;

public class MultiInstanceEditorManager extends
        AbstractComponentManager<Editor> implements EditorManager {

    public MultiInstanceEditorManager(EditorIdentifier identifier) {
        super(identifier, ComponentGroup.EDITOR);
    }

    @Override
    protected void configureComponent() {
    }

    @Override
    public void save() {
        checkIfOpened();
    }

}
