package hr.fer.zemris.vhdllab.applets.texteditor;

import hr.fer.zemris.vhdllab.platform.manager.editor.Wizard;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditorMetadata;

public class TextEditorMetadata extends AbstractEditorMetadata {

    public TextEditorMetadata() {
        super(TextEditor.class);
    }

    @Override
    public Class<? extends Wizard> getWizardClass() {
        return TextEditor.class;
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
