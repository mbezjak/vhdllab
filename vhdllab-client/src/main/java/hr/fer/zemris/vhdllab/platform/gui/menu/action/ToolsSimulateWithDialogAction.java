package hr.fer.zemris.vhdllab.platform.gui.menu.action;

import hr.fer.zemris.vhdllab.platform.gui.menu.AbstractMenuAction;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ToolsSimulateWithDialogAction extends AbstractMenuAction {

    private static final long serialVersionUID = 1L;
    
    @Autowired
    protected SimulationManager simulationManager;

    @Override
    public void actionPerformed(ActionEvent e) {
        simulationManager.simulateWithDialog();
    }

}
