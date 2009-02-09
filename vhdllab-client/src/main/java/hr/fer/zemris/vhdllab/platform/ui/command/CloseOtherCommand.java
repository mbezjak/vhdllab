package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class CloseOtherCommand extends ActionCommand {

    public static final String ID = "closeOtherCommand";

    @Autowired
    private EditorManagerFactory editorManagerFactory;

    public CloseOtherCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        editorManagerFactory.getAllButSelected().close();
    }

}
