package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;

public interface EditorManagerFactory {

    EditorManager get(FileInfo file);

    EditorManager get(EditorIdentifier identifier);

    EditorManager getSelected();

    EditorManager getAll();

    EditorManager getAllAssociatedWithProject(Caseless projectName);

    EditorManager getAllButSelected();

}
