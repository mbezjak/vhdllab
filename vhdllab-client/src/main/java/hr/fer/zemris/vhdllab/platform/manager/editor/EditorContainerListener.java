package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.platform.listener.AutoPublished;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.DefaultEditorContainer;

import java.util.EventListener;

@AutoPublished(publisher = DefaultEditorContainer.class)
public interface EditorContainerListener extends EventListener {

    void editorAdded(Editor editor);

    void editorRemoved(Editor editor);

    void editorSelected(Editor editor);

}
