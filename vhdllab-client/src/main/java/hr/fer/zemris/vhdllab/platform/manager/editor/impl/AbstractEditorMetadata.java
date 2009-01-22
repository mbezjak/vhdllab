package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorMetadata;
import hr.fer.zemris.vhdllab.platform.manager.view.View;
import hr.fer.zemris.vhdllab.platform.manager.view.impl.AbstractComponentMetadata;

public abstract class AbstractEditorMetadata extends AbstractComponentMetadata
        implements EditorMetadata {

    private final Class<? extends Editor> editorClass;

    public AbstractEditorMetadata(Class<? extends Editor> editorClass) {
        super(editorClass);
        this.editorClass = editorClass;
    }

    @Override
    public Class<? extends View> getViewClass() {
        return null;
    }

    @Override
    public Class<? extends Editor> getEditorClass() {
        return editorClass;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }
    
    @Override
    public boolean isCloseable() {
        return true;
    }

}
