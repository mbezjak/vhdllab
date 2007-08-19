package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.IStatusBar;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageType;
import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.ComponentIdentifierFactory;
import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier;
import hr.fer.zemris.vhdllab.applets.main.conf.ComponentConfiguration;
import hr.fer.zemris.vhdllab.applets.main.conf.ComponentConfigurationParser;
import hr.fer.zemris.vhdllab.applets.main.conf.EditorProperties;
import hr.fer.zemris.vhdllab.applets.main.constant.ComponentTypes;
import hr.fer.zemris.vhdllab.applets.main.constant.LanguageConstants;
import hr.fer.zemris.vhdllab.applets.main.dialog.RunDialog;
import hr.fer.zemris.vhdllab.applets.main.dialog.SaveDialog;
import hr.fer.zemris.vhdllab.applets.main.event.ResourceVetoException;
import hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceAdapter;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentProvider;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.applets.main.model.ResultTarget;
import hr.fer.zemris.vhdllab.applets.main.model.SystemMessage;
import hr.fer.zemris.vhdllab.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;
import hr.fer.zemris.vhdllab.preferences.PropertyAccessException;
import hr.fer.zemris.vhdllab.utilities.PlaceholderUtil;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;

import java.awt.Frame;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

/**
 * This is a default implementation of {@link ISystemContainer} interface.
 * <p>
 * To use this implementation you need to setup it up properly. This is how:
 * </p>
 * <blockquote><code>
 * DefaultSystemContainer container = new DefaultSystemContainer(myResourceManager, mySystemLog, myComponentProvider, parentFrame);<br/>
 * container.setComponentStorage(myComponentStorage);<br/>
 * container.setEditorManager(myEditorManager);<br/>
 * container.setViewManager(myViewManager);<br/>
 * container.setViewStrage(myViewStorage);<br/>
 * container.init();<br/>
 * </code></blockquote>
 * <p>
 * To dispose of this system container simply invoke {@link #dispose()} method.
 * </p>
 * <p>
 * Note that this implementation requires that a GUI it setup before system
 * container initialization.
 * </p>
 * 
 * @author Miro Bezjak
 */
public class DefaultSystemContainer implements ISystemContainer {

	/**
	 * A resource manager.
	 */
	private IResourceManager resourceManager;
	/**
	 * A system log.
	 */
	private ISystemLog systemLog;
	/**
	 * A Component configuration.
	 */
	private ComponentConfiguration configuration;
	/**
	 * A resource bundle of using language in error reporting.
	 */
	private ResourceBundle bundle;
	/**
	 * A parent frame for enabling modal dialogs.
	 */
	private Frame parentFrame;
	/**
	 * A component provider.
	 */
	private IComponentProvider componentProvider;
	/**
	 * A component storage.
	 */
	private IComponentStorage componentStorage;
	/**
	 * An editor manager.
	 */
	private IEditorManager editorManager;
	/**
	 * A view manager.
	 */
	private IViewManager viewManager;

	/**
	 * Constructs a default system container.
	 * 
	 * @param resourceManager
	 *            a resource manager
	 * @param systemLog
	 *            a system log
	 * @param componentProvider
	 *            a component provider
	 * @param parentFrame
	 *            a parent frame for enabling modal dialogs
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public DefaultSystemContainer(IResourceManager resourceManager,
			ISystemLog systemLog, IComponentProvider componentProvider,
			Frame parentFrame) {
		if (resourceManager == null) {
			throw new NullPointerException("Resource manager cant be null");
		}
		if (systemLog == null) {
			throw new NullPointerException("System log cant be null");
		}
		if (componentProvider == null) {
			throw new NullPointerException("Component provider cant be null");
		}
		if (parentFrame == null) {
			throw new NullPointerException("Parent frame cant be null");
		}
		this.resourceManager = resourceManager;
		this.systemLog = systemLog;
		this.componentProvider = componentProvider;
		this.parentFrame = parentFrame;
	}

	/**
	 * Setter for a component storage.
	 * 
	 * @param componentStorage
	 *            a component storage
	 */
	public void setComponentStorage(IComponentStorage componentStorage) {
		this.componentStorage = componentStorage;
	}

	/**
	 * Setter for an editor manager.
	 * 
	 * @param editorManager
	 *            an editor manager
	 */
	public void setEditorManager(IEditorManager editorManager) {
		this.editorManager = editorManager;
	}

	/**
	 * Setter for a view manager.
	 * 
	 * @param viewManager
	 *            a view manager
	 */
	public void setViewManager(IViewManager viewManager) {
		this.viewManager = viewManager;
	}

	/**
	 * Initializes a system container.
	 * 
	 * @throws UniformAppletException
	 *             if exceptional condition occurs
	 */
	public void init() throws UniformAppletException {
		configuration = ComponentConfigurationParser.getConfiguration();
		bundle = getResourceBundle(LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN);
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

		String data;
		data = getProperty(UserFileConstants.SYSTEM_OPENED_EDITORS);
		List<FileIdentifier> files = SerializationUtil
				.deserializeEditorInfo(data);
		for (FileIdentifier f : files) {
			IComponentIdentifier<FileIdentifier> identifier = ComponentIdentifierFactory
					.createFileEditorIdentifier(f);
			editorManager.openEditorByResource(identifier);
		}

		data = getProperty(UserFileConstants.SYSTEM_OPENED_VIEWS);
		List<String> views = SerializationUtil.deserializeViewInfo(data);
		for (String s : views) {
			IComponentIdentifier<?> identifier = ComponentIdentifierFactory
					.createViewIdentifier(s);
			viewManager.openView(identifier);
		}
	}

	/**
	 * Disposes any resources used by system container.
	 * 
	 * @throws UniformAppletException
	 *             if exceptional condition occurs
	 */
	public void dispose() throws UniformAppletException {
		editorManager.saveAllEditors();
		String data;
		data = SerializationUtil.serializeEditorInfo(editorManager
				.getAllOpenedEditors());
		setProperty(UserFileConstants.SYSTEM_OPENED_EDITORS, data);

		List<String> views = new ArrayList<String>();
		for (IView v : viewManager.getAllOpenedViews()) {
			IComponentIdentifier<?> id = viewManager.getIdentifierFor(v);
			views.add(id.getComponentType());
		}
		data = SerializationUtil.serializeViewInfo(views);
		setProperty(UserFileConstants.SYSTEM_OPENED_VIEWS, data);

		StringBuilder sb = new StringBuilder(2000);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		for (SystemMessage m : systemLog.getErrorMessages()) {
			sb.append(formatter.format(m.getDate())).append("\t\t").append(
					m.getContent()).append("\n\n");
		}
		resourceManager.saveErrorMessage(sb.toString());

		systemLog.removeAllSystemLogListeners();
		resourceManager.removeAllVetoableResourceListeners();
		getPreferences().removeAllPropertyListeners();

		resourceManager = null;
		componentProvider = null;
		componentStorage = null;
		editorManager = null;
		viewManager = null;
		bundle = null;
		parentFrame = null;
	}

	/* ISystemContainer METHODS */

	/* COMPILE RESOURCE METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#compileWithDialog()
	 */
	@Override
	public boolean compileWithDialog() {
		FileIdentifier file = showCompilationRunDialog();
		return compile(file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#compileLastHistoryResult()
	 */
	@Override
	public boolean compileLastHistoryResult() {
		if (systemLog.compilationHistoryIsEmpty()) {
			return compileWithDialog();
		} else {
			ResultTarget<CompilationResult> resultTarget = systemLog
					.getLastCompilationResultTarget();
			return compile(resultTarget.getResource());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#compile(hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier)
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#compile(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	@Override
	public boolean compile(IEditor editor) {
		if (editor == null) {
			return false;
		}
		String projectName = editor.getProjectName();
		String fileName = editor.getFileName();
		return compile(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#compile(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean compile(String projectName, String fileName) {
		if (projectName == null || fileName == null) {
			return false;
		}
		CompilationResult result;
		try {
			result = resourceManager.compile(projectName, fileName);
		} catch (UniformAppletException e) {
			String text = getBundleString(LanguageConstants.STATUSBAR_CANT_COMPILE);
			text = PlaceholderUtil.replacePlaceholders(text, new String[] {
					fileName, projectName });
			echoStatusText(text, MessageType.ERROR);
			return false;
		}
		return result != null;
	}

	/* SIMULATE RESOURCE METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#simulateWithDialog()
	 */
	@Override
	public boolean simulateWithDialog() {
		FileIdentifier file = showSimulationRunDialog();
		return simulate(file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#simulateLastHistoryResult()
	 */
	@Override
	public boolean simulateLastHistoryResult() {
		if (systemLog.simulationHistoryIsEmpty()) {
			return simulateWithDialog();
		} else {
			ResultTarget<SimulationResult> resultTarget = systemLog
					.getLastSimulationResultTarget();
			return simulate(resultTarget.getResource());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#simulate(hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier)
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#simulate(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	@Override
	public boolean simulate(IEditor editor) {
		if (editor == null) {
			return false;
		}
		String projectName = editor.getProjectName();
		String fileName = editor.getFileName();
		return simulate(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#simulate(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean simulate(String projectName, String fileName) {
		if (projectName == null || fileName == null) {
			return false;
		}
		SimulationResult result;
		try {
			result = resourceManager.simulate(projectName, fileName);
		} catch (UniformAppletException e) {
			String text = getBundleString(LanguageConstants.STATUSBAR_CANT_SIMULATE);
			text = PlaceholderUtil.replacePlaceholders(text, new String[] {
					fileName, projectName });
			echoStatusText(text, MessageType.ERROR);
			return false;
		}
		return result != null;
	}

	/* RESOURCE MANIPULATION METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#createNewProjectInstance()
	 */
	@Override
	public boolean createNewProjectInstance() {
		String projectName = showCreateProjectDialog();
		if (projectName == null) {
			return false;
		}
		try {
			return resourceManager.createNewProject(projectName);
		} catch (UniformAppletException e) {
			String text = getBundleString(LanguageConstants.STATUSBAR_CANT_CREATE_PROJECT);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { projectName });
			echoStatusText(text, MessageType.ERROR);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#createNewFileInstance(java.lang.String)
	 */
	@Override
	public boolean createNewFileInstance(String type) {
		if (type == null) {
			throw new NullPointerException("File type cant be null");
		}
		String projectName = getSelectedProject();
		if (projectName == null) {
			String text = getBundleString(LanguageConstants.STATUSBAR_NO_SELECTED_PROJECT);
			echoStatusText(text, MessageType.INFORMATION);
			return false;
		}
		IWizard wizard = getNewEditorInstanceByFileType(type).getWizard();
		FileContent content = initWizard(wizard, projectName);
		if (content == null) {
			// user canceled or no wizard for such editor
			return false;
		}
		projectName = content.getProjectName();
		String fileName = content.getFileName();
		String data = content.getContent();
		try {
			return resourceManager.createNewResource(projectName, fileName,
					type, data);
		} catch (UniformAppletException e) {
			String text = getBundleString(LanguageConstants.STATUSBAR_CANT_CREATE_FILE);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { fileName });
			echoStatusText(text, MessageType.ERROR);
			return false;
		}
	}

	private IEditor getNewEditorInstanceByFileType(String type) {
		EditorProperties ep = configuration.getEditorPropertiesByFileType(type);
		String clazz = ep.getClazz();
		IEditor editor;
		try {
			editor = (IEditor) Class.forName(clazz).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getSelectedProject()
	 */
	@Override
	public String getSelectedProject() {
		IComponentIdentifier<?> identifier = ComponentIdentifierFactory
				.createProjectExplorerIdentifier();
		if (!viewManager.isViewOpened(identifier)) {
			return null;
		} else {
			IView view = viewManager.getOpenedView(identifier);
			return view.asInterface(IProjectExplorer.class)
					.getSelectedProject();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getSelectedFile()
	 */
	@Override
	public FileIdentifier getSelectedFile() {
		IComponentIdentifier<?> identifier = ComponentIdentifierFactory
				.createProjectExplorerIdentifier();
		if (!viewManager.isViewOpened(identifier)) {
			return null;
		} else {
			IView view = viewManager.getOpenedView(identifier);
			return view.asInterface(IProjectExplorer.class).getSelectedFile();
		}
	}

	/* PREFERENCES AND RESOURCE BUNDLE METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getEditorManager()
	 */
	@Override
	public IEditorManager getEditorManager() {
		return editorManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getViewManager()
	 */
	@Override
	public IViewManager getViewManager() {
		return viewManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getResourceManager()
	 */
	@Override
	public IResourceManager getResourceManager() {
		return resourceManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getSystemLog()
	 */
	@Override
	public ISystemLog getSystemLog() {
		return systemLog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getPreferences()
	 */
	@Override
	public IUserPreferences getPreferences() {
		return resourceManager.getPreferences();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String name) {
		IUserPreferences preferences = getPreferences();
		String data;
		try {
			data = preferences.getProperty(name);
		} catch (PropertyAccessException e) {
			// try again
			try {
				data = preferences.getProperty(name);
			} catch (PropertyAccessException ex) {
				// report problems
				ex.printStackTrace();
				data = null;
			}
		}
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#setProperty(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void setProperty(String name, String data) {
		IUserPreferences preferences = getPreferences();
		try {
			preferences.setProperty(name, data);
		} catch (PropertyAccessException e) {
			// try again
			try {
				preferences.setProperty(name, data);
			} catch (PropertyAccessException ex) {
				// report problems
				ex.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getResourceBundle(java.lang.String)
	 */
	@Override
	public ResourceBundle getResourceBundle(String baseName) {
		return resourceManager.getResourceBundle(baseName);
	}

	/* COMPONENT PROVIDER METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getComponentProvider()
	 */
	@Override
	public IComponentProvider getComponentProvider() {
		return componentProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getStatusBar()
	 */
	@Override
	public IStatusBar getStatusBar() {
		return componentProvider.getStatusBar();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#echoStatusText(java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageType)
	 */
	@Override
	public void echoStatusText(String text, MessageType type) {
		systemLog.addSystemMessage(new SystemMessage(text, type));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#echoErrorMessage(java.lang.Throwable)
	 */
	@Override
	public void echoErrorMessage(Throwable ex) {
		SystemMessage message = new SystemMessage(ex);
		systemLog.addErrorMessage(message);
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
	private FileContent initWizard(IWizard wizard, String projectName) {
		if (wizard == null) {
			throw new NullPointerException("Wizard cant be null");
		}
		// Initialization of a wizard
		wizard.setSystemContainer(this);
		FileContent content = wizard.getInitialFileContent(parentFrame,
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
	public List<IEditor> showSaveDialog(String title, String message,
			List<IEditor> editorsToBeSaved) {
		if (editorsToBeSaved.isEmpty())
			return Collections.emptyList();
		String selectAll = getBundleString(LanguageConstants.DIALOG_BUTTON_SELECT_ALL);
		String deselectAll = getBundleString(LanguageConstants.DIALOG_BUTTON_DESELECT_ALL);
		String ok = getBundleString(LanguageConstants.DIALOG_BUTTON_OK);
		String cancel = getBundleString(LanguageConstants.DIALOG_BUTTON_CANCEL);
		String alwaysSave = getBundleString(LanguageConstants.DIALOG_SAVE_CHECKBOX_ALWAYS_SAVE_RESOURCES);

		SaveDialog dialog = new SaveDialog(parentFrame, true);
		dialog.setTitle(title);
		dialog.setText(message);
		dialog.setOKButtonText(ok);
		dialog.setCancelButtonText(cancel);
		dialog.setSelectAllButtonText(selectAll);
		dialog.setDeselectAllButtonText(deselectAll);
		dialog.setAlwaysSaveCheckBoxText(alwaysSave);
		for (IEditor editor : editorsToBeSaved) {
			dialog.addItem(true, editor);
		}
		dialog.startDialog();
		// control locked until user clicks on OK, CANCEL or CLOSE button

		boolean shouldAutoSave = dialog.shouldAlwaysSaveResources();
		if (shouldAutoSave) {
			setProperty(UserFileConstants.SYSTEM_ALWAYS_SAVE_RESOURCES, String
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
		String projectName = getSelectedProject();
		String currentProjectLabel;
		if (projectName == null) {
			currentProjectLabel = getBundleString(LanguageConstants.DIALOG_RUN_ACTIVE_PROJECT_LABEL_NO_ACTIVE_PROJECT);
		} else {
			currentProjectLabel = getBundleString(LanguageConstants.DIALOG_RUN_CURRENT_PROJECT_LABEL);
			currentProjectLabel = PlaceholderUtil.replacePlaceholders(
					currentProjectLabel, new String[] { projectName });
		}

		RunDialog dialog = new RunDialog(parentFrame, true, this, dialogType);
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
		String projectName = JOptionPane.showInputDialog(parentFrame, message,
				title, JOptionPane.OK_CANCEL_OPTION);
		/*
		 * try { if(projectName != null &&
		 * communicator.existsProject(projectName)) { return null; } } catch
		 * (UniformAppletException e) { }
		 */
		if (projectName.equals("")) {
			projectName = null;
		}
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
		public void beforeResourceCompilation(String projectName,
				String fileName) throws ResourceVetoException {
			if (!resourceManager.isCompilable(projectName, fileName)) {
				String text = getBundleString(LanguageConstants.STATUSBAR_NOT_COMPILABLE);
				text = PlaceholderUtil.replacePlaceholders(text, new String[] {
						fileName, projectName });
				echoStatusText(text, MessageType.INFORMATION);
				// veto compilation
				throw new ResourceVetoException();
			}
			List<IEditor> openedEditors = editorManager
					.getOpenedEditorsThatHave(projectName);
			String title = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_COMPILATION_TITLE);
			String message = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_COMPILATION_MESSAGE);
			boolean shouldContinue = editorManager.saveResourcesWithDialog(
					openedEditors, title, message);
			if (!shouldContinue) {
				// veto compilation
				throw new ResourceVetoException();
			}
		}
	}

	/**
	 * Echoes that a resource has been compiled.
	 * 
	 * @author Miro Bezjak
	 */
	private class AfterCompilationEcho extends VetoableResourceAdapter {
		@Override
		public void resourceCompiled(String projectName, String fileName,
				CompilationResult result) {
			String text = getBundleString(LanguageConstants.STATUSBAR_COMPILED);
			text = PlaceholderUtil.replacePlaceholders(text, new String[] {
					fileName, projectName });
			echoStatusText(text, MessageType.INFORMATION);
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
		public void resourceCompiled(String projectName, String fileName,
				CompilationResult result) {
			IComponentIdentifier<?> identifier = ComponentIdentifierFactory
					.createViewIdentifier(ComponentTypes.VIEW_COMPILATION_ERRORS);
			viewManager.openView(identifier);

			FileIdentifier resource = new FileIdentifier(projectName, fileName);
			ResultTarget<CompilationResult> resultTarget = new ResultTarget<CompilationResult>(
					resource, result);
			systemLog.addCompilationResultTarget(resultTarget);
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
		public void beforeResourceSimulation(String projectName, String fileName)
				throws ResourceVetoException {
			if (!resourceManager.isSimulatable(projectName, fileName)) {
				String text = getBundleString(LanguageConstants.STATUSBAR_NOT_SIMULATABLE);
				text = PlaceholderUtil.replacePlaceholders(text, new String[] {
						fileName, projectName });
				echoStatusText(text, MessageType.INFORMATION);
				// veto simulation
				throw new ResourceVetoException();
			}
			List<IEditor> openedEditors = editorManager
					.getOpenedEditorsThatHave(projectName);
			String title = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_SIMULATION_TITLE);
			String message = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_SIMULATION_MESSAGE);
			boolean shouldContinue = editorManager.saveResourcesWithDialog(
					openedEditors, title, message);
			if (!shouldContinue) {
				// veto simulation
				throw new ResourceVetoException();
			}

		}
	}

	/**
	 * Echoes that a resource has been simulated.
	 * 
	 * @author Miro Bezjak
	 */
	private class AfterSimulationEcho extends VetoableResourceAdapter {
		@Override
		public void resourceSimulated(String projectName, String fileName,
				SimulationResult result) {
			String text = getBundleString(LanguageConstants.STATUSBAR_SIMULATED);
			text = PlaceholderUtil.replacePlaceholders(text, new String[] {
					fileName, projectName });
			echoStatusText(text, MessageType.INFORMATION);
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
		public void resourceSimulated(String projectName, String fileName,
				SimulationResult result) {
			IComponentIdentifier<?> identifier = ComponentIdentifierFactory
					.createViewIdentifier(ComponentTypes.VIEW_SIMULATION_ERRORS);
			viewManager.openView(identifier);

			FileIdentifier resource = new FileIdentifier(projectName, fileName);
			ResultTarget<SimulationResult> resultTarget = new ResultTarget<SimulationResult>(
					resource, result);
			systemLog.addSimulationResultTarget(resultTarget);
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
		public void resourceSimulated(String projectName, String fileName,
				SimulationResult result) {
			if (result.getWaveform() != null) {
				FileIdentifier file = new FileIdentifier(projectName, fileName);
				IComponentIdentifier<FileIdentifier> identifier = ComponentIdentifierFactory
						.createSimulationEditorIdentifier(file);
				FileContent content = new FileContent(projectName, fileName,
						result.getWaveform());
				editorManager.openEditor(identifier, content);
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
		public void beforeProjectCreation(String projectName)
				throws ResourceVetoException {
			boolean exists;
			try {
				exists = resourceManager.existsProject(projectName);
			} catch (UniformAppletException e) {
				String text = getBundleString(LanguageConstants.STATUSBAR_CANT_CHECK_PROJECT_EXISTENCE);
				text = PlaceholderUtil.replacePlaceholders(text,
						new String[] { projectName });
				echoStatusText(text, MessageType.ERROR);
				// veto project creation
				throw new ResourceVetoException();
			}
			if (exists) {
				String text = getBundleString(LanguageConstants.STATUSBAR_EXISTS_PROJECT);
				text = PlaceholderUtil.replacePlaceholders(text,
						new String[] { projectName });
				echoStatusText(text, MessageType.INFORMATION);
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
		public void beforeProjectCreation(String projectName)
				throws ResourceVetoException {
			if (!resourceManager.isCorrectProjectName(projectName)) {
				String text = getBundleString(LanguageConstants.STATUSBAR_NOT_CORRECT_PROJECT_NAME);
				text = PlaceholderUtil.replacePlaceholders(text,
						new String[] { projectName });
				echoStatusText(text, MessageType.ERROR);
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
		public void projectCreated(String projectName) {
			String text = getBundleString(LanguageConstants.STATUSBAR_PROJECT_CREATED);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { projectName });
			echoStatusText(text, MessageType.SUCCESSFUL);
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
		public void beforeResourceCreation(String projectName, String fileName,
				String type) throws ResourceVetoException {
			boolean exists;
			try {
				exists = resourceManager.existsFile(projectName, fileName);
			} catch (UniformAppletException e) {
				String text = getBundleString(LanguageConstants.STATUSBAR_CANT_CHECK_FILE_EXISTENCE);
				text = PlaceholderUtil.replacePlaceholders(text,
						new String[] { fileName });
				echoStatusText(text, MessageType.ERROR);
				// veto resource creation
				throw new ResourceVetoException();
			}
			if (exists) {
				String text = getBundleString(LanguageConstants.STATUSBAR_EXISTS_FILE);
				text = PlaceholderUtil.replacePlaceholders(text, new String[] {
						fileName, projectName });
				echoStatusText(text, MessageType.INFORMATION);
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
		public void beforeResourceCreation(String projectName, String fileName,
				String type) throws ResourceVetoException {
			if (!resourceManager.isCorrectFileName(fileName)) {
				String text = getBundleString(LanguageConstants.STATUSBAR_NOT_CORRECT_FILE_NAME);
				text = PlaceholderUtil.replacePlaceholders(text,
						new String[] { fileName });
				echoStatusText(text, MessageType.ERROR);
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
		public void resourceCreated(String projectName, String fileName,
				String type) {
			String text = getBundleString(LanguageConstants.STATUSBAR_FILE_CREATED);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { fileName });
			echoStatusText(text, MessageType.SUCCESSFUL);
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
		public void resourceCreated(String projectName, String fileName,
				String type) {
			FileIdentifier file = new FileIdentifier(projectName, fileName);
			IComponentIdentifier<FileIdentifier> identifier = ComponentIdentifierFactory
					.createFileEditorIdentifier(file);

			editorManager.openEditorByResource(identifier);
		}
	}

	/**
	 * Echoes that a resource has been saved.
	 * 
	 * @author Miro Bezjak
	 */
	private class ResourceSavedEcho extends VetoableResourceAdapter {
		@Override
		public void resourceSaved(String projectName, String fileName) {
			String text = bundle
					.getString(LanguageConstants.STATUSBAR_FILE_SAVED);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { fileName });
			echoStatusText(text, MessageType.SUCCESSFUL);
		}
	}

}