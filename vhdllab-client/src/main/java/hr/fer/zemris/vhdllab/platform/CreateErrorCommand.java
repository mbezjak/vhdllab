package hr.fer.zemris.vhdllab.platform;

import org.springframework.richclient.command.ActionCommand;

public class CreateErrorCommand extends ActionCommand {

    @Override
    protected void doExecuteCommand() {
        throw new IllegalStateException("Exception caused on purpose");
//        throw new Error("ss");
    }

}
