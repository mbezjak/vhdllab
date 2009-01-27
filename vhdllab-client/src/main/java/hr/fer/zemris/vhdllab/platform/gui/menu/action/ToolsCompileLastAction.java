package hr.fer.zemris.vhdllab.platform.gui.menu.action;

import hr.fer.zemris.vhdllab.platform.gui.menu.AbstractMenuAction;
import hr.fer.zemris.vhdllab.platform.manager.compilation.CompilationManager;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ToolsCompileLastAction extends AbstractMenuAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    protected CompilationManager compilationManager;

    @Override
    public void actionPerformed(ActionEvent e) {
        compilationManager.compileLast();
    }

}
