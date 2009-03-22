package hr.fer.zemris.vhdllab.platform.ui.wizard.file;

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractResourceCreatingWizard;
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
        FileFormObject file = (FileFormObject) formObject;
        List<CircuitInterfaceObject> ports = circuitInterfacePage.getPorts();
        FileType type = getFileType();
        String data = createData(file, ports);
        File f = new File(type, new Caseless(file.getFileName()), data,
                file.getProject().getId());
        workspaceManager.create(f);
    }

    private FileType getFileType() {
        return FileType.SOURCE;
    }

    protected String createData(FileFormObject file,
            List<CircuitInterfaceObject> ports) {
        StringBuilder sb = new StringBuilder(100 + ports.size() * 20);
        sb.append("library IEEE;\nuse IEEE.STD_LOGIC_1164.ALL;\n\n");
        sb.append("-- note: entity name and resource name must match\n");
        sb.append("ENTITY ").append(file.getFileName()).append(" IS PORT (\n");
        for (CircuitInterfaceObject p : ports) {
            PortType type = p.getTypeName();
            sb.append("\t").append(p.getName()).append(" : ").append(
                    p.getPortDirection().toString()).append(" ").append(
                    type.toString());
            if (type.equals(PortType.STD_LOGIC_VECTOR)) {
                sb.append(" (").append(p.getFrom()).append(" ")
                        .append(p.getFrom() >= p.getTo() ? "DOWNTO" : "TO")
                        .append(" ").append(p.getTo()).append(")");
            }
            sb.append(";\n");
        }
        if (ports.size() == 0) {
            sb.replace(sb.length() - 8, sb.length(), "\n");
        } else {
            sb.replace(sb.length() - 2, sb.length() - 1, ");");
        }
        sb.append("end ").append(file.getFileName()).append(";\n\n");
        sb.append("ARCHITECTURE arch OF ").append(file.getFileName()).append(
                " IS \n\nBEGIN\n\nEND arch;");

        return sb.toString();
    }

}
