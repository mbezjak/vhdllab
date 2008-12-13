package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentManagerFactory;

public interface EditorManagerFactory extends
        ComponentManagerFactory<EditorManager> {

    EditorManager get(FileInfo file);

}
