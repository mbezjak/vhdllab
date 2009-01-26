package hr.fer.zemris.vhdllab.platform.gui.menu;

import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;

import javax.swing.AbstractAction;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMenuAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    @Autowired
    protected EditorManagerFactory editorManagerFactory;
    @Autowired
    protected ISystemContainer systemContainer;

}
