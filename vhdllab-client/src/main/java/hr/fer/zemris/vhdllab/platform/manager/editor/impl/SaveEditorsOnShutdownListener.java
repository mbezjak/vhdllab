package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.editor.SaveContext;
import hr.fer.zemris.vhdllab.platform.manager.shutdown.ShutdownEvent;
import hr.fer.zemris.vhdllab.platform.manager.shutdown.ShutdownListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveEditorsOnShutdownListener implements ShutdownListener {

    @Autowired
    private EditorManagerFactory editorManagerFactory;

    @Override
    public void shutdownInProgress(ShutdownEvent event) {
        EditorManager openedEditors = editorManagerFactory.getAll();
        boolean saved = openedEditors.save(true,
                SaveContext.SHUTDOWN_AFTER_SAVE);
        if (!saved && openedEditors instanceof MulticastEditorManager) {
            event.cancelShutdown();
        }
    }

    @Override
    public int getShutdownLevel() {
        return MIN_SHUTDOWN_LEVEL;
    }

}
