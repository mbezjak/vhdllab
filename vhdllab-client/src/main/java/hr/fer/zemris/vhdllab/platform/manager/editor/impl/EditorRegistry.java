package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Component;

@Component
public final class EditorRegistry {

    private final Map<JPanel, EditorManager> views;
    private final Map<EditorIdentifier, EditorManager> editors;

    public EditorRegistry() {
        views = new HashMap<JPanel, EditorManager>();
        editors = new HashMap<EditorIdentifier, EditorManager>();
    }

    public void add(EditorManager manager, JPanel panel,
            EditorIdentifier identifier) {
        Validate.notNull(manager, "Editor manager can't be null");
        Validate.notNull(panel, "Panel can't be null");
        Validate.notNull(identifier, "Editor identifier can't be null");
        views.put(panel, manager);
        editors.put(identifier, manager);
    }

    public void remove(JPanel panel) {
        Validate.notNull(panel, "Panel can't be null");
        views.remove(panel);
    }

    public EditorManager get(JPanel panel) {
        Validate.notNull(panel, "Panel can't be null");
        return views.get(panel);
    }

    public EditorManager get(EditorIdentifier identifier) {
        Validate.notNull(identifier, "Editor identifier can't be null");
        return editors.get(identifier);
    }

}
