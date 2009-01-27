package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.api.workspace.Workspace;
import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.constant.LanguageConstants;
import hr.fer.zemris.vhdllab.applets.main.dialog.RunDialog;
import hr.fer.zemris.vhdllab.applets.main.event.ResourceVetoException;
import hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceAdapter;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.client.core.bundle.ResourceBundleProvider;
import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.WizardRegistry;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectManager;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializationListener;
import hr.fer.zemris.vhdllab.utilities.PlaceholderUtil;

import java.awt.Frame;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultSystemContainer implements ISystemContainer,
        WorkspaceInitializationListener {

    @Autowired
    private IResourceManager resourceManager;
    @Autowired
    private ViewManager viewManager;
    @Autowired
    private WizardRegistry wizardRegistry;
    @Autowired
    private EditorManagerFactory editorManagerFactory;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    @Autowired
    private ProjectManager projectManager;
    private ResourceBundle bundle;

    private Frame getParentFrame() {
        return ApplicationContextHolder.getContext().getFrame();
    }

    @Override
    public void initialize(Workspace workspace) {
        try {
            init();
        } catch (UniformAppletException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Initializes a system container.
     * 
     * @throws UniformAppletException
     *             if exceptional condition occurs
     */
    private void init() throws UniformAppletException {
        bundle = ResourceBundleProvider
                .getBundle(LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN);
        resourceManager
                .addVetoableResourceListener(new BeforeCompilationCheckCompilableAndSaveEditors());
        resourceManager.addVetoableResourceListener(new AfterCompilationEcho());
        resourceManager
                .addVetoableResourceListener(new BeforeSimulationCheckSimulatableAndSaveEditors());
        resourceManager.addVetoableResourceListener(new AfterSimulationEcho());
        // resourceManager
        // .addVetoableResourceListener(new
        // BeforeProjectCreationCheckExistence());
        // resourceManager
        // .addVetoableResourceListener(new
        // BeforeProjectCreationCheckCorrectName());
        resourceManager
                .addVetoableResourceListener(new AfterProjectCreationEcho());
        resourceManager
                .addVetoableResourceListener(new BeforeResourceCreationCheckExistence());
        resourceManager
                .addVetoableResourceListener(new BeforeResourceCreationCheckCorrectName());
        resourceManager
                .addVetoableResourceListener(new AfterResourceCreationEcho());
        resourceManager
                .addVetoableResourceListener(new AfterResourceCreationOpenEditor());
        resourceManager.addVetoableResourceListener(new ResourceSavedEcho());
        resourceManager
                .addVetoableResourceListener(new ResourceDeletedCloseEditor());
    }

    public IProjectExplorer getProjectExplorer() {
        return viewManager.getProjectExplorer();
    }

    /**
     * Disposes any resources used by system container.
     * 
     * @throws UniformAppletException
     *             if exceptional condition occurs
     */
    private void dispose() throws UniformAppletException {
        // TODO kada se sustav gasi ovo se mora pozvat. samo ne s false
        // zastavicom nego sa true
        editorManagerFactory.getAll().save(false);
    }

    /* RESOURCE MANIPULATION METHODS */

    @Override
    public void createNewProjectInstance() {
        String projectName = showCreateProjectDialog();
        if (projectName == null) {
            return;
        }
        projectManager.create(new ProjectInfo(ApplicationContextHolder
                .getContext().getUserId(), new Caseless(projectName)));
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
        try {
            return resourceManager.createNewResource(projectName, fileName,
                    type, data);
        } catch (UniformAppletException e) {
            String text = getBundleString(LanguageConstants.STATUSBAR_CANT_CREATE_FILE);
            text = PlaceholderUtil.replacePlaceholders(text,
                    new Caseless[] { fileName });
            SystemLog.instance().addSystemMessage(text, MessageType.ERROR);
            return false;
        }
    }

    @Override
    public IResourceManager getResourceManager() {
        return resourceManager;
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
        wizard.setSystemContainer(this);
        FileContent content = wizard.getInitialFileContent(getParentFrame(),
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
    private String getBundleString(String key) {
        return bundle.getString(key);
    }

    /* SHOW DIALOGS METHODS */

    public hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier showCompilationRunDialog() {
        String title = getBundleString(LanguageConstants.DIALOG_RUN_COMPILATION_TITLE);
        String listTitle = getBundleString(LanguageConstants.DIALOG_RUN_COMPILATION_LIST_TITLE);
        FileIdentifier file = showRunDialog(title, listTitle,
                RunDialog.COMPILATION_TYPE);
        return new hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier(
                file.getProjectName(), file.getFileName());
    }

    public hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier showSimulationRunDialog() {
        String title = getBundleString(LanguageConstants.DIALOG_RUN_SIMULATION_TITLE);
        String listTitle = getBundleString(LanguageConstants.DIALOG_RUN_SIMULATION_LIST_TITLE);
        FileIdentifier file = showRunDialog(title, listTitle,
                RunDialog.SIMULATION_TYPE);
        return new hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier(
                file.getProjectName(), file.getFileName());
    }

    /**
     * TODO PENDING CHANGE! also to change (by transition): - compileWithDialog
     */
    private FileIdentifier showRunDialog(String title, String listTitle,
            int dialogType) {
        String ok = getBundleString(LanguageConstants.DIALOG_BUTTON_OK);
        String cancel = getBundleString(LanguageConstants.DIALOG_BUTTON_CANCEL);
        String currentProjectTitle = getBundleString(LanguageConstants.DIALOG_RUN_CURRENT_PROJECT_TITLE);
        String changeCurrentProjectButton = getBundleString(LanguageConstants.DIALOG_RUN_CHANGE_CURRENT_PROJECT_BUTTON);
        Caseless projectName = getProjectExplorer().getSelectedProject();
        String currentProjectLabel;
        if (projectName == null) {
            currentProjectLabel = getBundleString(LanguageConstants.DIALOG_RUN_ACTIVE_PROJECT_LABEL_NO_ACTIVE_PROJECT);
        } else {
            currentProjectLabel = getBundleString(LanguageConstants.DIALOG_RUN_CURRENT_PROJECT_LABEL);
            currentProjectLabel = PlaceholderUtil.replacePlaceholders(
                    currentProjectLabel, new Caseless[] { projectName });
        }

        RunDialog dialog = new RunDialog(getParentFrame(), true, this,
                dialogType);
        dialog.setTitle(title);
        dialog.setCurrentProjectTitle(currentProjectTitle);
        dialog.setChangeProjectButtonText(changeCurrentProjectButton);
        dialog.setCurrentProjectText(currentProjectLabel);
        dialog.setListTitle(listTitle);
        dialog.setOKButtonText(ok);
        dialog.setCancelButtonText(cancel);
        dialog.startDialog();
        // control locked until user clicks on OK, CANCEL or CLOSE button

        return dialog.getSelectedFile();
    }

    /**
     * TODO PENDING CHANGE! also to change (by transition): -
     * createNewProjectInstance
     */
    private String showCreateProjectDialog() {
        String title = getBundleString(LanguageConstants.DIALOG_CREATE_NEW_PROJECT_TITLE);
        String message = getBundleString(LanguageConstants.DIALOG_CREATE_NEW_PROJECT_MESSAGE);
        String ok = getBundleString(LanguageConstants.DIALOG_BUTTON_OK);
        String cancel = getBundleString(LanguageConstants.DIALOG_BUTTON_CANCEL);
        Object[] options = new Object[] { ok, cancel };

        String projectName = (String) JOptionPane.showInputDialog(
                getParentFrame(), message, title, JOptionPane.OK_CANCEL_OPTION,
                null, options, options[0]);
        /*
         * try { if(projectName != null &&
         * communicator.existsProject(projectName)) { return null; } } catch
         * (UniformAppletException e) { }
         */
        return projectName;
    }

    /**
     * Check if if resource is compilable. Also save opened editors.
     * 
     * @author Miro Bezjak
     */
    private class BeforeCompilationCheckCompilableAndSaveEditors extends
            VetoableResourceAdapter {
        @Override
        public void beforeResourceCompilation(Caseless projectName,
                Caseless fileName) throws ResourceVetoException {
            if (!resourceManager.isCompilable(projectName, fileName)) {
                String text = getBundleString(LanguageConstants.STATUSBAR_NOT_COMPILABLE);
                text = PlaceholderUtil.replacePlaceholders(text,
                        new Caseless[] { fileName, projectName });
                SystemLog.instance().addSystemMessage(text,
                        MessageType.INFORMATION);
                // veto compilation
                throw new ResourceVetoException();
            }
            boolean saved = editorManagerFactory.getAllAssociatedWithProject(
                    projectName).save(true);
            if (!saved) {
                throw new ResourceVetoException();
            }
            // List<IEditor> openedEditors = editorManager
            // .getOpenedEditorsThatHave(projectName);
            // String title =
            // getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_COMPILATION_TITLE);
            // String message =
            // getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_COMPILATION_MESSAGE);
            // boolean shouldContinue = editorManager.saveResourcesWithDialog(
            // openedEditors, title, message);
            // if (!shouldContinue) {
            // // veto compilation
            // throw new ResourceVetoException();
            // }
        }
    }

    /**
     * Echoes that a resource has been compiled.
     * 
     * @author Miro Bezjak
     */
    private class AfterCompilationEcho extends VetoableResourceAdapter {
        @Override
        public void resourceCompiled(Caseless projectName, Caseless fileName,
                CompilationResult result) {
            String text = getBundleString(LanguageConstants.STATUSBAR_COMPILED);
            text = PlaceholderUtil.replacePlaceholders(text, new Caseless[] {
                    fileName, projectName });
            SystemLog.instance()
                    .addSystemMessage(text, MessageType.INFORMATION);
        }
    }

    /**
     * Check if if resource is simulatable. Also save opened editors.
     * 
     * @author Miro Bezjak
     */
    private class BeforeSimulationCheckSimulatableAndSaveEditors extends
            VetoableResourceAdapter {
        @Override
        public void beforeResourceSimulation(Caseless projectName,
                Caseless fileName) throws ResourceVetoException {
            if (!resourceManager.isSimulatable(projectName, fileName)) {
                String text = getBundleString(LanguageConstants.STATUSBAR_NOT_SIMULATABLE);
                text = PlaceholderUtil.replacePlaceholders(text,
                        new Caseless[] { fileName, projectName });
                SystemLog.instance().addSystemMessage(text,
                        MessageType.INFORMATION);
                // veto simulation
                throw new ResourceVetoException();
            }
            boolean saved = editorManagerFactory.getAllAssociatedWithProject(
                    projectName).save(true);
            if (!saved) {
                throw new ResourceVetoException();
            }
            // List<IEditor> openedEditors = editorManager
            // .getOpenedEditorsThatHave(projectName);
            // String title =
            // getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_SIMULATION_TITLE);
            // String message =
            // getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_SIMULATION_MESSAGE);
            // boolean shouldContinue = editorManager.saveResourcesWithDialog(
            // openedEditors, title, message);
            // if (!shouldContinue) {
            // // veto simulation
            // throw new ResourceVetoException();
            // }
        }
    }

    /**
     * Echoes that a resource has been simulated.
     * 
     * @author Miro Bezjak
     */
    private class AfterSimulationEcho extends VetoableResourceAdapter {
        @Override
        public void resourceSimulated(Caseless projectName, Caseless fileName,
                SimulationResult result) {
            String text = getBundleString(LanguageConstants.STATUSBAR_SIMULATED);
            text = PlaceholderUtil.replacePlaceholders(text, new Caseless[] {
                    fileName, projectName });
            SystemLog.instance()
                    .addSystemMessage(text, MessageType.INFORMATION);
        }
    }

    // private class BeforeProjectCreationCheckExistence extends
    // VetoableResourceAdapter {
    // @Override
    // public void beforeProjectCreation(Caseless projectName)
    // throws ResourceVetoException {
    // boolean exists;
    // try {
    // exists = resourceManager.existsProject(projectName);
    // } catch (UniformAppletException e) {
    // String text =
    // getBundleString(LanguageConstants.STATUSBAR_CANT_CHECK_PROJECT_EXISTENCE);
    // text = PlaceholderUtil.replacePlaceholders(text,
    // new Caseless[] { projectName });
    // SystemLog.instance().addSystemMessage(text, MessageType.ERROR);
    // // veto project creation
    // throw new ResourceVetoException();
    // }
    // if (exists) {
    // String text =
    // getBundleString(LanguageConstants.STATUSBAR_EXISTS_PROJECT);
    // text = PlaceholderUtil.replacePlaceholders(text,
    // new Caseless[] { projectName });
    // SystemLog.instance().addSystemMessage(text,
    // MessageType.INFORMATION);
    // // veto project creation
    // throw new ResourceVetoException();
    // }
    //
    // }
    // }
    //
    // private class BeforeProjectCreationCheckCorrectName extends
    // VetoableResourceAdapter {
    // @Override
    // public void beforeProjectCreation(Caseless projectName)
    // throws ResourceVetoException {
    // if (!resourceManager.isCorrectProjectName(projectName)) {
    // String text =
    // getBundleString(LanguageConstants.STATUSBAR_NOT_CORRECT_PROJECT_NAME);
    // text = PlaceholderUtil.replacePlaceholders(text,
    // new Caseless[] { projectName });
    // SystemLog.instance().addSystemMessage(text,
    // MessageType.INFORMATION);
    // // veto project creation
    // throw new ResourceVetoException();
    // }
    // }
    // }

    /**
     * Echo that project was created.
     * 
     * @author Miro Bezjak
     */
    private class AfterProjectCreationEcho extends VetoableResourceAdapter {
        @Override
        public void projectCreated(Caseless projectName) {
            String text = getBundleString(LanguageConstants.STATUSBAR_PROJECT_CREATED);
            text = PlaceholderUtil.replacePlaceholders(text,
                    new Caseless[] { projectName });
            SystemLog.instance().addSystemMessage(text, MessageType.SUCCESSFUL);
        }
    }

    /**
     * Check existence of resource before creating it.
     * 
     * @author Miro Bezjak
     */
    private class BeforeResourceCreationCheckExistence extends
            VetoableResourceAdapter {
        @Override
        public void beforeResourceCreation(Caseless projectName,
                Caseless fileName, FileType type) throws ResourceVetoException {
            boolean exists;
            try {
                exists = resourceManager.existsFile(projectName, fileName);
            } catch (UniformAppletException e) {
                String text = getBundleString(LanguageConstants.STATUSBAR_CANT_CHECK_FILE_EXISTENCE);
                text = PlaceholderUtil.replacePlaceholders(text,
                        new Caseless[] { fileName });
                SystemLog.instance().addSystemMessage(text, MessageType.ERROR);
                // veto resource creation
                throw new ResourceVetoException();
            }
            if (exists) {
                String text = getBundleString(LanguageConstants.STATUSBAR_EXISTS_FILE);
                text = PlaceholderUtil.replacePlaceholders(text,
                        new Caseless[] { fileName, projectName });
                SystemLog.instance().addSystemMessage(text,
                        MessageType.INFORMATION);
                // veto resource creation
                throw new ResourceVetoException();
            }
        }
    }

    /**
     * Check if a resource has a correct name.
     * 
     * @author Miro Bezjak
     */
    private class BeforeResourceCreationCheckCorrectName extends
            VetoableResourceAdapter {
        @Override
        public void beforeResourceCreation(Caseless projectName,
                Caseless fileName, FileType type) throws ResourceVetoException {
            if (!resourceManager.isCorrectFileName(fileName)) {
                String text = getBundleString(LanguageConstants.STATUSBAR_NOT_CORRECT_FILE_NAME);
                text = PlaceholderUtil.replacePlaceholders(text,
                        new Caseless[] { fileName });
                SystemLog.instance().addSystemMessage(text,
                        MessageType.INFORMATION);
                // veto project creation
                throw new ResourceVetoException();
            }
        }
    }

    /**
     * Echo that resource was created.
     * 
     * @author Miro Bezjak
     */
    private class AfterResourceCreationEcho extends VetoableResourceAdapter {
        @Override
        public void resourceCreated(Caseless projectName, Caseless fileName,
                FileType type) {
            String text = getBundleString(LanguageConstants.STATUSBAR_FILE_CREATED);
            text = PlaceholderUtil.replacePlaceholders(text,
                    new Caseless[] { fileName });
            SystemLog.instance().addSystemMessage(text, MessageType.SUCCESSFUL);
        }
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
     * Echoes that a resource has been saved.
     * 
     * @author Miro Bezjak
     */
    private class ResourceSavedEcho extends VetoableResourceAdapter {
        @Override
        public void resourceSaved(Caseless projectName, Caseless fileName) {
            String text = bundle
                    .getString(LanguageConstants.STATUSBAR_FILE_SAVED);
            text = PlaceholderUtil.replacePlaceholders(text,
                    new Caseless[] { fileName });
            SystemLog.instance().addSystemMessage(text, MessageType.SUCCESSFUL);
        }
    }

    /**
     * Close editor after resource has been deleted.
     * 
     * @author Miro Bezjak
     */
    private class ResourceDeletedCloseEditor extends VetoableResourceAdapter {

        @Override
        public void resourceDeleted(Caseless projectName, Caseless fileName) {
            // TODO napravit ovo. kvagu kolko problema s tim.
        }
    }

}
