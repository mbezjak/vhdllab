package hr.fer.zemris.vhdllab.platform.ui.wizard;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.ui.wizard.source.PortWizardPage;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractNewFileWizard extends FormBackedWizard {

    @Autowired
    private WorkspaceManager workspaceManager;
    protected PortWizardPage portPage;

    public AbstractNewFileWizard(String wizardId) {
        super(wizardId);
    }

    @Override
    public void addPages() {
        super.addPages();
        addPortWizardPage();
    }

    protected void addPortWizardPage() {
        portPage = new PortWizardPage();
        addPage(portPage);
    }

    @Override
    protected void onWizardFinished(Object formObject) {
        File file = (File) formObject;
        CircuitInterface ci = new CircuitInterface(file.getName());
        ci.addAll(portPage.getPorts());
        file.setType(getFileType());
        file.setData(createData(ci));
        file.setProject(new Project(file.getProject()));
        workspaceManager.create(file);
    }

    protected abstract FileType getFileType();

    protected abstract String createData(CircuitInterface ci);

}
