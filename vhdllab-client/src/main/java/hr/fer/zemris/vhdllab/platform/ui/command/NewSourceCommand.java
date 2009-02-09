package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.platform.manager.editor.WizardManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class NewSourceCommand extends ActionCommand {

    public static final String ID = "newSourceCommand";

    @Autowired
    private WizardManager wizardManager;

    public NewSourceCommand() {
        super(ID);
        setDisplaysInputDialog(true);
    }

    @Override
    protected void doExecuteCommand() {
        wizardManager.createNewFileInstance(FileType.SOURCE);
    }

}
