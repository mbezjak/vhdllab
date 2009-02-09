package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class CloseAllCommand extends ActionCommand {

    public static final String ID = "closeAllCommand";

    @Autowired
    private EditorManagerFactory editorManagerFactory;

    public CloseAllCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        editorManagerFactory.getAll().close();
    }

}
