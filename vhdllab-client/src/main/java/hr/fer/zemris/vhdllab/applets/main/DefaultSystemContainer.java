package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.api.workspace.Workspace;
import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.constant.LanguageConstants;
import hr.fer.zemris.vhdllab.applets.main.dialog.RunDialog;
import hr.fer.zemris.vhdllab.applets.main.dialog.SaveDialog;
import hr.fer.zemris.vhdllab.applets.main.event.ResourceVetoException;
import hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceAdapter;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.applets.simulations.WaveAppletMetadata;
import hr.fer.zemris.vhdllab.applets.view.compilation.CompilationErrorsView;
import hr.fer.zemris.vhdllab.applets.view.simulation.SimulationErrorsView;
import hr.fer.zemris.vhdllab.client.core.bundle.ResourceBundleProvider;
import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.ResultTarget;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.client.core.log.SystemMessage;
import hr.fer.zemris.vhdllab.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.editor.impl.WizardRegistry;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.ProjectIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializationListener;
import hr.fer.zemris.vhdllab.utilities.PlaceholderUtil;

import java.awt.Frame;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

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
    public void init() throws UniformAppletException {
        bundle = ResourceBundleProvider
                .getBundle(LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN);
        resourceManager
                .addVetoableResourceListener(new BeforeCompilationCheckCompilableAndSaveEditors());
        resourceManager.addVetoableResourceListener(new AfterCompilationEcho());
        resourceManager
                .addVetoableResourceListener(new AfterCompilationOpenView());
        resourceManager
                .addVetoableResourceListener(new BeforeSimulationCheckSimulatableAndSaveEditors());
        resourceManager.addVetoableResourceListener(new AfterSimulationEcho());
        resourceManager
                .addVetoableResourceListener(new AfterSimulationOpenView());
        resourceManager
                .addVetoableResourceListener(new AfterSimulationOpenEditor());
        resourceManager
                .addVetoableResourceListener(new BeforeProjectCreationCheckExistence());
        resourceManager
                .addVetoableResourceListener(new BeforeProjectCreationCheckCorrectName());
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
    public void dispose() throws UniformAppletException {
        editorManagerFactory.getAll().save(false);

        StringBuilder sb = new StringBuilder(2000);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        for (SystemMessage m : SystemLog.instance().getSystemMessages()) {
            sb.append(formatter.format(m.getDate())).append("\t\t").append(
                    m.getContent()).append("\n");
        }
        if (sb.length() != 0) {
            sb.append("---------------------------------\n\n\n");
        }
        for (SystemMessage m : SystemLog.instance().getErrorMessages()) {
            sb.append(formatter.format(m.getDate())).append("\t\t").append(
                    m.getContent()).append("\n\n");
        }
        if (sb.length() != 0) {
            resourceManager.saveErrorMessage(sb.toString());
        }

        SystemLog.instance().removeAllSystemLogListeners();
        resourceManager.removeAllVetoableResourceListeners();

        resourceManager = null;
        bundle = null;
    }

    /* ISystemContainer METHODS */

    /* COMPILE RESOURCE METHODS */

    /*
     * (non-Javadoc)
     * 
     * @seehr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#
     * compileWithDialog()
     */
    @Override
    public boolean compileWithDialog() {
        FileIdentifier file = showCompilationRunDialog();
        return compile(file);
    }

    /*
     * (non-Javadoc)
     * 
     * @seehr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#
     * compileLastHistoryResult()
     */
    @Override
    public boolean compileLastHistoryResult() {
        SystemLog log = SystemLog.instance();
        if (log.compilationHistoryIsEmpty()) {
            return compileWithDialog();
        } else {
            ResultTarget<CompilationResult> resultTarget = log
                    .getLastCompilationResultTarget();
            return compile(resultTarget.getResource());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#compile
     * (hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier)
     */
    @Override
    public boolean compile(FileIdentifier file) {
        if (file == null) {
            return false;
        }
        return compile(file.getProjectName(), file.getFileName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#compile
     * (java.lang.String, java.lang.String)
     */
    @Override
    public boolean compile(Caseless projectName, Caseless fileName) {
        if (projectName == null || fileName == null) {
            return false;
        }
        CompilationResult result;
        try {
            result = resourceManager.compile(projectName, fileName);
        } catch (UniformAppletException e) {
            String text = getBundleString(LanguageConstants.STATUSBAR_CANT_COMPILE);
            text = PlaceholderUtil.replacePlaceholders(text, new Caseless[] {
                    fileName, projectName });
            SystemLog.instance().addSystemMessage(text, MessageType.ERROR);
            return false;
        }
        return result != null;
    }

    /* SIMULATE RESOURCE METHODS */

    /*
     * (non-Javadoc)
     * 
     * @seehr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#
     * simulateWithDialog()
     */
    @Override
    public boolean simulateWithDialog() {
        FileIdentifier file = showSimulationRunDialog();
        return simulate(file);
    }

    /*
     * (non-Javadoc)
     * 
     * @seehr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#
     * simulateLastHistoryResult()
     */
    @Override
    public boolean simulateLastHistoryResult() {
        SystemLog log = SystemLog.instance();
        if (log.simulationHistoryIsEmpty()) {
            return simulateWithDialog();
        } else {
            ResultTarget<SimulationResult> resultTarget = log
                    .getLastSimulationResultTarget();
            return simulate(resultTarget.getResource());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#simulate
     * (hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier)
     */
    @Override
    public boolean simulate(FileIdentifier file) {
        if (file == null) {
            return false;
        }
        return simulate(file.getProjectName(), file.getFileName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#simulate
     * (java.lang.String, java.lang.String)
     */
    @Override
    public boolean simulate(Caseless projectName, Caseless fileName) {
        if (projectName == null || fileName == null) {
            return false;
        }
        SimulationResult result;
        try {
            result = resourceManager.simulate(projectName, fileName);
        } catch (UniformAppletException e) {
            String text = getBundleString(LanguageConstants.STATUSBAR_CANT_SIMULATE);
            text = PlaceholderUtil.replacePlaceholders(text, new Caseless[] {
                    fileName, projectName });
            SystemLog.instance().addSystemMessage(text, MessageType.ERROR);
            return false;
        }
        return result != null;
    }

    /* RESOURCE MANIPULATION METHODS */

    /*
     * (non-Javadoc)
     * 
     * @seehr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#
     * createNewProjectInstance()
     */
    @Override
    public boolean createNewProjectInstance() {
        String projectName = showCreateProjectDialog();
        if (projectName == null) {
            return false;
        }
        try {
            return resourceManager.createNewProject(new Caseless(projectName));
        } catch (UniformAppletException e) {
            String text = getBundleString(LanguageConstants.STATUSBAR_CANT_CREATE_PROJECT);
            text = PlaceholderUtil.replacePlaceholders(text,
                    new String[] { projectName });
            SystemLog.instance().addSystemMessage(text, MessageType.ERROR);
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seehr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#
     * createNewFileInstance(java.lang.String)
     */
    @Override
    public boolean createNewFileInstance(FileType type) {
        if (type == null) {
            throw new NullPointerException("File type cant be null");
        }
        Caseless projectName = getSelectedProject();
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

    /*
     * (non-Javadoc)
     * 
     * @seehr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#
     * getSelectedProject()
     */
    @Override
    public Caseless getSelectedProject() {
        return getProjectExplorer().getSelectedProject();
    }

    /*
     * (non-Javadoc)
     * 
     * @seehr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#
     * getSelectedFile()
     */
    @Override
    public FileIdentifier getSelectedFile() {
        return getProjectExplorer().getSelectedFile();
    }

    /* MANAGER GETTER METHODS */

    /*
     * (non-Javadoc)
     * 
     * @seehr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#
     * getResourceManager()
     */
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

    private FileIdentifier showCompilationRunDialog() {
        String title = getBundleString(LanguageConstants.DIALOG_RUN_COMPILATION_TITLE);
        String listTitle = getBundleString(LanguageConstants.DIALOG_RUN_COMPILATION_LIST_TITLE);
        FileIdentifier file = showRunDialog(title, listTitle,
                RunDialog.COMPILATION_TYPE);
        return file;
    }

    private FileIdentifier showSimulationRunDialog() {
        String title = getBundleString(LanguageConstants.DIALOG_RUN_SIMULATION_TITLE);
        String listTitle = getBundleString(LanguageConstants.DIALOG_RUN_SIMULATION_LIST_TITLE);
        FileIdentifier file = showRunDialog(title, listTitle,
                RunDialog.SIMULATION_TYPE);
        return file;
    }

    /**
     * TODO PENDING CHANGE! also to change (by transition): -
     * saveResourcesWithSaveDialog
     */
    @Override
    public List<Editor> showSaveDialog(String title, String message,
            List<Editor> editorsToBeSaved) {
        if (editorsToBeSaved.isEmpty())
            return Collections.emptyList();
        String selectAll = getBundleString(LanguageConstants.DIALOG_BUTTON_SELECT_ALL);
        String deselectAll = getBundleString(LanguageConstants.DIALOG_BUTTON_DESELECT_ALL);
        String ok = getBundleString(LanguageConstants.DIALOG_BUTTON_OK);
        String cancel = getBundleString(LanguageConstants.DIALOG_BUTTON_CANCEL);
        String alwaysSave = getBundleString(LanguageConstants.DIALOG_SAVE_CHECKBOX_ALWAYS_SAVE_RESOURCES);

        SaveDialog dialog = new SaveDialog(getParentFrame(), true);
        dialog.setTitle(title);
        dialog.setText(message);
        dialog.setOKButtonText(ok);
        dialog.setCancelButtonText(cancel);
        dialog.setSelectAllButtonText(selectAll);
        dialog.setDeselectAllButtonText(deselectAll);
        dialog.setAlwaysSaveCheckBoxText(alwaysSave);
        for (Editor editor : editorsToBeSaved) {
            dialog.addItem(true, editor);
        }
        dialog.startDialog();
        // control locked until user clicks on OK, CANCEL or CLOSE button

        boolean shouldAutoSave = dialog.shouldAlwaysSaveResources();
        if (shouldAutoSave) {
            Preferences pref = Preferences
                    .userNodeForPackage(DefaultSystemContainer.class);
            pref.put(UserFileConstants.SYSTEM_ALWAYS_SAVE_RESOURCES, String
                    .valueOf(shouldAutoSave));
        }
        int option = dialog.getOption();
        if (option != SaveDialog.OK_OPTION)
            return null;
        else
            return dialog.getSelectedResources();
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
        Caseless projectName = getSelectedProject();
        String currentProjectLabel;
        if (projectName == null) {
            currentProjectLabel = getBundleString(LanguageConstants.DIALOG_RUN_ACTIVE_PROJECT_LABEL_NO_ACTIVE_PROJECT);
        } else {
            currentProjectLabel = getBundleString(LanguageConstants.DIALOG_RUN_CURRENT_PROJECT_LABEL);
            currentProjectLabel = PlaceholderUtil.replacePlaceholders(
                    currentProjectLabel, new Caseless[] { projectName });
        }

        RunDialog dialog = new RunDialog(getParentFrame(), true, this, dialogType);
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

        // String projectName = (String) JOptionPane.showInputDialog(this,
        // message, title, JOptionPane.OK_CANCEL_OPTION, null, options,
        // options[0]);
        String projectName = JOptionPane.showInputDialog(getParentFrame(), message,
                title, JOptionPane.OK_CANCEL_OPTION);
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
            boolean saved = editorManagerFactory.getAllAssociatedWithProject(projectName).save(true);
            if(!saved) {
                throw new ResourceVetoException();
            }
//            List<IEditor> openedEditors = editorManager
//                    .getOpenedEditorsThatHave(projectName);
//            String title = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_COMPILATION_TITLE);
//            String message = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_COMPILATION_MESSAGE);
//            boolean shouldContinue = editorManager.saveResourcesWithDialog(
//                    openedEditors, title, message);
//            if (!shouldContinue) {
//                // veto compilation
//                throw new ResourceVetoException();
//            }
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
     * First open view for displaying compilation result and then log this
     * compilation is system log.
     * 
     * @author Miro Bezjak
     */
    private class AfterCompilationOpenView extends VetoableResourceAdapter {
        @Override
        public void resourceCompiled(Caseless projectName, Caseless fileName,
                CompilationResult result) {
            viewManager.select(CompilationErrorsView.class);

            FileIdentifier resource = new FileIdentifier(projectName, fileName);
            ResultTarget<CompilationResult> resultTarget = new ResultTarget<CompilationResult>(
                    resource, result);
            SystemLog.instance().addCompilationResultTarget(resultTarget);
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
            boolean saved = editorManagerFactory.getAllAssociatedWithProject(projectName).save(true);
            if(!saved) {
                throw new ResourceVetoException();
            }
//            List<IEditor> openedEditors = editorManager
//                    .getOpenedEditorsThatHave(projectName);
//            String title = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_SIMULATION_TITLE);
//            String message = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_SIMULATION_MESSAGE);
//            boolean shouldContinue = editorManager.saveResourcesWithDialog(
//                    openedEditors, title, message);
//            if (!shouldContinue) {
//                // veto simulation
//                throw new ResourceVetoException();
//            }
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

    /**
     * First open view for displaying simulation result and then log this
     * simulation is system log.
     * 
     * @author Miro Bezjak
     */
    private class AfterSimulationOpenView extends VetoableResourceAdapter {
        @Override
        public void resourceSimulated(Caseless projectName, Caseless fileName,
                SimulationResult result) {
            viewManager.select(SimulationErrorsView.class);

            FileIdentifier resource = new FileIdentifier(projectName, fileName);
            ResultTarget<SimulationResult> resultTarget = new ResultTarget<SimulationResult>(
                    resource, result);
            SystemLog.instance().addSimulationResultTarget(resultTarget);
        }
    }

    /**
     * Opens a simulations editor to present a simulation result if simulation
     * is successful.
     * 
     * @author Miro Bezjak
     */
    private class AfterSimulationOpenEditor extends VetoableResourceAdapter {
        @Override
        public void resourceSimulated(Caseless projectName, Caseless fileName,
                SimulationResult result) {
            if (result.getWaveform() != null
                    && !result.getWaveform().equals("")) {
                ProjectInfo project = mapper.getProject(new ProjectIdentifier(
                        projectName));
                editorManagerFactory
                        .get(
                                new EditorIdentifier(new WaveAppletMetadata(),
                                        new FileInfo(FileType.SIMULATION,
                                                fileName, result.getWaveform(),
                                                project.getId()))).open();
            }
        }
    }

    /**
     * Check existence of project before creating it.
     * 
     * @author Miro Bezjak
     */
    private class BeforeProjectCreationCheckExistence extends
            VetoableResourceAdapter {
        @Override
        public void beforeProjectCreation(Caseless projectName)
                throws ResourceVetoException {
            boolean exists;
            try {
                exists = resourceManager.existsProject(projectName);
            } catch (UniformAppletException e) {
                String text = getBundleString(LanguageConstants.STATUSBAR_CANT_CHECK_PROJECT_EXISTENCE);
                text = PlaceholderUtil.replacePlaceholders(text,
                        new Caseless[] { projectName });
                SystemLog.instance().addSystemMessage(text, MessageType.ERROR);
                // veto project creation
                throw new ResourceVetoException();
            }
            if (exists) {
                String text = getBundleString(LanguageConstants.STATUSBAR_EXISTS_PROJECT);
                text = PlaceholderUtil.replacePlaceholders(text,
                        new Caseless[] { projectName });
                SystemLog.instance().addSystemMessage(text,
                        MessageType.INFORMATION);
                // veto project creation
                throw new ResourceVetoException();
            }

        }
    }

    /**
     * Check if a project has a correct name.
     * 
     * @author Miro Bezjak
     */
    private class BeforeProjectCreationCheckCorrectName extends
            VetoableResourceAdapter {
        @Override
        public void beforeProjectCreation(Caseless projectName)
                throws ResourceVetoException {
            if (!resourceManager.isCorrectProjectName(projectName)) {
                String text = getBundleString(LanguageConstants.STATUSBAR_NOT_CORRECT_PROJECT_NAME);
                text = PlaceholderUtil.replacePlaceholders(text,
                        new Caseless[] { projectName });
                SystemLog.instance().addSystemMessage(text,
                        MessageType.INFORMATION);
                // veto project creation
                throw new ResourceVetoException();
            }
        }
    }

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
            hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier i = new hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier(projectName, fileName);
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
