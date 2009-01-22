package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewMetadata;

public interface EditorMetadata extends ViewMetadata {

    Class<? extends Editor> getEditorClass();

    Class<? extends IWizard> getWizardClass();

    boolean isSaveable();

}
