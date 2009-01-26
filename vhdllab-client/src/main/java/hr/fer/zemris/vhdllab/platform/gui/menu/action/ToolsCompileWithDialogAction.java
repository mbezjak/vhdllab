package hr.fer.zemris.vhdllab.platform.gui.menu.action;

import hr.fer.zemris.vhdllab.platform.gui.menu.AbstractMenuAction;

import java.awt.event.ActionEvent;

import org.springframework.stereotype.Component;

@Component
public class ToolsCompileWithDialogAction extends AbstractMenuAction {

    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        systemContainer.simulateWithDialog();
    }

}
