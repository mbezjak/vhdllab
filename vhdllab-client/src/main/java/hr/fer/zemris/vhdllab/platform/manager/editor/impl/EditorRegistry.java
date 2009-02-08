package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Component;

@Component
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
