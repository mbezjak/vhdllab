package hr.fer.zemris.vhdllab.platform.ui.wizard;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.FileForm;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.PortWizardPage;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.util.EntityUtils;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractNewFileWizard extends
        AbstractFormSupportingWizard {

    @Autowired
    private WorkspaceManager manager;

    @Override
    protected void onWizardFinished() {
        File file = getFile();
        file.setProject(EntityUtils.lightweightClone(file.getProject()));
        file.setType(getFileType());
        file.setData(createData());
        manager.create(file);
    }

    protected abstract File getFile();

    protected abstract FileType getFileType();

    protected abstract String createData();

    @Override
    protected File getFile(FileForm fileForm) {
        return (File) fileForm.getFormObject();
    }

    @Override
    protected CircuitInterface getCircuitInterface(FileForm fileForm,
            PortWizardPage portPage) {
        File file = getFile(fileForm);
        CircuitInterface ci = new CircuitInterface(file.getName());
        ci.addAll(portPage.getPorts());
        return ci;
    }

}
