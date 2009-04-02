package hr.fer.zemris.vhdllab.platform.ui.wizard.source;

import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractShowWizardDialogCommand;

import org.springframework.richclient.wizard.Wizard;

public class NewSourceCommand extends AbstractShowWizardDialogCommand {

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewSourceWizard.class;
    }

}
