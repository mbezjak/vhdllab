package hr.fer.zemris.vhdllab.platform.ui.wizard.testbench;

import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializer;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractShowWizardDialogCommand;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.WorkspaceHasCompilableFileCommandGuard;

import org.springframework.richclient.wizard.Wizard;

public class NewTestbenchCommand extends AbstractShowWizardDialogCommand {

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewTestbenchWizard.class;
    }

    @Override
    protected void installCommandGuard(WorkspaceManager wm,
            WorkspaceInitializer wi, SimulationManager sm) {
        new WorkspaceHasCompilableFileCommandGuard(wm, wi, this);
    }

}
