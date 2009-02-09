package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.applets.main.component.about.About;

import org.springframework.richclient.command.ActionCommand;

public class AboutCommand extends ActionCommand {

    public static final String ID = "aboutCommand";

    public AboutCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        About.instance().setVisible(true);
    }

}
