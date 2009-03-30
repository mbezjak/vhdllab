package hr.fer.zemris.vhdllab.platform.ui.wizard.source;

import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractFileCreatingCommand;

import org.springframework.richclient.wizard.Wizard;

public class NewSourceCommand extends AbstractFileCreatingCommand {

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewSourceWizard.class;
    }

}
