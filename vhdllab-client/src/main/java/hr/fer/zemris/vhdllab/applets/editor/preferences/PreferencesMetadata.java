package hr.fer.zemris.vhdllab.applets.editor.preferences;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditorMetadata;

public class PreferencesMetadata extends AbstractEditorMetadata {

    public PreferencesMetadata() {
        super(PreferencesEditor.class);
    }

    @Override
    public Class<? extends IWizard> getWizardClass() {
        return null;
    }

}
