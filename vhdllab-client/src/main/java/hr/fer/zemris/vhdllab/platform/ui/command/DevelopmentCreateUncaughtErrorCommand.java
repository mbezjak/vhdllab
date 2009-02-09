package hr.fer.zemris.vhdllab.platform.ui.command;

import org.springframework.richclient.command.ActionCommand;

public class DevelopmentCreateUncaughtErrorCommand extends ActionCommand {

    public static final String ID = "createUncaughtErrorCommand";

    public DevelopmentCreateUncaughtErrorCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        throw new Error("Forced unchaught error");
    }

}
