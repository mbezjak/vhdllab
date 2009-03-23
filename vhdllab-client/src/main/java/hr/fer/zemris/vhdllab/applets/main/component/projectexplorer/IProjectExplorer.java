package hr.fer.zemris.vhdllab.applets.main.component.projectexplorer;

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.entity.File;

public interface IProjectExplorer {

    Caseless getSelectedProject();

    File getSelectedFile();

}
