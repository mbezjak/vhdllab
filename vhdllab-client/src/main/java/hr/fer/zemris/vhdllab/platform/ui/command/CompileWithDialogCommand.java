package hr.fer.zemris.vhdllab.platform.ui.command;

import hr.fer.zemris.vhdllab.platform.manager.compilation.CompilationManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public class CompileWithDialogCommand extends ActionCommand {

    public static final String ID = "compileWithDialogCommand";

    @Autowired
    private CompilationManager compilationManager;

    public CompileWithDialogCommand() {
        super(ID);
        setDisplaysInputDialog(true);
    }

    @Override
    protected void doExecuteCommand() {
        compilationManager.compileWithDialog();
    }

}
