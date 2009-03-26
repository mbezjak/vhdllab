package hr.fer.zemris.vhdllab.view.explorer;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;

public interface ProjectExplorer {

    Project getSelectedProject();

    File getSelectedFile();

}
