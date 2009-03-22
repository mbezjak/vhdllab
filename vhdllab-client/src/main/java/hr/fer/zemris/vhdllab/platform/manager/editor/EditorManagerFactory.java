package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.entity.File;

public interface EditorManagerFactory {

    EditorManager get(File file);

    EditorManager get(EditorIdentifier identifier);

    EditorManager getSelected();

    EditorManager getAll();

    EditorManager getAllAssociatedWithProject(Caseless projectName);

    EditorManager getAllButSelected();

}
