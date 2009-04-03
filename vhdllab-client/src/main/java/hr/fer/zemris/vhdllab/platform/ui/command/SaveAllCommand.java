package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class SaveAllCommand extends ActionCommand {

    public static final String ID = "saveAllCommand";

    @Autowired
    private EditorManagerFactory editorManagerFactory;

    public SaveAllCommand() {
        super(ID);
        setEnabled(false);
    }

    @Override
    protected void doExecuteCommand() {
        editorManagerFactory.getAll().save(false);
    }

}
