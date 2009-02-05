package hr.fer.zemris.vhdllab.platform.manager.editor;


public interface EditorMetadata {

    Class<? extends Editor> getEditorClass();

    String getCode();

    Class<? extends Wizard> getWizardClass();

    boolean isSaveable();

    boolean isEditable();

}
