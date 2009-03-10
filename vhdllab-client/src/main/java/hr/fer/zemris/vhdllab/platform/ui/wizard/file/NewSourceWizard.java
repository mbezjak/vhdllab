package hr.fer.zemris.vhdllab.platform.ui.wizard.file;

import hr.fer.zemris.vhdllab.api.vhdl.TypeName;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.platform.manager.file.FileManager;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractResourceCreatingWizard;
import hr.fer.zemris.vhdllab.platform.util.BeanUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewSourceWizard extends AbstractResourceCreatingWizard {

    @Autowired
    private FileManager fileManager;
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
        FileInfo f = new FileInfo(type, new Caseless(file.getFileName()), data,
                file.getProject().getId());
        fileManager.create(f);
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
            TypeName type = p.getTypeName();
            sb.append("\t").append(p.getName()).append(" : ").append(
                    p.getPortDirection().toString()).append(" ").append(
                    type.toString());
            if (type.equals(TypeName.STD_LOGIC_VECTOR)) {
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
