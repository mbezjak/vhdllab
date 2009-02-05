package hr.fer.zemris.vhdllab.applets.editor.automat;

import hr.fer.zemris.vhdllab.platform.manager.editor.Wizard;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditorMetadata;

public class AutomatMetadata extends AbstractEditorMetadata {

    public AutomatMetadata() {
        super(Automat.class);
    }

    @Override
    public Class<? extends Wizard> getWizardClass() {
        return Automat.class;
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
