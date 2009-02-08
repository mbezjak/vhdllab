package hr.fer.zemris.vhdllab.platform.gui.menu.action;

import hr.fer.zemris.vhdllab.platform.gui.menu.AbstractMenuAction;
import hr.fer.zemris.vhdllab.platform.manager.shutdown.ShutdownManager;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileExitAction extends AbstractMenuAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ShutdownManager shutdownManager;

    @Override
    public void actionPerformed(ActionEvent e) {
        shutdownManager.shutdownWithoutGUI();
    }

}
