package hr.fer.zemris.vhdllab.platform.ui.command;

import org.springframework.richclient.command.ActionCommand;

public class DevCreateUncaughtExceptionCommand extends ActionCommand {

    public static final String ID = "createUncaughtExceptionCommand";

    public DevCreateUncaughtExceptionCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        throw new IllegalStateException("Forced unchaught error");
    }

}
