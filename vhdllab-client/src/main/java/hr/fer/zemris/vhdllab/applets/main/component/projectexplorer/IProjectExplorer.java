package hr.fer.zemris.vhdllab.applets.main.component.projectexplorer;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;

public interface IProjectExplorer {

    Project getSelectedProject();

    File getSelectedFile();

}
