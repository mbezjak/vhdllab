package hr.fer.zemris.vhdllab.platform.gui.container;

import hr.fer.zemris.vhdllab.platform.gui.MaximizationManager;

public class EditorTabbedPane extends ViewTabbedPane {

    private static final long serialVersionUID = 1L;

    public EditorTabbedPane(MaximizationManager manager) {
        super(manager);
    }

    @Override
    protected String getIconName() {
        return "editor.png";
    }

}
