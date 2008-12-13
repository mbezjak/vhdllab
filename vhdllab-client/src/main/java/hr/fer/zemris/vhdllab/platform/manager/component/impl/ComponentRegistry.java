package hr.fer.zemris.vhdllab.platform.manager.component.impl;

import hr.fer.zemris.vhdllab.platform.manager.component.ComponentManager;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Component;

@Component
public final class ComponentRegistry {

    private final Map<JPanel, ComponentManager> components;

    public ComponentRegistry() {
        components = new HashMap<JPanel, ComponentManager>();
    }

    public void add(ComponentManager manager, JPanel panel) {
        Validate.notNull(manager, "Component manager can't be null");
        Validate.notNull(panel, "Panel can't be null");
        components.put(panel, manager);
    }

    public void remove(JPanel panel) {
        Validate.notNull(panel, "Panel can't be null");
        components.remove(panel);
    }

    public ComponentManager get(JPanel panel) {
        Validate.notNull(panel, "Panel can't be null");
        return components.get(panel);
    }

}
