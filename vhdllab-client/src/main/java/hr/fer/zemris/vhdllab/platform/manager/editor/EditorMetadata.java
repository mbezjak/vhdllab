package hr.fer.zemris.vhdllab.platform.manager.editor;

public interface EditorMetadata {

    Class<? extends Editor> getEditorClass();

    String getCode();

    boolean isSaveable();

    boolean isEditable();

}
