package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class RedoCommand extends ActionCommand {

    public static final String ID = "redoCommand";

    @Autowired
    private EditorManagerFactory editorManagerFactory;

    public RedoCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        editorManagerFactory.getSelected().redo();
    }

}
