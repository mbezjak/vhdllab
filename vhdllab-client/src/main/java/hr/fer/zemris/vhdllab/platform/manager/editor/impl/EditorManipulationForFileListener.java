package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.api.workspace.FileReport;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.file.FileAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EditorManipulationForFileListener extends FileAdapter {

    @Autowired
    private EditorManagerFactory editorManagerFactory;

    @Override
    public void fileCreated(FileReport report) {
        editorManagerFactory.get(report.getFile()).open();
    }

    @Override
    public void fileDeleted(FileReport report) {
        EditorManager em = editorManagerFactory.get(report.getFile());
        if (em.isOpened()) {
            em.close(false);
        }
    }

}
