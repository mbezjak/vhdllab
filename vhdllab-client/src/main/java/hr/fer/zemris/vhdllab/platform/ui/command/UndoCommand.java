package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class UndoCommand extends ActionCommand {

    public static final String ID = "undoCommand";

    @Autowired
    private EditorManagerFactory editorManagerFactory;

    public UndoCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        editorManagerFactory.getSelected().undo();
    }

}
