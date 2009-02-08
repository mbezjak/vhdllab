package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorContainer;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorContainerListener;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Component;

@Component
public class DefaultEditorContainer extends
        AbstractEventPublisher<EditorContainerListener> implements
        EditorContainer {

    private List<Editor> editors = new ArrayList<Editor>();
    private Editor selected;

    public DefaultEditorContainer() {
        super(EditorContainerListener.class);
    }

    @Override
    public void add(Editor editor) {
        Validate.notNull(editor, "Editor can't be null");
        editors.add(editor);
        fireEditorAdded(editor);
    }

    @Override
    public void remove(Editor editor) {
        editors.remove(editor);
        fireEditorRemoved(editor);
    }

    @Override
    public Editor getSelected() {
        return selected;
    }

    @Override
    public void setSelected(Editor editor) {
        selected = editor;
        fireEditorSelected(selected);
    }

    @Override
    public boolean isSelected(Editor editor) {
        return getSelected() == editor;
    }

    @Override
    public List<Editor> getAll() {
        return new ArrayList<Editor>(editors);
    }

    @Override
    public List<Editor> getAllButSelected() {
        List<Editor> openedEditors = getAll();
        Editor selectedEditor = getSelected();
        if (selectedEditor != null) {
            openedEditors.remove(selectedEditor);
        }
        return openedEditors;
    }

    private void fireEditorAdded(Editor editor) {
        for (EditorContainerListener l : getListeners()) {
            l.editorAdded(editor);
        }
    }

    private void fireEditorRemoved(Editor editor) {
        for (EditorContainerListener l : getListeners()) {
            l.editorRemoved(editor);
        }
    }

    private void fireEditorSelected(Editor editor) {
        for (EditorContainerListener l : getListeners()) {
            l.editorSelected(editor);
        }
    }

}
