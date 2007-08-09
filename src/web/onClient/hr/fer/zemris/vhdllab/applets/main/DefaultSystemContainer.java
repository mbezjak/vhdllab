package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.IStatusBar;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageType;
import hr.fer.zemris.vhdllab.applets.main.conf.ComponentConfiguration;
import hr.fer.zemris.vhdllab.applets.main.conf.ComponentConfigurationParser;
import hr.fer.zemris.vhdllab.applets.main.conf.EditorProperties;
import hr.fer.zemris.vhdllab.applets.main.conf.ViewProperties;
import hr.fer.zemris.vhdllab.applets.main.constant.ComponentTypes;
import hr.fer.zemris.vhdllab.applets.main.constant.LanguageConstants;
import hr.fer.zemris.vhdllab.applets.main.dialog.RunDialog;
import hr.fer.zemris.vhdllab.applets.main.dialog.SaveDialog;
import hr.fer.zemris.vhdllab.applets.main.event.ResourceVetoException;
import hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceAdapter;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentProvider;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.applets.main.model.ResultTarget;
import hr.fer.zemris.vhdllab.applets.main.model.SystemMessage;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;
import hr.fer.zemris.vhdllab.preferences.PropertyAccessException;
import hr.fer.zemris.vhdllab.utilities.PlaceholderUtil;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;

import java.awt.Frame;
import java.io.PrintWriter;
import java.io.StringWriter;
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
 * container.setEditorStrage(myEditorStorage);<br/>
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
	 * An editor storage.
	 */
	private IEditorStorage editorStorage;
	/**
	 * A view storage.
	 */
	private IViewStorage viewStorage;

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
	 * Setter for an editor storage.
	 * 
	 * @param editorStorage
	 *            an editor storage
	 */
	public void setEditorStorage(IEditorStorage editorStorage) {
		this.editorStorage = editorStorage;
	}

	/**
	 * Setter for a view storage.
	 * 
	 * @param viewStorage
	 *            a view storage
	 */
	public void setViewStorage(IViewStorage viewStorage) {
		this.viewStorage = viewStorage;
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
		resourceManager
				.addVetoableResourceListener(new AfterCompilationEcho());
		resourceManager
				.addVetoableResourceListener(new AfterCompilationOpenView());
		resourceManager
				.addVetoableResourceListener(new BeforeSimulationCheckSimulatableAndSaveEditors());
		resourceManager
				.addVetoableResourceListener(new AfterSimulationEcho());
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

		String data;
		data = getProperty(UserFileConstants.SYSTEM_OPENED_EDITORS);
		List<FileIdentifier> files = SerializationUtil
				.deserializeEditorInfo(data);
		for (FileIdentifier f : files) {
			openEditor(f.getProjectName(), f.getFileName(), true, false);
		}

		data = getProperty(UserFileConstants.SYSTEM_OPENED_VIEWS);
		List<String> views = SerializationUtil.deserializeViewInfo(data);
		for (String s : views) {
			openView(s);
		}
	}

	/**
	 * Disposes any resources used by system container.
	 * 
	 * @throws UniformAppletException
	 *             if exceptional condition occurs
	 */
	public void dispose() throws UniformAppletException {
		saveAllEditors();
		String data;
		data = SerializationUtil.serializeEditorInfo(editorStorage
				.getAllOpenedEditors());
		setProperty(UserFileConstants.SYSTEM_OPENED_EDITORS, data);

		List<String> views = new ArrayList<String>();
		for (IView v : viewStorage.getAllOpenedViews()) {
			String id = viewStorage.getIdentifierFor(v);
			views.add(id);
		}
		data = SerializationUtil.serializeViewInfo(views);
		setProperty(UserFileConstants.SYSTEM_OPENED_VIEWS, data);

		systemLog.removeAllSystemLogListeners();
		resourceManager.removeAllVetoableResourceListeners();
		getPreferences().removeAllPropertyListeners();

		resourceManager = null;
		componentProvider = null;
		componentStorage = null;
		editorStorage = null;
		viewStorage = null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getSelectedProject()
	 */
	@Override
	public String getSelectedProject() {
		if (!viewStorage.isViewOpened(ComponentTypes.VIEW_PROJECT_EXPLORER)) {
			return null;
		} else {
			IView view = viewStorage.getOpenedView(ComponentTypes.VIEW_PROJECT_EXPLORER);
			return view.asInterface(IProjectExplorer.class).getSelectedProject();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getSelectedFile()
	 */
	@Override
	public FileIdentifier getSelectedFile() {
		if (!viewStorage.isViewOpened(ComponentTypes.VIEW_PROJECT_EXPLORER)) {
			return null;
		} else {
			IView view = viewStorage.getOpenedView(ComponentTypes.VIEW_PROJECT_EXPLORER);
			return view.asInterface(IProjectExplorer.class).getSelectedFile();
		}
	}

	/* PREFERENCES AND RESOURCE BUNDLE METHODS */

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
	 *      hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageEnum)
	 */
	@Override
	public void echoStatusText(String text, MessageType type) {
		// getStatusBar().setMessage(text, type);
		systemLog.addSystemMessage(new SystemMessage(text, type));
	}

	/* EDITOR MANIPULATION METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#openPreferences()
	 */
	@Override
	public void openPreferences() {
		String data = getPreferences().serialize();
		openEditor("about", "config", data, FileTypes.FT_PREFERENCES, false,
				false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#viewVHDLCode(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	@Override
	public void viewVHDLCode(IEditor editor) {
		if (editor == null) {
			throw new NullPointerException("Editor cant be null");
		}
		viewVHDLCode(editor.getProjectName(), editor.getFileName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#viewVHDLCode(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public IEditor viewVHDLCode(String projectName, String fileName) {
		if(editorStorage.isEditorOpened(projectName, "vhdl:" + fileName)) {
			return editorStorage.getOpenedEditor(projectName, "vhdl:" + fileName);
		}
		if (resourceManager.isCircuit(projectName, fileName)
				|| resourceManager.isTestbench(projectName, fileName)) {
			String vhdl;
			try {
				vhdl = resourceManager.generateVHDL(projectName, fileName);
			} catch (UniformAppletException e) {
				String text = bundle
						.getString(LanguageConstants.STATUSBAR_CANT_VIEW_VHDL_CODE);
				text = PlaceholderUtil.replacePlaceholders(text,
						new String[] { fileName });
				echoStatusText(text, MessageType.ERROR);
				return null;
			}
			openEditor(projectName, "vhdl:" + fileName, vhdl,
					FileTypes.FT_VHDL_SOURCE, false, true);
			return editorStorage.getOpenedEditor(projectName, "vhdl:" + fileName);
		} else {
			String text = bundle
					.getString(LanguageConstants.STATUSBAR_CANT_VIEW_VHDL_CODE_FOR_THAT_FILE);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { fileName });
			echoStatusText(text, MessageType.ERROR);
			return null;
		}
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	@Override
	public IEditor getEditor(FileIdentifier resource) {
		if (resource == null) {
			return null;
		}
		return getEditor(resource.getProjectName(), resource.getFileName());
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	@Override
	public IEditor getEditor(String projectName, String fileName) {
		if (!editorStorage.isEditorOpened(projectName, fileName)) {
			// TODO treba se tablica savable-readonly za svaki file type
			// napravit pa tu samo to gledat.
			openEditor(projectName, fileName, true, false);
		}
		return editorStorage.getOpenedEditor(projectName, fileName);
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	@Override
	public void openEditor(String projectName, String fileName,
			boolean savable, boolean readOnly) {
		if (projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if (fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		IEditor editor = editorStorage.getOpenedEditor(projectName, fileName);
		if (editor == null) {
			String content;
			try {
				content = resourceManager.getFileContent(projectName,
						fileName);
			} catch (UniformAppletException e) {
				e.printStackTrace();
				return;
			}
			FileContent fileContent = new FileContent(projectName, fileName,
					content);
			String type = resourceManager.getFileType(projectName, fileName);

			editor = openEditorImpl(fileContent, type, savable, readOnly);
		}
		editorStorage.setSelectedEditor(editor);
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	public void openEditor(String projectName, String fileName, String content,
			String type, boolean savable, boolean readOnly) {
		if (projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if (fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		if (content == null) {
			throw new NullPointerException("Content can not be null.");
		}
		IEditor editor = editorStorage.getOpenedEditor(projectName, fileName);
		if (editor == null) {
			FileContent fileContent = new FileContent(projectName, fileName,
					content);

			editor = openEditorImpl(fileContent, type, savable, readOnly);
		} else {
			FileContent fileContent = new FileContent(projectName, fileName,
					content);
			editor.setFileContent(fileContent);
		}
		editorStorage.setSelectedEditor(editor);
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	private IEditor openEditorImpl(FileContent fileContent, String type,
			boolean savable, boolean readOnly) {
		// Initialization of an editor
		IEditor editor = getNewEditorInstanceByFileType(type);
		editor.setSystemContainer(this);
		editor.init();
		editor.setSavable(savable);
		editor.setReadOnly(readOnly);
		editor.setFileContent(fileContent);
		// End of initialization

		String projectName = fileContent.getProjectName();
		String fileName = fileContent.getFileName();
		String title = editorStorage.createTitleFor(projectName, fileName);
		editorStorage.add(title, title, editor);
		String toolTipText = createEditorToolTip(projectName, fileName);
		editorStorage.setToolTipText(editor, toolTipText);
		return editor;
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	@Override
	public void saveEditor(IEditor editor) {
		if (editor == null)
			return;
		List<IEditor> editorsToSave = new ArrayList<IEditor>(1);
		editorsToSave.add(editor);
		saveEditors(editorsToSave);
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	@Override
	public void saveAllEditors() {
		List<IEditor> openedEditors = editorStorage.getAllOpenedEditors();
		saveEditors(openedEditors);
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	private void saveEditors(List<IEditor> editorsToSave) {
		if (editorsToSave == null || editorsToSave.isEmpty())
			return;
		List<IEditor> savedEditors = new ArrayList<IEditor>();
		List<String> projects = new ArrayList<String>();
		for (IEditor editor : editorsToSave) {
			if (editor.isSavable() && editor.isModified()) {
				String fileName = editor.getFileName();
				String projectName = editor.getProjectName();
				String content = editor.getData();
				if (content == null)
					continue;
				savedEditors.add(editor);
				if (!projects.contains(projectName)) {
					projects.add(projectName);
				}
				try {
					resourceManager.saveFile(projectName, fileName, content);
					resetEditorTitle(false, projectName, fileName);
					String text = bundle
							.getString(LanguageConstants.STATUSBAR_FILE_SAVED);
					text = PlaceholderUtil.replacePlaceholders(text,
							new String[] { fileName });
					echoStatusText(text, MessageType.SUCCESSFUL);
				} catch (UniformAppletException e) {
					String text = bundle
							.getString(LanguageConstants.STATUSBAR_CANT_SAVE_FILE);
					text = PlaceholderUtil.replacePlaceholders(text,
							new String[] { fileName });
					echoStatusText(text, MessageType.ERROR);
					// TODO ovo se treba maknut kad MainApplet vise nece bit u
					// development fazi
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					JOptionPane.showMessageDialog(parentFrame, sw.toString());
					return;
				}
			}
		}

		if (viewStorage.isViewOpened(ComponentTypes.VIEW_PROJECT_EXPLORER)) {
			IView view = viewStorage.getOpenedView(ComponentTypes.VIEW_PROJECT_EXPLORER);
			IProjectExplorer projectExplorer = view.asInterface(IProjectExplorer.class);
			for (String projectName : projects) {
				projectExplorer.refreshProject(projectName);
			}
		}
		if (savedEditors.size() != 0) {
			String text = bundle
					.getString(LanguageConstants.STATUSBAR_FILE_SAVED_ALL);
			String numberOfFiles = String.valueOf(savedEditors.size());
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { numberOfFiles });
			echoStatusText(text, MessageType.SUCCESSFUL);
		}
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	private List<IEditor> pickOpenedEditors(List<IEditor> editors) {
		List<IEditor> openedEditors = new ArrayList<IEditor>();
		for (IEditor e : editors) {
			if (editorStorage.isEditorOpened(e)) {
				openedEditors.add(e);
			}
		}
		return openedEditors;
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	@Override
	public void closeEditor(IEditor editor, boolean showDialog) {
		if (editor == null)
			return;
		List<IEditor> editorsToClose = new ArrayList<IEditor>(1);
		editorsToClose.add(editor);
		closeEditors(editorsToClose, showDialog);
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	@Override
	public void closeAllEditors(boolean showDialog) {
		List<IEditor> openedEditors = editorStorage.getAllOpenedEditors();
		closeEditors(openedEditors, showDialog);
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	@Override
	public void closeAllButThisEditor(IEditor editorToKeepOpened,
			boolean showDialog) {
		if (editorToKeepOpened == null)
			return;
		List<IEditor> openedEditors = editorStorage.getAllOpenedEditors();
		openedEditors.remove(editorToKeepOpened);
		closeEditors(openedEditors, showDialog);
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	private void closeEditors(List<IEditor> editorsToClose, boolean showDialog) {
		if (editorsToClose == null)
			return;
		String title = bundle
				.getString(LanguageConstants.DIALOG_SAVE_RESOURCES_TITLE);
		String message = bundle
				.getString(LanguageConstants.DIALOG_SAVE_RESOURCES_MESSAGE);
		editorsToClose = pickOpenedEditors(editorsToClose);
		boolean shouldContinue;
		if (showDialog) {
			shouldContinue = saveResourcesWithDialog(editorsToClose, title,
					message);
		} else {
			shouldContinue = true;
		}
		if (shouldContinue) {
			for (IEditor editor : editorsToClose) {
				// Clean up of an editor
				editor.dispose();
				// End of clean up

				editorStorage.close(editor);
			}
		}
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	@Override
	public IView getView(String type) {
		IView view = viewStorage.getOpenedView(type);
		if (view == null) {
			openView(type);
			view = viewStorage.getOpenedView(type);
		}
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#openProjectExplorer()
	 */
	@Override
	public void openProjectExplorer() {
		openView(ComponentTypes.VIEW_PROJECT_EXPLORER);
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	@Override
	public IView openView(String id) {
		IView view = viewStorage.getOpenedView(id);
		if (view == null) {
			// Initialization of a view
			view = getNewViewInstance(id);
			view.setSystemContainer(this);
			view.init();
			// End of initialization

			String title = bundle.getString(LanguageConstants.TITLE_FOR + id);
			viewStorage.add(id, title, view);
		}
		viewStorage.setSelectedView(id);
		return getView(id);
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	@Override
	public void closeView(IView view) {
		if (view == null)
			return;
		List<IView> viewsToClose = new ArrayList<IView>(1);
		viewsToClose.add(view);
		closeViews(viewsToClose);
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	private void closeAllViews() {
		List<IView> openedViews = viewStorage.getAllOpenedViews();
		closeViews(openedViews);
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	private void closeAllButThisView(IView viewToKeepOpened) {
		if (viewToKeepOpened == null)
			return;
		List<IView> openedViews = viewStorage.getAllOpenedViews();
		openedViews.remove(viewToKeepOpened);
		closeViews(openedViews);
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	private void closeViews(List<IView> viewsToClose) {
		if (viewsToClose == null)
			return;
		for (IView view : viewsToClose) {
			view.dispose();
			viewStorage.close(view);
		}
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	@Override
	public void resetEditorTitle(boolean contentChanged, String projectName,
			String fileName) {
		String title = editorStorage.createTitleFor(projectName, fileName);
		if (contentChanged) {
			title = "*" + title;
		}
		IEditor editor = editorStorage.getOpenedEditor(projectName, fileName);
		if (editor != null) {
			editorStorage.setTitle(editor, title);
		}
	}

	/**
	 * TODO PENDING CHANGE!
	 */
	private boolean saveResourcesWithDialog(List<IEditor> openedEditors,
			String title, String message) {
		if (openedEditors == null)
			return false;
		// create a list of savable and modified editors
		List<IEditor> notSavedEditors = new ArrayList<IEditor>();
		for (IEditor editor : openedEditors) {
			if (editor.isSavable() && editor.isModified()) {
				notSavedEditors.add(editor);
			}
		}

		if (!notSavedEditors.isEmpty()) {
			// look in preference and see if there is a need to show save dialog
			// (user might have
			// checked a "always save resources" checkbox)
			boolean shouldAutoSave;
			String selected = getProperty(UserFileConstants.SYSTEM_ALWAYS_SAVE_RESOURCES);
			if (selected != null) {
				shouldAutoSave = Boolean.parseBoolean(selected);
			} else {
				shouldAutoSave = false;
			}

			List<IEditor> editorsToSave = notSavedEditors;
			if (!shouldAutoSave) {
				List<FileIdentifier> filesToSave = showSaveDialog(title,
						message, notSavedEditors);
				if (filesToSave == null)
					return false;

				// If size of files returned by save dialog equals those of not
				// saved editors
				// then a list of files are entirely equal to a list of not
				// saved editors and
				// no transformation is required.
				if (filesToSave.size() != editorsToSave.size()) {
					// transform FileIdentifiers to editors
					editorsToSave = new ArrayList<IEditor>();
					for (FileIdentifier file : filesToSave) {
						String projectName = file.getProjectName();
						String fileName = file.getFileName();
						IEditor e = editorStorage.getOpenedEditor(projectName,
								fileName);
						editorsToSave.add(e);
					}
				}
			}

			saveEditors(editorsToSave);
		}
		return true;
	}

	/* PRIVATE COMMON TASK METHODS */

	/**
	 * Creates an editor tooltip text.
	 * 
	 * @param projectName
	 *            a name of a project
	 * @param fileName
	 *            a name of a file
	 * @return a tooltip text
	 */
	private String createEditorToolTip(String projectName, String fileName) {
		// TODO CHANGE THIS TO MORE APPROPRIATE
		return editorStorage.createTitleFor(projectName, fileName);
	}

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
	 * Returns a new instance of specified editor.
	 * 
	 * @param type
	 *            a type of a file that editor handles
	 * @return a new editor instance
	 * @throws NullPointerException
	 *             if <code>type</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if can't find editor for given <code>type</code>
	 * @throws IllegalStateException
	 *             if editor can't be instantiated
	 */
	private IEditor getNewEditorInstanceByFileType(String type) {
		if (type == null) {
			throw new NullPointerException("Type can not be null.");
		}
		EditorProperties ep = configuration.getEditorPropertiesByFileType(type);
		if (ep == null) {
			throw new IllegalArgumentException(
					"Can not find editor for given type.");
		}
		return instantiateEditor(ep);
	}

	/**
	 * Returns a new instance of specified editor.
	 * 
	 * @param id
	 *            an identifier of an editor
	 * @return a new editor instance
	 * @throws NullPointerException
	 *             if <code>id</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if can't find editor for given <code>id</code>
	 * @throws IllegalStateException
	 *             if editor can't be instantiated
	 */
	private IEditor getNewEditorInstance(String id) {
		if (id == null) {
			throw new NullPointerException("Identifier can not be null.");
		}
		EditorProperties ep = configuration.getEditorProperties(id);
		if (ep == null) {
			throw new IllegalArgumentException(
					"Can not find editor for given identifier.");
		}
		return instantiateEditor(ep);
	}

	/**
	 * Returns a new instance of specified editor.
	 * 
	 * @param ep
	 *            an editor properties that contains an information on editor
	 *            class
	 * @return a new editor instance
	 * @throws IllegalStateException
	 *             if editor can't be instantiated
	 */
	private IEditor instantiateEditor(EditorProperties ep) {
		IEditor editor = null;
		try {
			editor = (IEditor) Class.forName(ep.getClazz()).newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Can not instantiate editor.");
		}
		return editor;
	}

	/**
	 * Returns a new instance of specified view.
	 * 
	 * @param id
	 *            an identifier of an view
	 * @return a new view instance
	 * @throws NullPointerException
	 *             if <code>id</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if can't find view for given <code>id</code>
	 * @throws IllegalStateException
	 *             if view can't be instantiated
	 */
	private IView getNewViewInstance(String id) {
		if (id == null) {
			throw new NullPointerException("Identifier can not be null.");
		}
		ViewProperties vp = configuration.getViewProperties(id);
		if (vp == null) {
			throw new IllegalArgumentException(
					"Can not find view for given identifier.");
		}
		return instantiateView(vp);
	}

	/**
	 * Returns a new instance of specified view.
	 * 
	 * @param vp
	 *            a view properties that contains an information on a view class
	 * @return a new view instance
	 * @throws IllegalStateException
	 *             if view can't be instantiated
	 */
	private IView instantiateView(ViewProperties vp) {
		IView view = null;
		try {
			view = (IView) Class.forName(vp.getClazz()).newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Can not instantiate view.");
		}
		return view;
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
	private List<FileIdentifier> showSaveDialog(String title, String message,
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
			dialog.addItem(true, editor.getProjectName(), editor.getFileName());
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
		String projectName = (String) JOptionPane.showInputDialog(parentFrame,
				message, title, JOptionPane.OK_CANCEL_OPTION);
		/*
		 * try { if(projectName != null &&
		 * communicator.existsProject(projectName)) { return null; } } catch
		 * (UniformAppletException e) { }
		 */
		if(projectName.equals("")) {
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
			List<IEditor> openedEditors = editorStorage
					.getOpenedEditorsThatHave(projectName);
			String title = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_COMPILATION_TITLE);
			String message = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_COMPILATION_MESSAGE);
			boolean shouldContinue = saveResourcesWithDialog(openedEditors,
					title, message);
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
			openView(ComponentTypes.VIEW_COMPILATION_ERRORS);

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
			List<IEditor> openedEditors = editorStorage
					.getOpenedEditorsThatHave(projectName);
			String title = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_SIMULATION_TITLE);
			String message = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_SIMULATION_MESSAGE);
			boolean shouldContinue = saveResourcesWithDialog(openedEditors,
					title, message);
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
			openView(ComponentTypes.VIEW_SIMULATION_ERRORS);

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
				String simulationName = fileName + ".sim";
				openEditor(projectName, simulationName, result.getWaveform(),
						FileTypes.FT_VHDL_SIMULATION, false, true);
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
			openEditor(projectName, fileName, true, false);
		}
	}

}