package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.DefaultProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.IStatusBar;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageEnum;
import hr.fer.zemris.vhdllab.applets.main.constant.ComponentTypes;
import hr.fer.zemris.vhdllab.applets.main.constant.LanguageConstants;
import hr.fer.zemris.vhdllab.applets.main.dialog.RunDialog;
import hr.fer.zemris.vhdllab.applets.main.dialog.SaveDialog;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentProvider;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IProjectExplorerStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IViewStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;
import hr.fer.zemris.vhdllab.preferences.PropertyAccessException;
import hr.fer.zemris.vhdllab.utilities.PlaceholderUtil;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;
import hr.fer.zemris.vhdllab.vhdl.model.StringFormat;

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
 * DefaultSystemContainer container = new DefaultSystemContainer(myCommunicator, myComponentProvider);
 * container.setComponentStorage(myComponentStorage);
 * container.setEditorStrage(myEditorStorage);
 * container.setViewStrage(myViewStorage);
 * container.setProjectExplorerStrage(myProjectExplorerStorage);
 * container.init();
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
	 * A communicator.
	 */
	private Communicator communicator;
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
	 * A project explorer storage.
	 */
	private IProjectExplorerStorage projectExplorerStorage;

	/**
	 * Constructs a default system container.
	 * 
	 * @param communicator
	 *            a communicator
	 * @param componentProvider
	 *            a component provider
	 * @param parentFrame
	 *            a parent frame for enabling modal dialogs
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public DefaultSystemContainer(Communicator communicator,
			IComponentProvider componentProvider, Frame parentFrame) {
		if (communicator == null) {
			throw new NullPointerException("Communicator cant be null");
		}
		if (componentProvider == null) {
			throw new NullPointerException("Component provider cant be null");
		}
		this.communicator = communicator;
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
	 * Setter for a project explorer storage.
	 * 
	 * @param projectExplorerStorage
	 *            a project explorer storage
	 */
	public void setProjectExplorerStorage(
			IProjectExplorerStorage projectExplorerStorage) {
		this.projectExplorerStorage = projectExplorerStorage;
	}

	/**
	 * Initializes a system container.
	 * 
	 * @throws UniformAppletException
	 *             if exceptional condition occurs
	 */
	public void init() throws UniformAppletException {
		bundle = getResourceBundle(LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN);
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
			String type = communicator.getViewType(v);
			views.add(type);
		}
		data = SerializationUtil.serializeViewInfo(views);
		setProperty(UserFileConstants.SYSTEM_OPENED_VIEWS, data);

		// TODO vidjet sto tocno s ovim. kakva je implementacija i mozda
		// poboljsat implementaciju
		try {
			communicator.dispose();
		} catch (Exception ignored) {
			// TODO ovo se treba maknut kad MainApplet vise nece bit u
			// development fazi
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ignored.printStackTrace(pw);
			JOptionPane.showMessageDialog(parentFrame, sw.toString());
		}
		communicator = null;
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
		String title = getBundleString(LanguageConstants.DIALOG_RUN_COMPILATION_TITLE);
		String listTitle = getBundleString(LanguageConstants.DIALOG_RUN_COMPILATION_LIST_TITLE);
		FileIdentifier file = showRunDialog(title, listTitle,
				RunDialog.COMPILATION_TYPE);
		if (file == null) {
			return false;
		}
		String projectName = file.getProjectName();
		String fileName = file.getFileName();
		return compile(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#compileLastHistoryResult()
	 */
	@Override
	public boolean compileLastHistoryResult() {
		if (communicator.compilationHistoryIsEmpty()) {
			return compileWithDialog();
		} else {
			FileIdentifier file = getLastCompilationHistoryTarget();
			return compile(file.getProjectName(), file.getFileName());
		}
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
		if (!isCompilable(projectName, fileName)) {
			String text = getBundleString(LanguageConstants.STATUSBAR_NOT_COMPILABLE);
			text = PlaceholderUtil.replacePlaceholders(text, new String[] {
					fileName, projectName });
			echoStatusText(text, MessageEnum.Information);
			return false;
		}
		List<IEditor> openedEditors = editorStorage
				.getOpenedEditorsThatHave(projectName);
		String title = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_COMPILATION_TITLE);
		String message = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_COMPILATION_MESSAGE);
		boolean shouldContinue = saveResourcesWithDialog(openedEditors, title,
				message);
		if (!shouldContinue) {
			return false;
		}
		CompilationResult result;
		try {
			result = communicator.compile(projectName, fileName);
		} catch (UniformAppletException e) {
			String text = getBundleString(LanguageConstants.STATUSBAR_CANT_COMPILE);
			text = PlaceholderUtil.replacePlaceholders(text, new String[] {
					fileName, projectName });
			echoStatusText(text, MessageEnum.Error);
			return false;
		}
		String text = getBundleString(LanguageConstants.STATUSBAR_COMPILED);
		text = PlaceholderUtil.replacePlaceholders(text, new String[] {
				fileName, projectName });
		echoStatusText(text, MessageEnum.Information);
		IView view = openView(ComponentTypes.VIEW_COMPILATION_ERRORS);
		view.setData(result);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getLastCompilationHistoryTarget()
	 */
	@Override
	public FileIdentifier getLastCompilationHistoryTarget() {
		return communicator.getLastCompilationHistoryTarget();
	}

	/* SIMULATE RESOURCE METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#simulateWithDialog()
	 */
	@Override
	public boolean simulateWithDialog() {
		String title = getBundleString(LanguageConstants.DIALOG_RUN_SIMULATION_TITLE);
		String listTitle = getBundleString(LanguageConstants.DIALOG_RUN_SIMULATION_LIST_TITLE);
		FileIdentifier file = showRunDialog(title, listTitle,
				RunDialog.SIMULATION_TYPE);
		if (file == null) {
			return false;
		}
		String projectName = file.getProjectName();
		String fileName = file.getFileName();
		return simulate(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#simulateLastHistoryResult()
	 */
	@Override
	public boolean simulateLastHistoryResult() {
		if (communicator.simulationHistoryIsEmpty()) {
			return simulateWithDialog();
		} else {
			FileIdentifier file = communicator.getLastSimulationHistoryTarget();
			return simulate(file.getProjectName(), file.getFileName());
		}
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
		if (!isSimulatable(projectName, fileName)) {
			String text = getBundleString(LanguageConstants.STATUSBAR_NOT_SIMULATABLE);
			text = PlaceholderUtil.replacePlaceholders(text, new String[] {
					fileName, projectName });
			echoStatusText(text, MessageEnum.Information);
		}
		List<IEditor> openedEditors = editorStorage
				.getOpenedEditorsThatHave(projectName);
		String title = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_SIMULATION_TITLE);
		String message = getBundleString(LanguageConstants.DIALOG_SAVE_RESOURCES_FOR_SIMULATION_MESSAGE);
		boolean shouldContinue = saveResourcesWithDialog(openedEditors, title,
				message);
		if (!shouldContinue) {
			return false;
		}
		SimulationResult result;
		try {
			result = communicator.runSimulation(projectName, fileName);
		} catch (UniformAppletException e) {
			String text = getBundleString(LanguageConstants.STATUSBAR_CANT_SIMULATE);
			text = PlaceholderUtil.replacePlaceholders(text, new String[] {
					fileName, projectName });
			echoStatusText(text, MessageEnum.Error);
			return false;
		}
		String text = getBundleString(LanguageConstants.STATUSBAR_SIMULATED);
		text = PlaceholderUtil.replacePlaceholders(text, new String[] {
				fileName, projectName });
		echoStatusText(text, MessageEnum.Information);
		IView view = openView(ComponentTypes.VIEW_SIMULATION_ERRORS);
		view.setData(result);
		if (result.getWaveform() != null) {
			String simulationName = fileName + ".sim";
			openEditor(projectName, simulationName, result.getWaveform(),
					FileTypes.FT_VHDL_SIMULATION, false, true);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getLastSimulationHistoryTarget()
	 */
	@Override
	public FileIdentifier getLastSimulationHistoryTarget() {
		return communicator.getLastSimulationHistoryTarget();
	}

	/* RESOURCE MANIPULATION METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#createNewProjectInstance()
	 */
	@Override
	public boolean createNewProjectInstance() {
		String title = getBundleString(LanguageConstants.DIALOG_CREATE_NEW_PROJECT_TITLE);
		String message = getBundleString(LanguageConstants.DIALOG_CREATE_NEW_PROJECT_MESSAGE);
		String projectName = showCreateProjectDialog(title, message);
		if (projectName == null || projectName.equals("")) {
			return false;
		}
		boolean exists;
		try {
			exists = existsProject(projectName);
		} catch (UniformAppletException e) {
			String text = getBundleString(LanguageConstants.STATUSBAR_CANT_CHECK_PROJECT_EXISTENCE);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { projectName });
			echoStatusText(text, MessageEnum.Error);
			return false;
		}
		if (exists) {
			// projectName is never null here
			String text = getBundleString(LanguageConstants.STATUSBAR_EXISTS_PROJECT);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { projectName });
			echoStatusText(text, MessageEnum.Information);
			return false;
		}
		try {
			communicator.createProject(projectName);
		} catch (UniformAppletException e) {
			String text = getBundleString(LanguageConstants.STATUSBAR_CANT_CREATE_PROJECT);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { projectName });
			echoStatusText(text, MessageEnum.Error);
			return false;
		}
		// TODO PENDING REMOVAL!
		if (projectExplorerStorage.isProjectExplorerOpened()) {
			projectExplorerStorage.getProjectExplorer().addProject(projectName);
		}
		// END REMOVAL
		String text = getBundleString(LanguageConstants.STATUSBAR_PROJECT_CREATED);
		text = PlaceholderUtil.replacePlaceholders(text,
				new String[] { projectName });
		echoStatusText(text, MessageEnum.Successfull);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#createNewFileInstance(java.lang.String)
	 */
	@Override
	public boolean createNewFileInstance(String id) {
		if (id == null) {
			throw new NullPointerException("Component identifier cant be null.");
		}
		String projectName = getSelectedProject();
		if (projectName == null) {
			String text = getBundleString(LanguageConstants.STATUSBAR_NO_SELECTED_PROJECT);
			echoStatusText(text, MessageEnum.Information);
			return false;
		}
		IWizard wizard = getNewEditorInstance(id).getWizard();
		FileContent content = initWizard(wizard, projectName);
		if (content == null) {
			// user canceled or no wizard for such editor
			return false;
		}
		String fileName = content.getFileName();
		String data = content.getContent();
		boolean exists;
		try {
			exists = existsFile(projectName, fileName);
		} catch (UniformAppletException e) {
			String text = getBundleString(LanguageConstants.STATUSBAR_CANT_CHECK_FILE_EXISTENCE);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { fileName });
			echoStatusText(text, MessageEnum.Error);
			return false;
		}
		if (exists) {
			String text = getBundleString(LanguageConstants.STATUSBAR_EXISTS_FILE);
			text = PlaceholderUtil.replacePlaceholders(text, new String[] {
					fileName, projectName });
			echoStatusText(text, MessageEnum.Information);
			return false;
		}
		try {
			communicator.createFile(projectName, fileName, id);
			communicator.saveFile(projectName, fileName, data);
		} catch (UniformAppletException e) {
			String text = getBundleString(LanguageConstants.STATUSBAR_CANT_CREATE_FILE);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { fileName });
			echoStatusText(text, MessageEnum.Error);
		}
		// TODO PENDING REMOVAL!
		if (projectExplorerStorage.isProjectExplorerOpened()) {
			projectExplorerStorage.getProjectExplorer().addFile(projectName,
					fileName);
		}
		// END REMOVAL
		String text = getBundleString(LanguageConstants.STATUSBAR_FILE_CREATED);
		text = PlaceholderUtil.replacePlaceholders(text,
				new String[] { fileName });
		echoStatusText(text, MessageEnum.Successfull);
		openEditor(projectName, fileName, true, false);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#deleteFile(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void deleteFile(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		if (fileName == null) {
			throw new NullPointerException("File name can not be null.");
		}
		if (editorStorage.isEditorOpened(projectName, fileName)) {
			IEditor editor = editorStorage.getOpenedEditor(projectName,
					fileName);
			closeEditor(editor, false);
		}
		// TODO PENDING REMOVAL!
		if (projectExplorerStorage.isProjectExplorerOpened()) {
			projectExplorerStorage.getProjectExplorer().removeFile(projectName,
					fileName);
		}
		// END REMOVAL
		communicator.deleteFile(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#deleteProject(java.lang.String)
	 */
	@Override
	public void deleteProject(String projectName) throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name can not be null.");
		}
		for (IEditor e : editorStorage.getOpenedEditorsThatHave(projectName)) {
			closeEditor(e, false);
		}
		// TODO PENDING REMOVAL!
		if (projectExplorerStorage.isProjectExplorerOpened()) {
			projectExplorerStorage.getProjectExplorer().removeProject(
					projectName);
		}
		// END REMOVAL
		communicator.deleteProject(projectName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#existsFile(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean existsFile(String projectName, String fileName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		return communicator.existsFile(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#existsProject(java.lang.String)
	 */
	@Override
	public boolean existsProject(String projectName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		return communicator.existsProject(projectName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getAllProjects()
	 */
	@Override
	public List<String> getAllProjects() throws UniformAppletException {
		return communicator.getAllProjects();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getFilesFor(java.lang.String)
	 */
	@Override
	public List<String> getFilesFor(String projectName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		return communicator.findFilesByProject(projectName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getAllCircuits(java.lang.String)
	 */
	@Override
	public List<String> getAllCircuits(String projectName)
			throws UniformAppletException {
		List<String> fileNames = getFilesFor(projectName);
		List<String> circuits = new ArrayList<String>();
		for (String name : fileNames) {
			if (isCircuit(projectName, name)) {
				circuits.add(name);
			}
		}
		return circuits;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getAllTestbenches(java.lang.String)
	 */
	@Override
	public List<String> getAllTestbenches(String projectName)
			throws UniformAppletException {
		List<String> fileNames = getFilesFor(projectName);
		List<String> testbenches = new ArrayList<String>();
		for (String name : fileNames) {
			if (isTestbench(projectName, name)) {
				testbenches.add(name);
			}
		}
		return testbenches;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getCircuitInterfaceFor(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public CircuitInterface getCircuitInterfaceFor(String projectName,
			String fileName) throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		return communicator.getCircuitInterfaceFor(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getPredefinedFileContent(java.lang.String)
	 */
	@Override
	public String getPredefinedFileContent(String fileName)
			throws UniformAppletException {
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		return communicator.loadPredefinedFileContent(fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getFileType(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String getFileType(String projectName, String fileName) {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		try {
			return communicator.loadFileType(projectName, fileName);
		} catch (UniformAppletException e) {
			String text = getBundleString(LanguageConstants.STATUSBAR_CANT_LOAD_FILE_TYPE);
			echoStatusText(text, MessageEnum.Error);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#extractHierarchy(java.lang.String)
	 */
	@Override
	public Hierarchy extractHierarchy(String projectName)
			throws UniformAppletException {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		return communicator.extractHierarchy(projectName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getSelectedProject()
	 */
	@Override
	public String getSelectedProject() {
		if (!projectExplorerStorage.isProjectExplorerOpened()) {
			return null;
		} else {
			return projectExplorerStorage.getProjectExplorer()
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
		if (!projectExplorerStorage.isProjectExplorerOpened()) {
			return null;
		} else {
			return projectExplorerStorage.getProjectExplorer()
					.getSelectedFile();
		}
	}

	/* IS-SOMETHING METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#isCircuit(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isCircuit(String projectName, String fileName) {
		String type = getFileType(projectName, fileName);
		return FileTypes.isCircuit(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#isTestbench(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isTestbench(String projectName, String fileName) {
		String type = getFileType(projectName, fileName);
		if (FileTypes.isTestbench(type)) {
			return true;
		}

		CircuitInterface ci;
		try {
			ci = getCircuitInterfaceFor(projectName, fileName);
		} catch (UniformAppletException e) {
			return false;
		}
		if (ci.getPorts().isEmpty()) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#isSimulation(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isSimulation(String projectName, String fileName) {
		String type = getFileType(projectName, fileName);
		return FileTypes.isSimulation(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#isCompilable(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isCompilable(String projectName, String fileName) {
		String type = getFileType(projectName, fileName);
		return FileTypes.isCircuit(type) && !FileTypes.isPredefined(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#isSimulatable(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isSimulatable(String projectName, String fileName) {
		String type = getFileType(projectName, fileName);
		return FileTypes.isTestbench(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#isCorrectEntityName(java.lang.String)
	 */
	@Override
	public boolean isCorrectEntityName(String name) {
		return StringFormat.isCorrectEntityName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#isCorrectProjectName(java.lang.String)
	 */
	@Override
	public boolean isCorrectProjectName(String name) {
		return StringFormat.isCorrectProjectName(name);
	}

	/* PREFERENCES AND RESOURCE BUNDLE METHODS */

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getPreferences()
	 */
	@Override
	public IUserPreferences getPreferences() {
		return communicator.getPreferences();
	}
	

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#setProperty(java.lang.String, java.lang.String)
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
		return communicator.getResourceBundle(baseName);
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
	public void echoStatusText(String text, MessageEnum type) {
		getStatusBar().setMessage(text, type);
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
	public void viewVHDLCode(String projectName, String fileName) {
		if (isCircuit(projectName, fileName)
				|| isTestbench(projectName, fileName)) {
			String vhdl;
			try {
				vhdl = communicator.generateVHDL(projectName, fileName);
			} catch (UniformAppletException e) {
				String text = bundle
						.getString(LanguageConstants.STATUSBAR_CANT_VIEW_VHDL_CODE);
				text = PlaceholderUtil.replacePlaceholders(text,
						new String[] { fileName });
				echoStatusText(text, MessageEnum.Error);
				return;
			}
			openEditor(projectName, "vhdl:" + fileName, vhdl,
					FileTypes.FT_VHDL_SOURCE, false, true);
		} else {
			String text = bundle
					.getString(LanguageConstants.STATUSBAR_CANT_VIEW_VHDL_CODE_FOR_THAT_FILE);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { fileName });
			echoStatusText(text, MessageEnum.Error);
		}
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
				content = communicator.loadFileContent(projectName, fileName);
			} catch (UniformAppletException e) {
				e.printStackTrace();
				return;
			}
			FileContent fileContent = new FileContent(projectName, fileName,
					content);
			String type = getFileType(projectName, fileName);

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
		IEditor editor = communicator.getEditor(type);
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
					communicator.saveFile(projectName, fileName, content);
					resetEditorTitle(false, projectName, fileName);
					String text = bundle
							.getString(LanguageConstants.STATUSBAR_FILE_SAVED);
					text = PlaceholderUtil.replacePlaceholders(text,
							new String[] { fileName });
					echoStatusText(text, MessageEnum.Successfull);
				} catch (UniformAppletException e) {
					String text = bundle
							.getString(LanguageConstants.STATUSBAR_CANT_SAVE_FILE);
					text = PlaceholderUtil.replacePlaceholders(text,
							new String[] { fileName });
					echoStatusText(text, MessageEnum.Error);
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

		if (projectExplorerStorage.isProjectExplorerOpened()) {
			IProjectExplorer projectExplorer = projectExplorerStorage
					.getProjectExplorer();
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
			echoStatusText(text, MessageEnum.Successfull);
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
		// TODO ovo bolje slozit
		IProjectExplorer projectExplorer = new DefaultProjectExplorer();
		projectExplorer.setSystemContainer(this);
		projectExplorerStorage.add("Project Explorer", projectExplorer);
		refreshWorkspace();
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	@Override
	public IView openView(String type) {
		IView view = viewStorage.getOpenedView(type);
		if (view == null) {
			// Initialization of an editor
			view = communicator.getView(type);
			view.setSystemContainer(this);
			// End of initialization

			String title = bundle.getString(LanguageConstants.TITLE_FOR + type);
			viewStorage.add(type, title, view);
		}
		viewStorage.setSelectedView(type);
		return getView(type);
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
			viewStorage.close(view);
		}
	}

	/**
	 * TODO PENDING REMOVAL!
	 */
	@Override
	public void refreshWorkspace() {
		if (!projectExplorerStorage.isProjectExplorerOpened()) {
			return;
		}
		IProjectExplorer projectExplorer = projectExplorerStorage
				.getProjectExplorer();
		// TODO to treba jos do kraja napravit, zajedno s refreshProject i
		// refreshFile
		List<String> openedProjects = projectExplorer.getAllProjects();
		for (String p : openedProjects) {
			projectExplorer.removeProject(p);
		}

		try {
			List<String> projects = communicator.getAllProjects();
			for (String projectName : projects) {
				projectExplorer.addProject(projectName);
			}

			String text = bundle
					.getString(LanguageConstants.STATUSBAR_LOAD_COMPLETE);
			echoStatusText(text, MessageEnum.Successfull);
		} catch (UniformAppletException e) {
			String text = bundle
					.getString(LanguageConstants.STATUSBAR_CANT_LOAD_WORKSPACE);
			echoStatusText(text, MessageEnum.Error);
			// TODO ovo se treba maknut kad MainApplet vise nece bit u
			// development fazi
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			JOptionPane.showMessageDialog(parentFrame, sw.toString());
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
	 * @param id
	 *            an identifier of an editor
	 * @return a new editor instance
	 */
	private IEditor getNewEditorInstance(String id) {
		return communicator.getEditor(id);
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
	private String showCreateProjectDialog(String title, String message) {
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
		return projectName;
	}

}
