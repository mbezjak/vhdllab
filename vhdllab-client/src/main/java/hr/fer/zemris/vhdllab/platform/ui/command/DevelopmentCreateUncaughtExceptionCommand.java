package hr.fer.zemris.vhdllab.platform.ui.command;

import org.springframework.richclient.command.ActionCommand;

public class DevelopmentCreateUncaughtExceptionCommand extends ActionCommand {

    public static final String ID = "createUncaughtExceptionCommand";

    public DevelopmentCreateUncaughtExceptionCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        throw new IllegalStateException("Forced unchaught error");
    }

}
