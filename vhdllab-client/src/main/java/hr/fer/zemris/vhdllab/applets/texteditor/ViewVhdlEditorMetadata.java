package hr.fer.zemris.vhdllab.applets.texteditor;

import hr.fer.zemris.vhdllab.platform.manager.editor.Wizard;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditorMetadata;

public class ViewVhdlEditorMetadata extends AbstractEditorMetadata {

    public ViewVhdlEditorMetadata() {
        super(TextEditor.class);
    }

    @Override
    public Class<? extends Wizard> getWizardClass() {
        return null;
    }

    @Override
    public boolean isSaveable() {
        return false;
    }

    @Override
    public boolean isEditable() {
        return false;
    }

}
