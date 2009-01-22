package hr.fer.zemris.vhdllab.platform.manager.component.impl;

import javax.swing.JTabbedPane;

@org.springframework.stereotype.Component
class EditorContainer extends AbstractTabbedPaneContainer {

    @Override
    protected JTabbedPane getTabbedPane() {
        return getFrame().getEditorPane();
    }

}
