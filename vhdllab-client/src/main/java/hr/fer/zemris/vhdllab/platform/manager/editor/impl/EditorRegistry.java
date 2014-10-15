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

import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

public final class EditorRegistry {

    private final Map<Editor, EditorManager> views;
    private final Map<EditorIdentifier, EditorManager> editors;

    public EditorRegistry() {
        views = new HashMap<Editor, EditorManager>();
        editors = new HashMap<EditorIdentifier, EditorManager>();
    }

    public void add(EditorManager manager, Editor editor,
            EditorIdentifier identifier) {
        Validate.notNull(manager, "Editor manager can't be null");
        Validate.notNull(editor, "Editor can't be null");
        Validate.notNull(identifier, "Editor identifier can't be null");
        views.put(editor, manager);
        editors.put(identifier, manager);
    }

    public void remove(Editor editor) {
        Validate.notNull(editor, "Editor can't be null");
        EditorManager em = views.remove(editor);
        editors.remove(em.getIdentifier());
    }

    public EditorManager get(Editor panel) {
        Validate.notNull(panel, "Editor can't be null");
        return views.get(panel);
    }

    public EditorManager get(EditorIdentifier identifier) {
        Validate.notNull(identifier, "Editor identifier can't be null");
        return editors.get(identifier);
    }

}
