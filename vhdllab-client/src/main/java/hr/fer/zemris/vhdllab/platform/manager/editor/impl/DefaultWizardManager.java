package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.i18n.AbstractLocalizationSource;
import hr.fer.zemris.vhdllab.platform.manager.editor.PlatformContainer;
import hr.fer.zemris.vhdllab.platform.manager.editor.Wizard;
import hr.fer.zemris.vhdllab.platform.manager.editor.WizardManager;
import hr.fer.zemris.vhdllab.platform.manager.file.FileAlreadyExistsException;
import hr.fer.zemris.vhdllab.platform.manager.file.FileManager;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectAlreadyExistsException;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.ProjectIdentifier;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultWizardManager extends AbstractLocalizationSource implements
        WizardManager {
    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(DefaultWizardManager.class);

    @Autowired
    private PlatformContainer platformContainer;
    @Autowired
    private FileManager fileManager;
    @Autowired
    private IProjectExplorer projectExplorer;
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
        Caseless projectName = projectExplorer.getSelectedProject();
        if (projectName == null) {
            LOG
                    .info("Select a project from Project Explorer before creating a new file");
            return false;
        }
        Wizard wizard;
        try {
            wizard = wizardRegistry.get(type).getWizardClass().newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
            wizard = null;
        }
        FileInfo file = initWizard(wizard, projectName);
        if (file == null) {
            // user canceled or no wizard for such editor
            return false;
        }
        Caseless fileName = file.getName();
        String data = file.getData();
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

    private FileInfo initWizard(Wizard wizard, Caseless projectName) {
        if (wizard == null) {
            throw new NullPointerException("Wizard cant be null");
        }
        // Initialization of a wizard
        wizard.setContainer(platformContainer);
        FileInfo file = wizard.getInitialFileContent(getFrame(), projectName);
        // end of initialization
        return file;
    }

}
