package hr.fer.zemris.vhdllab.applets.texteditor;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditorMetadata;

public class PredefinedEditorMetadata extends AbstractEditorMetadata {

    public PredefinedEditorMetadata() {
        super(TextEditor.class);
    }

    @Override
    public Class<? extends IWizard> getWizardClass() {
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
