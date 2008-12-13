package hr.fer.zemris.vhdllab.platform.manager.component.impl;

import hr.fer.zemris.vhdllab.applets.main.VhdllabFrame;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentContainer;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.commons.lang.Validate;

@org.springframework.stereotype.Component
public class DefaultComponentContainer implements ComponentContainer {

    @Override
    public void add(String title, String tooltip, JPanel component,
            ComponentGroup group) {
        Validate.notNull(title, "Title can't be null");
        Validate.notNull(tooltip, "Tooltip can't be null");
        Validate.notNull(component, "Component can't be null");
        JTabbedPane pane = getTabbedPane(group);
        pane.add(title, component);
        int index = pane.indexOfComponent(component);
        pane.setToolTipTextAt(index, tooltip);
    }

    @Override
    public void remove(JPanel component, ComponentGroup group) {
        Validate.notNull(component, "Component can't be null");
        getTabbedPane(group).remove(component);
    }

    @Override
    public JPanel getSelected(ComponentGroup group) {
        return (JPanel) getTabbedPane(group).getSelectedComponent();
    }

    @Override
    public void setSelected(JPanel component, ComponentGroup group) {
        Validate.notNull(component, "Component can't be null");
        getTabbedPane(group).setSelectedComponent(component);
    }

    @Override
    public boolean isSelected(JPanel component, ComponentGroup group) {
        Validate.notNull(component, "Component can't be null");
        return getSelected(group) == component;
    }

    @Override
    public List<JPanel> getAll(ComponentGroup group) {
        JTabbedPane pane = getTabbedPane(group);
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

    private JTabbedPane getTabbedPane(ComponentGroup group) {
        Validate.notNull(group, "Component group can't be null");
        switch (group) {
        case EDITOR:
            return getFrame().getEditorPane();
        case VIEW:
            return getFrame().getViewPane();
        default:
            throw new IllegalStateException("Unrecognized component group: "
                    + group);
        }
    }

    private VhdllabFrame getFrame() {
        return (VhdllabFrame) ApplicationContextHolder.getContext().getFrame();
    }

}
