package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.platform.manager.editor.WizardManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class NewSchemaCommand extends ActionCommand {

    public static final String ID = "newSchemaCommand";

    @Autowired
    private WizardManager wizardManager;

    public NewSchemaCommand() {
        super(ID);
        setDisplaysInputDialog(true);
    }

    @Override
    protected void doExecuteCommand() {
        wizardManager.createNewFileInstance(FileType.SCHEMA);
    }

}
