package hr.fer.zemris.vhdllab.platform.manager.view.impl;

import hr.fer.zemris.vhdllab.platform.manager.view.ViewManager;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Component;

@Component
public final class ViewRegistry {

    private final Map<JPanel, ViewManager> views;

    public ViewRegistry() {
        views = new HashMap<JPanel, ViewManager>();
    }

    public void add(ViewManager manager, JPanel panel) {
        Validate.notNull(manager, "View manager can't be null");
        Validate.notNull(panel, "Panel can't be null");
        views.put(panel, manager);
    }

    public void remove(JPanel panel) {
        Validate.notNull(panel, "Panel can't be null");
        views.remove(panel);
    }

    public ViewManager get(JPanel panel) {
        Validate.notNull(panel, "Panel can't be null");
        return views.get(panel);
    }

}
