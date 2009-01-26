package hr.fer.zemris.vhdllab.platform.gui.menu.action;

import hr.fer.zemris.vhdllab.applets.editor.preferences.PreferencesMetadata;
import hr.fer.zemris.vhdllab.platform.gui.menu.AbstractMenuAction;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;

import java.awt.event.ActionEvent;

import org.springframework.stereotype.Component;

@Component
public class ToolsViewPreferencesAction extends AbstractMenuAction {

    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        editorManagerFactory.get(
                new EditorIdentifier(new PreferencesMetadata())).open();
    }

}
