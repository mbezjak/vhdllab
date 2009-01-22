package hr.fer.zemris.vhdllab.platform.manager.component.impl;

import hr.fer.zemris.vhdllab.platform.manager.component.ComponentContainer;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

abstract class AbstractTabbedPaneContainer extends
        AbstractComponentContainer implements ComponentContainer {

    @Override
    public void add(String title, String tooltip, JPanel component,
            ComponentGroup group) {
        JTabbedPane pane = getTabbedPane();
        pane.add(title, component);
        if (tooltip != null) {
            int index = pane.indexOfComponent(component);
            pane.setToolTipTextAt(index, tooltip);
        }
    }

    @Override
    public void setTitle(String title, JPanel component, ComponentGroup group) {
        JTabbedPane pane = getTabbedPane();
        int index = pane.indexOfComponent(component);
        pane.setTitleAt(index, title);
    }

    @Override
    public void remove(JPanel component, ComponentGroup group) {
        getTabbedPane().remove(component);
    }

    @Override
    public JPanel getSelected(ComponentGroup group) {
        return (JPanel) getTabbedPane().getSelectedComponent();
    }

    @Override
    public void setSelected(JPanel component, ComponentGroup group) {
        getTabbedPane().setSelectedComponent(component);
    }

    @Override
    public boolean isSelected(JPanel component, ComponentGroup group) {
        return getSelected(group) == component;
    }

    @Override
    public List<JPanel> getAll(ComponentGroup group) {
        JTabbedPane pane = getTabbedPane();
        List<JPanel> components = new ArrayList<JPanel>(pane.getTabCount());
        for (int i = 0; i < pane.getTabCount(); i++) {
            components.add((JPanel) pane.getComponentAt(i));
        }
        return components;
    }

    @Override
    public List<JPanel> getAllButSelected(ComponentGroup group) {
        List<JPanel> allButSelected = getAll(group);
        JPanel selected = getSelected(group);
        if (selected != null) {
            allButSelected.remove(selected);
        }
        return allButSelected;
    }

    protected abstract JTabbedPane getTabbedPane();

}
