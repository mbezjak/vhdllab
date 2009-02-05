package hr.fer.zemris.vhdllab.applets.editor.preferences;

import hr.fer.zemris.vhdllab.platform.manager.editor.Wizard;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditorMetadata;

public class PreferencesEditorMetadata extends AbstractEditorMetadata {

    public PreferencesEditorMetadata() {
        super(PreferencesEditor.class);
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
