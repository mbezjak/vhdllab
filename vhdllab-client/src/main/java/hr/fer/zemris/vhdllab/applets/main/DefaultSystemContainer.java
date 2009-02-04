package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.constant.LanguageConstants;
import hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceAdapter;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.client.core.bundle.ResourceBundleProvider;
import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.i18n.AbstractLocalizationSource;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.WizardRegistry;
import hr.fer.zemris.vhdllab.platform.manager.file.FileAlreadyExistsException;
import hr.fer.zemris.vhdllab.platform.manager.file.FileManager;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectAlreadyExistsException;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectManager;
import hr.fer.zemris.vhdllab.platform.manager.view.PlatformContainer;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.ProjectIdentifier;

import java.util.ResourceBundle;

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
    WorkspaceManager workspaceManager;
    @Autowired
    private FileManager fileManager;
    @Autowired
    private ViewManager viewManager;
    @Autowired
    private WizardRegistry wizardRegistry;
    @Autowired
    EditorManagerFactory editorManagerFactory;
    @Autowired
    IdentifierToInfoObjectMapper mapper;
    @Autowired
    private ProjectManager projectManager;

    ResourceBundle bundle = ResourceBundleProvider
            .getBundle(LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN);

    private IProjectExplorer getProjectExplorer() {
        return viewManager.getProjectExplorer();
    }

    /* RESOURCE MANIPULATION METHODS */

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
        Caseless projectName = getProjectExplorer().getSelectedProject();
        if (projectName == null) {
            String text = getBundleString(LanguageConstants.STATUSBAR_NO_SELECTED_PROJECT);
            SystemLog.instance()
                    .addSystemMessage(text, MessageType.INFORMATION);
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

    /* PRIVATE COMMON TASK METHODS */

    /**
     * Initializes and show a wizard and returns created file content (can be
     * <code>null</code>).
     * 
     * @param wizard
     *            a wizard to initialize and show
     * @param projectName
     *            a project name
     * @return a file content returned by wizard
     * @throws NullPointerException
     *             if wizard is <code>null</code>
     */
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

    /**
     * Returns a string from a bundle for specified key.
     * 
     * @param key
     *            a key to find value
     * @return a string from a bundle for specified key
     */
    String getBundleString(String key) {
        return bundle.getString(key);
    }

    /**
     * Opens an editor for specified resource.
     * 
     * @author Miro Bezjak
     */
    private class AfterResourceCreationOpenEditor extends
            VetoableResourceAdapter {
        @Override
        public void resourceCreated(Caseless projectName, Caseless fileName,
                FileType type) {
            hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier i = new hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier(
                    projectName, fileName);
            FileInfo info = mapper.getFile(i);
            editorManagerFactory.get(info).open();
        }
    }

    /**
     * Close editor after resource has been deleted.
     * 
     * @author Miro Bezjak
     */
    private class ResourceDeletedCloseEditor extends VetoableResourceAdapter {
        @Override
        public void resourceDeleted(Caseless projectName, FileInfo file) {
            EditorManager em = editorManagerFactory.get(file);
            if (em.isOpened()) {
                em.close(false);
            }
        }
    }

}
