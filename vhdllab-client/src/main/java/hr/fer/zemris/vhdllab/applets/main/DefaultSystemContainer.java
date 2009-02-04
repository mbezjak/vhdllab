package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.i18n.AbstractLocalizationSource;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.WizardRegistry;
import hr.fer.zemris.vhdllab.platform.manager.file.FileAlreadyExistsException;
import hr.fer.zemris.vhdllab.platform.manager.file.FileManager;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectAlreadyExistsException;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectManager;
import hr.fer.zemris.vhdllab.platform.manager.view.PlatformContainer;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.ProjectIdentifier;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultSystemContainer extends AbstractLocalizationSource
        implements ISystemContainer {
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(DefaultSystemContainer.class);

    @Autowired
    private PlatformContainer platformContainer;
    @Autowired
    private FileManager fileManager;
    @Autowired
    private ViewManager viewManager;
    @Autowired
    private WizardRegistry wizardRegistry;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    @Autowired
    private ProjectManager projectManager;

    @Override
    public void createNewProjectInstance() {
        String projectName = JOptionPane.showInputDialog(getFrame(),
                "Type a name of a project you want to create",
                "Create project", JOptionPane.PLAIN_MESSAGE);
        if (projectName == null) {
            return;
        }
        try {
            projectManager.create(new ProjectInfo(ApplicationContextHolder
                    .getContext().getUserId(), new Caseless(projectName)));
        } catch (IllegalArgumentException e) {
            LOG.info(projectName + " isn't a valid project name");
        } catch (ProjectAlreadyExistsException e) {
            LOG.info("Project " + projectName + " already exists");
        }
    }

    @Override
    public boolean createNewFileInstance(FileType type) {
        if (type == null) {
            throw new NullPointerException("File type cant be null");
        }
        Caseless projectName = viewManager.getProjectExplorer()
                .getSelectedProject();
        if (projectName == null) {
            LOG
                    .info("Select a project from Project Explorer before creating a new file");
            return false;
        }
        IWizard wizard;
        try {
            wizard = wizardRegistry.get(type).getWizardClass().newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
            wizard = null;
        }
        FileContent content = initWizard(wizard, projectName);
        if (content == null) {
            // user canceled or no wizard for such editor
            return false;
        }
        projectName = content.getProjectName();
        Caseless fileName = content.getFileName();
        String data = content.getContent();
        ProjectInfo project = mapper.getProject(new ProjectIdentifier(
                projectName));
        try {
            fileManager.create(new FileInfo(type, fileName, data, project
                    .getId()));
        } catch (IllegalArgumentException e) {
            LOG.info(fileName + " isn't a valid file name");
        } catch (FileAlreadyExistsException e) {
            LOG.info(fileName + " already exists in " + projectName);

        }
        return true;
    }

    private FileContent initWizard(IWizard wizard, Caseless projectName) {
        if (wizard == null) {
            throw new NullPointerException("Wizard cant be null");
        }
        // Initialization of a wizard
        wizard.setContainer(platformContainer);
        FileContent content = wizard.getInitialFileContent(getFrame(),
                projectName);
        // end of initialization
        return content;
    }

}
