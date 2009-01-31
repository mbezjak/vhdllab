package hr.fer.zemris.vhdllab.applets.simulations;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditorMetadata;

public class WaveAppletMetadata extends AbstractEditorMetadata {

    public WaveAppletMetadata() {
        super(WaveApplet.class);
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
