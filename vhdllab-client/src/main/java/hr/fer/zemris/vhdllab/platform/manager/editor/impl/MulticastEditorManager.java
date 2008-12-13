package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.manager.component.impl.AbstractMulticastComponentManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;

import java.util.List;

public class MulticastEditorManager extends
        AbstractMulticastComponentManager<EditorManager> implements
        EditorManager {

    public MulticastEditorManager(List<EditorManager> managers) {
        super(managers);
    }

    @Override
    public void close() {
        save();
        super.close();
    }

    @Override
    public void save() {
        for (EditorManager em : managers) {
            em.save();
        }
    }

}
