package hr.fer.zemris.vhdllab.platform.manager.component;

import java.util.List;

import javax.swing.JPanel;

public interface ComponentContainer {

    void add(String title, String tooltip, JPanel component,
            ComponentGroup group);

    void setTitle(String title, JPanel component, ComponentGroup group);

    void remove(JPanel component, ComponentGroup group);

    JPanel getSelected(ComponentGroup group);

    void setSelected(JPanel component, ComponentGroup group);

    boolean isSelected(JPanel component, ComponentGroup group);

    List<JPanel> getAll(ComponentGroup group);

    List<JPanel> getAllButSelected(ComponentGroup group);

}
