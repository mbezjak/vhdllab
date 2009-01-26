package hr.fer.zemris.vhdllab.platform.gui.container;

import hr.fer.zemris.vhdllab.platform.gui.MaximizationManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTabbedPane;

public class MaximizeOnDoubleClickMouseListener extends MouseAdapter {

    private MaximizationManager manager;

    public MaximizeOnDoubleClickMouseListener(MaximizationManager manager) {
        this.manager = manager;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JTabbedPane pane = (JTabbedPane) e.getSource();
        if (e.getClickCount() == 2 && pane.getTabCount() != 0) {
            manager.maximizeComponent(pane);
        }
    }
}
