package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class CloseCommand extends ActionCommand {

    public static final String ID = "closeCommand";

    @Autowired
    private EditorManagerFactory editorManagerFactory;

    public CloseCommand() {
        super(ID);
        setEnabled(false);
    }

    @Override
    protected void doExecuteCommand() {
        editorManagerFactory.getSelected().close();
    }

}
