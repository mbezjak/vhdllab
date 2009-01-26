package hr.fer.zemris.vhdllab.platform.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JPanel;

import org.apache.commons.lang.Validate;

public class MaximizationManager {

    private JPanel centerPanel;
    private Component normalCenterPanel;
    private Container parentOfMaximizedComponent = null;

    public JPanel getCenterPanel(Component vhdllabCenterPanel) {
        Validate.notNull(vhdllabCenterPanel, "Centered panel can't be null");
        this.normalCenterPanel = vhdllabCenterPanel;
        this.centerPanel = new JPanel(new BorderLayout());
        this.centerPanel.add(this.normalCenterPanel, BorderLayout.CENTER);
        return centerPanel;
    }

    public void maximizeComponent(Component component) {
        Validate.notNull(component, "Component can't be null");
        if (centerPanel == null) {
            throw new IllegalStateException(
                    "Setup center panel before trying to maximize a component");
        }
        if (isMaximized()) {
            minimize(component);
        } else {
            maximize(component);
        }
        centerPanel.repaint();
        centerPanel.validate();
        component.requestFocusInWindow();
    }

    private void maximize(Component component) {
        centerPanel.remove(normalCenterPanel);
        parentOfMaximizedComponent = component.getParent();
        centerPanel.add(component, BorderLayout.CENTER);
    }

    private void minimize(Component component) {
        centerPanel.remove(component);
        parentOfMaximizedComponent.add(component);
        centerPanel.add(normalCenterPanel, BorderLayout.CENTER);
        parentOfMaximizedComponent = null;
    }

    public boolean isMaximized() {
        return parentOfMaximizedComponent != null;
    }

    public boolean isMaximized(Component component) {
        return parentOfMaximizedComponent == component;
    }

}
