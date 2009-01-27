package hr.fer.zemris.vhdllab.platform.gui.menu.action;

import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.platform.gui.menu.AbstractMenuAction;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileNewAutomatonAction extends AbstractMenuAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    protected ISystemContainer systemContainer;

    @Override
    public void actionPerformed(ActionEvent e) {
        systemContainer.createNewFileInstance(FileType.AUTOMATON);
    }

}
