package hr.fer.zemris.vhdllab.platform.ui.wizard.file;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractResourceCreatingWizard;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.ci.Port;
import hr.fer.zemris.vhdllab.util.BeanUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewSourceWizard extends AbstractResourceCreatingWizard {

    @Autowired
    private WorkspaceManager workspaceManager;
    private CircuitInterfaceWizardPage circuitInterfacePage;

    public NewSourceWizard() {
        super(BeanUtil.getBeanName(NewSourceWizard.class));
    }

    @Override
    public void addPages() {
        super.addPages();
        circuitInterfacePage = new CircuitInterfaceWizardPage();
        addPage(circuitInterfacePage);
    }

    @Override
    protected void onWizardFinished(Object formObject) {
        File file = (File) formObject;
        List<Port> ports = circuitInterfacePage.getPorts();
        file.setType(getFileType());
        file.setData(createData(file, ports));
        file.setProject(new Project(file.getProject()));
        workspaceManager.create(file);
    }

    private FileType getFileType() {
        return FileType.SOURCE;
    }

    protected String createData(File file, List<Port> ports) {
        CircuitInterface ci = new CircuitInterface(file.getName());
        ci.addAll(ports);
        StringBuilder sb = new StringBuilder(100 + ports.size() * 20);
        sb.append("library IEEE;\nuse IEEE.STD_LOGIC_1164.ALL;\n\n");
        sb.append("-- note: entity name and resource name must match\n");
        sb.append(ci.toString()).append("\n\n");
        sb.append("ARCHITECTURE arch OF ").append(file.getName());
        sb.append(" IS \n\nBEGIN\n\nEND arch;");
        return sb.toString();
    }
}
