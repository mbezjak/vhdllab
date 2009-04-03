package hr.fer.zemris.vhdllab.platform.ui.command;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.ActionCommand;

public class DevCreateNewLoggedErrorCommand extends ActionCommand {

    private static final Logger LOG = Logger
            .getLogger(DevCreateNewLoggedErrorCommand.class);

    public static final String ID = "createNewLoggedErrorCommand";

    public DevCreateNewLoggedErrorCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        try {
            int error = 1 / 0;
            System.out.println(error);
        } catch (RuntimeException ex) {
            LOG.error("Forced error", ex);
        }
    }

}
