package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;

public interface EditorMetadata {

    Class<? extends Editor> getEditorClass();

    String getCode();

    Class<? extends IWizard> getWizardClass();

    boolean isSaveable();

    boolean isEditable();

}
