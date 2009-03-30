package hr.fer.zemris.vhdllab.applets.schema2.gui;

import hr.fer.zemris.vhdllab.platform.manager.editor.Wizard;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditorMetadata;

public class SchemaMainPanelMetadata extends AbstractEditorMetadata {

    public SchemaMainPanelMetadata() {
        super(SchemaMainPanel.class);
    }

    @Override
    public Class<? extends Wizard> getWizardClass() {
        return null;
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
