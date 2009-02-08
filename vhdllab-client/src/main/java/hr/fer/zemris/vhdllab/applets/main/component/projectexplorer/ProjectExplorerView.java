package hr.fer.zemris.vhdllab.applets.main.component.projectexplorer;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.platform.manager.editor.PlatformContainer;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;

import javax.swing.JComponent;

import org.springframework.richclient.application.support.AbstractView;

public class ProjectExplorerView extends AbstractView implements
        IProjectExplorer {

    private DefaultProjectExplorer projectExplorer;

    @Override
    protected JComponent createControl() {
        projectExplorer = new DefaultProjectExplorer();
        PlatformContainer container = (PlatformContainer) getApplicationContext()
                .getBean("platformContainer");
        projectExplorer.setContainer(container);
        projectExplorer.init();
        return projectExplorer;
    }

    @Override
    public FileIdentifier getSelectedFile() {
        return projectExplorer.getSelectedFile();
    }

    @Override
    public Caseless getSelectedProject() {
        return projectExplorer.getSelectedProject();
    }

}
