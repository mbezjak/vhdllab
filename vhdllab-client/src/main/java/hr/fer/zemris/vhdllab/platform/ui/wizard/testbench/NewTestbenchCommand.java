package hr.fer.zemris.vhdllab.platform.ui.wizard.testbench;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.CompilableFilesFilter;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.NotEmptyProjectFilter;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractShowWizardDialogCommand;

import java.util.List;

import org.apache.commons.collections.Predicate;
import org.springframework.richclient.wizard.Wizard;

public class NewTestbenchCommand extends AbstractShowWizardDialogCommand {

    @Override
    protected Class<? extends Wizard> getWizardClass() {
        return NewTestbenchWizard.class;
    }

    @Override
    protected void updateEnabledCommandState() {
        Predicate filter = new NotEmptyProjectFilter(manager,
                CompilableFilesFilter.getInstance());
        List<Project> projects = manager.getProjects(filter, null);
        setEnabled(!projects.isEmpty());
    }

}
