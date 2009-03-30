package hr.fer.zemris.vhdllab.platform.ui.wizard.source;

import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractNewFileWizard;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.util.BeanUtil;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewSourceWizard extends AbstractNewFileWizard {

    public NewSourceWizard() {
        super(BeanUtil.getBeanName(NewSourceWizard.class));
    }

    @Override
    protected FileType getFileType() {
        return FileType.SOURCE;
    }

    @Override
    protected String createData(CircuitInterface ci) {
        StringBuilder sb = new StringBuilder(100 + ci.getPorts().size() * 20);
        sb.append("library IEEE;\nuse IEEE.STD_LOGIC_1164.ALL;\n\n");
        sb.append("-- note: entity name and resource name must match\n");
        sb.append(ci.toString()).append("\n\n");
        sb.append("ARCHITECTURE arch OF ").append(ci.getName());
        sb.append(" IS \n\nBEGIN\n\nEND arch;");
        return sb.toString();
    }

}
