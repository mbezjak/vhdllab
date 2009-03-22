package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.i18n.AbstractLocalizationSource;
import hr.fer.zemris.vhdllab.platform.manager.editor.PlatformContainer;
import hr.fer.zemris.vhdllab.platform.manager.editor.Wizard;
import hr.fer.zemris.vhdllab.platform.manager.editor.WizardManager;
import hr.fer.zemris.vhdllab.platform.manager.file.FileAlreadyExistsException;
import hr.fer.zemris.vhdllab.platform.manager.file.FileManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.ProjectIdentifier;

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

    @Override
    public boolean createNewFileInstance(FileType type) {
        if (type == null) {
            throw new NullPointerException("File type cant be null");
        }
//        Caseless projectName = projectExplorer.getSelectedProject();
        Caseless projectName = new Caseless("a2");
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
        File file = initWizard(wizard, projectName);
        if (file == null) {
            // user canceled or no wizard for such editor
            return false;
        }
        Caseless fileName = file.getName();
        String data = file.getData();
        Project project = mapper.getProject(new ProjectIdentifier(
                projectName));
        try {
            fileManager.create(new File(type, fileName, data, project
                    .getId()));
        } catch (IllegalArgumentException e) {
            LOG.info(fileName + " isn't a valid file name");
        } catch (FileAlreadyExistsException e) {
            LOG.info(fileName + " already exists in " + projectName);

        }
        return true;
    }

    private File initWizard(Wizard wizard, Caseless projectName) {
        if (wizard == null) {
            throw new NullPointerException("Wizard cant be null");
        }
        // Initialization of a wizard
        wizard.setContainer(platformContainer);
        File file = wizard.getInitialFileContent(getFrame(), projectName);
        // end of initialization
        return file;
    }

}
