package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.applets.editor.preferences.PreferencesEditorMetadata;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class ViewPreferencesCommand extends ActionCommand {

    public static final String ID = "viewPreferencesCommand";

    @Autowired
    private EditorManagerFactory editorManagerFactory;

    public ViewPreferencesCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        editorManagerFactory.get(
                new EditorIdentifier(new PreferencesEditorMetadata())).open();
    }

}
