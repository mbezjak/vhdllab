package hr.fer.zemris.vhdllab.platform;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.view.explorer.ProjectExplorer;

import org.springframework.stereotype.Component;

/**
 * Only temporary!
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since Mar 25, 2009
 */
@Component
public class DefaultProjectExplorer implements ProjectExplorer {

    @Override
    public File getSelectedFile() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Project getSelectedProject() {
        // TODO Auto-generated method stub
        return null;
    }

}
