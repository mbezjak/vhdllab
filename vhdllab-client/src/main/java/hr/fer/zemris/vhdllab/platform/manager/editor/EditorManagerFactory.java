package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;

public interface EditorManagerFactory {

    EditorManager get(File file);

    EditorManager get(EditorIdentifier identifier);

    EditorManager getSelected();

    EditorManager getAll();

    EditorManager getAllAssociatedWithProject(Project project);

    EditorManager getAllButSelected();

}
