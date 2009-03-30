package hr.fer.zemris.vhdllab.platform.ui.wizard.schema;

import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractFileCreatingCommand;

import org.springframework.richclient.wizard.Wizard;

public class NewSchemaCommand extends AbstractFileCreatingCommand {

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewSchemaWizard.class;
    }

}
