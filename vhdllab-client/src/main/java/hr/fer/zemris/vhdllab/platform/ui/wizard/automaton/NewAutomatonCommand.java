package hr.fer.zemris.vhdllab.platform.ui.wizard.automaton;

import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractFileCreatingCommand;

import org.springframework.richclient.wizard.Wizard;

public class NewAutomatonCommand extends AbstractFileCreatingCommand {

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewAutomatonWizard.class;
    }

}
