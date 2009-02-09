package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.platform.manager.editor.WizardManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class NewProjectCommand extends ActionCommand {

    public static final String ID = "newProjectCommand";

    @Autowired
    private WizardManager wizardManager;

    public NewProjectCommand() {
        super(ID);
        setDisplaysInputDialog(true);
    }

    @Override
    protected void doExecuteCommand() {
        wizardManager.createNewProjectInstance();
    }

}
