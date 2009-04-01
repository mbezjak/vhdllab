package hr.fer.zemris.vhdllab.applets.editor.automat;

import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditorMetadata;

public class AutomatMetadata extends AbstractEditorMetadata {

    public AutomatMetadata() {
        super(Automat.class);
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

}
