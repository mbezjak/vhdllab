package hr.fer.zemris.vhdllab.platform.ui.wizard.testbench;

import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractShowWizardDialogCommand;

import org.springframework.richclient.wizard.Wizard;

public class NewTestbenchCommand extends AbstractShowWizardDialogCommand {

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewTestbenchWizard.class;
    }

}
