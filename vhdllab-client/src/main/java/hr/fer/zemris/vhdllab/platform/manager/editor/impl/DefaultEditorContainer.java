/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorContainer;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorContainerListener;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

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
    public int indexOf(Editor editor) {
        return editors.indexOf(editor);
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
    public void setSelected(int index) {
        if (index == -1) {
            setSelected(null);
        } else {
            setSelected(editors.get(index));
        }
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
