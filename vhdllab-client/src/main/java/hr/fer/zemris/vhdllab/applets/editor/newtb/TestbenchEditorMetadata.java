package hr.fer.zemris.vhdllab.applets.editor.newtb;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.AbstractEditorMetadata;

public class TestbenchEditorMetadata extends AbstractEditorMetadata {
    
    public TestbenchEditorMetadata() {
        super(TestbenchEditor.class);
    }

    @Override
    public Class<? extends IWizard> getWizardClass() {
        return TestbenchEditor.class;
    }

}
