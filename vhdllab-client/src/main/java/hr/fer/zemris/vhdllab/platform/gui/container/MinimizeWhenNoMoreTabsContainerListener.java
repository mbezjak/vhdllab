package hr.fer.zemris.vhdllab.platform.gui.container;

import hr.fer.zemris.vhdllab.platform.gui.MaximizationManager;

import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

import javax.swing.JTabbedPane;

public class MinimizeWhenNoMoreTabsContainerListener extends ContainerAdapter {

    private MaximizationManager manager;

    public MinimizeWhenNoMoreTabsContainerListener(MaximizationManager manager) {
        this.manager = manager;
    }

    @Override
    public void componentRemoved(ContainerEvent e) {
        JTabbedPane pane = (JTabbedPane) e.getSource();
        if (pane.getTabCount() == 0 && manager.isMaximized(pane)) {
            manager.maximizeComponent(pane);
        }
    }

}
