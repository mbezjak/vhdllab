package hr.fer.zemris.vhdllab.applets.texteditor;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditorMetadata;

public class ViewVhdlEditorMetadata extends AbstractEditorMetadata {
    
    public ViewVhdlEditorMetadata() {
        super(TextEditor.class);
    }

    @Override
    public Class<? extends IWizard> getWizardClass() {
        return null;
    }

}
