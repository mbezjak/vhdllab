package hr.fer.zemris.vhdllab.platform.ui.command;

import java.awt.Frame;

import javax.swing.JOptionPane;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;

public class DevShowModalDialogCommand extends ActionCommand {

    public static final String ID = "showModalDialogCommand";

    public DevShowModalDialogCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        Frame owner = Application.instance().getActiveWindow().getControl();
        JOptionPane.showMessageDialog(owner, "is it modal?");
    }

}
