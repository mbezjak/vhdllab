package hr.fer.zemris.vhdllab.platform.ui.wizard.automaton;

import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractShowWizardDialogCommand;

import org.springframework.richclient.wizard.Wizard;

public class NewAutomatonCommand extends AbstractShowWizardDialogCommand {

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewAutomatonWizard.class;
    }

}
