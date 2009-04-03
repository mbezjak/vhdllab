package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class SaveCommand extends ActionCommand {

    public static final String ID = "saveCommand";

    @Autowired
    private EditorManagerFactory editorManagerFactory;

    public SaveCommand() {
        super(ID);
        setEnabled(false);
    }

    @Override
    protected void doExecuteCommand() {
        editorManagerFactory.getSelected().save(false);
    }

}
