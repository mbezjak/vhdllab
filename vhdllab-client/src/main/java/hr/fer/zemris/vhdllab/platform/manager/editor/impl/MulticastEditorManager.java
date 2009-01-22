package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.view.impl.AbstractMulticastViewManager;

import java.util.List;

public class MulticastEditorManager extends
        AbstractMulticastViewManager<EditorManager> implements
        EditorManager {

    public MulticastEditorManager(List<EditorManager> managers) {
        super(managers);
    }

    @Override
    public void close() {
        save(true);
        super.close();
    }

    @Override
    public void save(boolean withDialog) {
        /*
         * Should show save dialog!
         */
        for (EditorManager em : managers) {
            em.save(withDialog);
        }
    }

}
