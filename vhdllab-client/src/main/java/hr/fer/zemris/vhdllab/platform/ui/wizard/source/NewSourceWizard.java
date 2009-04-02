package hr.fer.zemris.vhdllab.platform.ui.wizard.source;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractNewFileWizard;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.FileForm;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.PortWizardPage;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewSourceWizard extends AbstractNewFileWizard {

    private FileForm fileForm;
    private PortWizardPage portWizardPage;

    @Override
    public void addPages() {
        fileForm = new FileForm();
        addForm(fileForm);

        portWizardPage = new PortWizardPage();
        addPage(portWizardPage);
    }

    @Override
    protected File getFile() {
        return getFile(fileForm);
    }

    @Override
    protected FileType getFileType() {
        return FileType.SOURCE;
    }

    @Override
    protected String createData() {
        CircuitInterface ci = getCircuitInterface(fileForm, portWizardPage);
        StringBuilder sb = new StringBuilder(100 + ci.getPorts().size() * 20);
        sb.append("library IEEE;\nuse IEEE.STD_LOGIC_1164.ALL;\n\n");
        sb.append("-- note: entity name and resource name must match\n");
        sb.append(ci.toString()).append("\n\n");
        sb.append("ARCHITECTURE arch OF ").append(ci.getName());
        sb.append(" IS \n\nBEGIN\n\nEND arch;");
        return sb.toString();
    }

}
