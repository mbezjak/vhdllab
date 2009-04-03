package hr.fer.zemris.vhdllab.platform.ui.wizard.project;

import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractShowWizardDialogCommand;

import org.springframework.richclient.wizard.Wizard;

public class NewProjectCommand extends AbstractShowWizardDialogCommand {

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewProjectWizard.class;
    }

}
