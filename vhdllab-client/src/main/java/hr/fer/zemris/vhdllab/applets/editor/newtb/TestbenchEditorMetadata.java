package hr.fer.zemris.vhdllab.applets.editor.newtb;

import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditorMetadata;

public class TestbenchEditorMetadata extends AbstractEditorMetadata {
    
    public TestbenchEditorMetadata() {
        super(TestbenchEditor.class);
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
