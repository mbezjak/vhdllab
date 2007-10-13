package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.ComponentIdentifierFactory;
import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier;
import hr.fer.zemris.vhdllab.applets.main.conf.ComponentConfiguration;
import hr.fer.zemris.vhdllab.applets.main.conf.EditorProperties;
import hr.fer.zemris.vhdllab.applets.main.constant.LanguageConstants;
import hr.fer.zemris.vhdllab.applets.main.event.EditorListener;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IExplicitSave;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.client.core.bundle.ResourceBundleProvider;
import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.client.core.prefs.UserPreferences;
import hr.fer.zemris.vhdllab.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.utilities.ModelUtil;
import hr.fer.zemris.vhdllab.utilities.PlaceholderUtil;
import hr.fer.zemris.vhdllab.vhdl.VHDLGenerationResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JComponent;

/**
 * This is a default implementation of {@link IEditorManager}.
 * 
 * @author Miro Bezjak
 */
public class DefaultEditorManager implements IEditorManager {

	/**
	 * An editor storage.
	 */
	private IComponentStorage storage;

	/**
	 * An editor configuration.
	 */
	private ComponentConfiguration conf;

	/**
	 * A system container for accessing more information.
	 */
	private ISystemContainer container;

	/**
	 * A resource manager.
	 */
	private IResourceManager resourceManager;

	/**
	 * A resource bundle.
	 */
	private ResourceBundle bundle;

	/**
	 * A component group.
	 */
	private ComponentGroup group;

	/**
	 * Constructs a default editor manager out of specified editor storage and
	 * configuration.
	 * 
	 * @param storage
	 *            an editor storage
	 * @param conf
	 *            an editor configuration
	 * @param container
	 *            a system container for accessing more information
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public DefaultEditorManager(IComponentStorage storage,
			ComponentConfiguration conf, ISystemContainer container) {
		if (storage == null) {
			throw new NullPointerException("Editor storage cant be null");
		}
		if (conf == null) {
			throw new NullPointerException("Editor configuration cant be null");
		}
		if (container == null) {
			throw new NullPointerException("User preferences cant be null");
		}
		this.storage = storage;
		this.conf = conf;
		this.container = container;
		this.resourceManager = container.getResourceManager();
		this.bundle = ResourceBundleProvider
				.getBundle(LanguageConstants.APPLICATION_RESOURCES_NAME_MAIN);
		group = ComponentGroup.EDITOR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#openPreferences()
	 */
	public IEditor openPreferences() {
		IComponentIdentifier<?> identifier = ComponentIdentifierFactory
				.createPreferencesIdentifier();
		return openEditor(identifier, "");
	}

	@SuppressWarnings("unchecked")
	public IEditor viewVHDLCode(IEditor editor) {
		if (editor == null) {
			throw new NullPointerException("Editor cant be null");
		}
		IComponentIdentifier<?> identifier = getIdentifierFor(editor);
		if (identifier == null) {
			return null;
		}
		if (!(identifier.getInstanceModifier() instanceof FileIdentifier)) {
			return null;
		}
		IComponentIdentifier<FileIdentifier> viewVHDLId = ComponentIdentifierFactory
				.createViewVHDLIdentifier((FileIdentifier) identifier
						.getInstanceModifier());
		return viewVHDLCode(viewVHDLId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#viewVHDLCode(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier)
	 */
	public IEditor viewVHDLCode(IComponentIdentifier<FileIdentifier> identifier) {
		if (identifier == null) {
			throw new NullPointerException("Editor identifier cant be null");
		}
		FileIdentifier file = identifier.getInstanceModifier();
		if (file == null) {
			throw new IllegalArgumentException(
					"Not enough information to view vhdl code");
		}
		String projectName = file.getProjectName();
		String fileName = file.getFileName();
		if (!resourceManager.canGenerateVHDLCode(projectName, fileName)) {
			String text = bundle
					.getString(LanguageConstants.STATUSBAR_CANT_VIEW_VHDL_CODE_FOR_THAT_FILE);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { fileName });
			echoStatusText(text, MessageType.INFORMATION);
			return null;
		}

		VHDLGenerationResult data;
		try {
			data = resourceManager.generateVHDL(projectName, fileName);
		} catch (UniformAppletException e) {
			String text = bundle
					.getString(LanguageConstants.STATUSBAR_CANT_VIEW_VHDL_CODE);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { fileName });
			echoStatusText(text, MessageType.ERROR);
			return null;
		}
		if (!data.isSuccessful()) {
			String text = bundle
					.getString(LanguageConstants.STATUSBAR_CANT_VIEW_VHDL_CODE);
			text = PlaceholderUtil.replacePlaceholders(text,
					new String[] { fileName });
			echoStatusText(text, MessageType.ERROR);
			return null;
		}
		FileContent content = new FileContent(projectName, fileName, data
				.getVhdl());
		return openEditor(identifier, content);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#openEditor(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier,
	 *      java.lang.String)
	 */
	public IEditor openEditor(IComponentIdentifier<?> identifier, String data) {
		FileContent content = new FileContent(data);
		return openEditor(identifier, content);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#openEditorByResource(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier)
	 */
	public IEditor openEditorByResource(
			IComponentIdentifier<FileIdentifier> identifier) {
		if (identifier == null) {
			throw new NullPointerException("Editor identifier cant be null");
		}
		FileIdentifier file = identifier.getInstanceModifier();
		if (file == null) {
			throw new IllegalArgumentException(
					"Not enough information to view vhdl code");
		}
		String projectName = file.getProjectName();
		String fileName = file.getFileName();
		// only so content should not be loaded
		if (isEditorOpened(identifier)) {
			storage.setSelectedComponent(identifier);
			return getOpenedEditor(identifier);
		}
		String content;
		try {
			content = resourceManager.getFileContent(projectName, fileName);
		} catch (UniformAppletException e) {
			e.printStackTrace();
			String text = bundle
					.getString(LanguageConstants.STATUSBAR_INTERNAL_ERROR);
			echoStatusText(text, MessageType.ERROR);
			return null;
		}
		FileContent fileContent = new FileContent(projectName, fileName,
				content);
		return openEditor(identifier, fileContent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#openEditor(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier,
	 *      hr.fer.zemris.vhdllab.applets.main.model.FileContent)
	 */
	public IEditor openEditor(IComponentIdentifier<?> identifier,
			FileContent content) {
		if (identifier == null) {
			throw new NullPointerException("Editor identifier cant be null");
		}
		if (content == null) {
			throw new NullPointerException("File content cant be null");
		}
		if (storage.contains(identifier)) {
			storage.setSelectedComponent(identifier);
			IEditor editor = getOpenedEditor(identifier);
			editor.setFileContent(content);
			return editor;
		}

		EditorProperties ep = conf.getEditorProperties(identifier);
		boolean savable = ep.isSavable();
		boolean readOnly = ep.isReadonly();
		// Initialization of an editor
		IEditor editor = getNewEditorInstance(identifier);
		editor.setSystemContainer(container);
		editor.init();
		editor.setSavable(savable);
		editor.setReadOnly(readOnly);
		editor.setFileContent(content);
		editor.setModified(false);
		// End of initialization

		editor.addEditorListener(new EditorModifiedListener());

		String tooltipSpecial;
		if (readOnly) {
			tooltipSpecial = bundle
					.getString(LanguageConstants.TOOLTIP_EDITOR_READONLY);
		} else {
			tooltipSpecial = bundle
					.getString(LanguageConstants.TOOLTIP_EDITOR_EDITABLE);
		}
		String type = identifier.getComponentType();
		String[] replacements = new String[] { content.getFileName(),
				content.getProjectName(), tooltipSpecial };
		String title = bundle.getString(LanguageConstants.TITLE_FOR + type);
		title = PlaceholderUtil.replacePlaceholders(title, replacements);
		String tooltip = bundle.getString(LanguageConstants.TOOLTIP_FOR + type);
		tooltip = PlaceholderUtil.replacePlaceholders(tooltip, replacements);
		ComponentPlacement placement = getPlacement(identifier);
		storage.add(identifier, group, title, (JComponent) editor, placement);
		storage.setToolTipText(identifier, tooltip);
		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#getOpenedEditor(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier)
	 */
	public IEditor getOpenedEditor(IComponentIdentifier<?> identifier) {
		if (identifier == null) {
			throw new NullPointerException("Editor identifier cant be null");
		}
		return (IEditor) storage.getComponent(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#saveEditor(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	public void saveEditor(IEditor editor) {
		if (editor == null) {
			throw new NullPointerException("Editor cant be null");
		}
		List<IEditor> editorsToSave = new ArrayList<IEditor>(1);
		editorsToSave.add(editor);
		saveEditors(editorsToSave);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#saveEditorExplicitly(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	public void saveEditorExplicitly(IEditor editor) {
		if (editor == null) {
			throw new NullPointerException("Editor cant be null");
		}
		boolean saved = saveEditorImpl(editor, true);
		if (saved) {
			IComponentIdentifier<?> peIdentifier = ComponentIdentifierFactory
					.createProjectExplorerIdentifier();
			if (container.getViewManager().isViewOpened(peIdentifier)) {
				IView view = container.getViewManager().getOpenedView(
						peIdentifier);
				IProjectExplorer projectExplorer = view
						.asInterface(IProjectExplorer.class);
				projectExplorer.refreshProject(editor.getProjectName());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#saveAllEditors()
	 */
	public void saveAllEditors() {
		List<IEditor> openedEditors = getAllOpenedEditors();
		saveEditors(openedEditors);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#saveEditors(java.util.List)
	 */
	public void saveEditors(List<IEditor> editorsToSave) {
		if (editorsToSave == null) {
			throw new NullPointerException(
					"A list of editors to save cant be null");
		}
		if (editorsToSave.isEmpty()) {
			return;
		}
		List<IEditor> savedEditors = new ArrayList<IEditor>();
		List<String> projects = new ArrayList<String>();
		for (IEditor editor : editorsToSave) {
			boolean saved = saveEditorImpl(editor, false);
			if (saved) {
				savedEditors.add(editor);
				String projectName = editor.getProjectName();
				if (!projects.contains(projectName)) {
					projects.add(projectName);
				}
			}
		}

		IComponentIdentifier<?> peIdentifier = ComponentIdentifierFactory
				.createProjectExplorerIdentifier();
		if (container.getViewManager().isViewOpened(peIdentifier)) {
			IView view = container.getViewManager().getOpenedView(peIdentifier);
			IProjectExplorer projectExplorer = view
					.asInterface(IProjectExplorer.class);
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
	 * This is an actual implementation of saveEditor method. Returns
	 * <code>true</code> if editor has been saved or <code>false</code>
	 * otherwise.
	 * 
	 * @param editor
	 *            an editor to save
	 * @param tryExplicitly
	 *            <code>true</code> if this method should try to save an
	 *            editor explicitly if it is not savable
	 * @return <code>true</code> if editor has been saved; <code>false</code>
	 *         otherwise
	 * @throws IllegalStateException
	 *             if editor's explicit save class can't be instantiated
	 */
	private boolean saveEditorImpl(IEditor editor, boolean tryExplicitly) {
		if (editor.isSavable() && editor.isModified()) {
			String fileName = editor.getFileName();
			String projectName = editor.getProjectName();
			String content = editor.getData();
			if (content == null) {
				return false;
			}
			try {
				resourceManager.saveFile(projectName, fileName, content);
			} catch (UniformAppletException e) {
				String text = bundle
						.getString(LanguageConstants.STATUSBAR_CANT_SAVE_FILE);
				text = PlaceholderUtil.replacePlaceholders(text,
						new String[] { fileName });
				echoStatusText(text, MessageType.ERROR);
				return false;
			}
			editor.setModified(false);
			return true;
		} else if (tryExplicitly) {
			IComponentIdentifier<?> id = getIdentifierFor(editor);
			EditorProperties ep = conf.getEditorProperties(id);
			if (ep.getExplicitSaveValue()) {
				String clazz = ep.getExplicitSaveClass();
				IExplicitSave saveClass;
				try {
					Class<?> c = Class.forName(clazz);
					if (c == null) {
						return false;
					}
					saveClass = (IExplicitSave) c.newInstance();
				} catch (Exception e) {
					throw new IllegalStateException(
							"Cant instantiate explicit save class", e);
				}
				try {
					saveClass.save(editor, container);
				} catch (UniformAppletException e) {
					String text = bundle
							.getString(LanguageConstants.STATUSBAR_CANT_SAVE_FILE);
					text = PlaceholderUtil.replacePlaceholders(text,
							new String[] { editor.getFileName() });
					echoStatusText(text, MessageType.ERROR);
					return false;
				}
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#closeEditor(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	public void closeEditor(IEditor editor) {
		closeEditor(editor, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#closeEditorWithoutSaving(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	public void closeEditorWithoutSaving(IEditor editor) {
		closeEditor(editor, false);
	}

	/**
	 * Closes a specified editor. If <code>editor</code> is <code>null</code>
	 * no exception will be thrown and this method will simply return.
	 * 
	 * @param editor
	 *            an editor to close
	 * @param shouldSave
	 *            <code>true</code> if an editor should be saved before
	 *            closing it; <code>false</code> otherwise
	 */
	private void closeEditor(IEditor editor, boolean shouldSave) {
		if (editor == null) {
			return;
		}
		List<IEditor> editorsToClose = new ArrayList<IEditor>(1);
		editorsToClose.add(editor);
		closeEditors(editorsToClose, shouldSave);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#closeAllEditors()
	 */
	public void closeAllEditors() {
		List<IEditor> openedEditors = getAllOpenedEditors();
		closeEditors(openedEditors);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#closeAllButThisEditor(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	public void closeAllButThisEditor(IEditor editorToKeepOpened) {
		if (editorToKeepOpened == null)
			return;
		List<IEditor> openedEditors = getAllOpenedEditors();
		openedEditors.remove(editorToKeepOpened);
		closeEditors(openedEditors);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#closeEditors(java.util.List)
	 */
	public void closeEditors(List<IEditor> editorsToClose) {
		closeEditors(editorsToClose, true);
	}

	/**
	 * Closes all specified editors. If <code>editorsToClose</code> is
	 * <code>null</code> no exception will be thrown and this method will
	 * simply return.
	 * 
	 * @param editorsToClose
	 *            an editors to close
	 * @param shouldSave
	 *            <code>true</code> if an editor should be saved before
	 *            closing it; <code>false</code> otherwise
	 */
	private void closeEditors(List<IEditor> editorsToClose, boolean shouldSave) {
		if (editorsToClose == null)
			return;
		editorsToClose = pickOpenedEditors(editorsToClose);
		boolean shouldContinue;
		if (shouldSave) {
			String title = bundle
					.getString(LanguageConstants.DIALOG_SAVE_RESOURCES_TITLE);
			String message = bundle
					.getString(LanguageConstants.DIALOG_SAVE_RESOURCES_MESSAGE);
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

				storage.remove((JComponent) editor);
			}
		}
	}

	/**
	 * Returns only opened editors from a list of specified editors.
	 * 
	 * @param editors
	 *            a list of specified editors
	 * @return opened editors from a list of specified editors
	 */
	private List<IEditor> pickOpenedEditors(List<IEditor> editors) {
		List<IEditor> openedEditors = new ArrayList<IEditor>();
		for (IEditor e : editors) {
			if (isEditorOpened(e)) {
				openedEditors.add(e);
			}
		}
		return openedEditors;
	}

	/**
	 * Resets an editor title to make it visible to a user that an editor's
	 * content has been changed.
	 * 
	 * @param projectName
	 *            a name of a project that file belongs to
	 * @param fileName
	 *            a name of a file that editor represents
	 * @param contentChanged
	 *            <code>true</code> if an editor content has been changed;
	 *            <code>false</code> otherwise.
	 */
	private void resetEditorTitle(String projectName, String fileName,
			boolean modified) {
		IComponentIdentifier<FileIdentifier> id = ComponentIdentifierFactory
				.createFileEditorIdentifier(projectName, fileName);
		resetEditorTitle(id, modified);
	}

	/**
	 * Resets an editor title to make it visible to a user that an editor's
	 * content has been changed.
	 * 
	 * @param identifier
	 *            an identifier of an editor
	 * @param contentChanged
	 *            <code>true</code> if an editor content has been changed;
	 *            <code>false</code> otherwise.
	 */
	private void resetEditorTitle(IComponentIdentifier<FileIdentifier> id,
			boolean modified) {
		String title = getTitle(id);
		if (modified) {
			title = "*" + title;
		} else if (title.startsWith("*")) {
			title = title.substring(1);
		}
		IEditor editor = getOpenedEditor(id);
		if (editor != null) {
			setTitle(editor, title);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#saveResourcesWithDialog(java.util.List)
	 */
	@Override
	public boolean saveResourcesWithDialog(List<IEditor> openedEditors,
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
			String name = UserFileConstants.SYSTEM_ALWAYS_SAVE_RESOURCES;
			boolean shouldAutoSave = UserPreferences.instance().getBoolean(
					name, false);
			List<IEditor> editorsToSave = notSavedEditors;
			if (!shouldAutoSave) {
				editorsToSave = container.showSaveDialog(title, message,
						notSavedEditors);
				if (editorsToSave == null)
					return false;
			}

			saveEditors(editorsToSave);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#isEditorOpened(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	public boolean isEditorOpened(IEditor editor) {
		if (editor == null) {
			throw new NullPointerException("Editor cant be null");
		}
		IComponentIdentifier<?> identifier = getIdentifierFor(editor);
		return isEditorOpened(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#isEditorOpened(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier)
	 */
	public boolean isEditorOpened(IComponentIdentifier<?> identifier) {
		return storage.contains(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#findAllEditorAssociatedWith(java.lang.Object)
	 */
	public List<IEditor> findAllEditorsAssociatedWith(Object instanceModifier) {
		if (instanceModifier == null) {
			throw new NullPointerException("Instance modifier cant be null");
		}
		List<IEditor> associatedEditors = new ArrayList<IEditor>();
		for (IEditor e : getAllOpenedEditors()) {
			IComponentIdentifier<?> id = getIdentifierFor(e);
			Object im = id.getInstanceModifier();
			if (im != null) {
				if (im.equals(instanceModifier)) {
					associatedEditors.add(e);
				}
			}
		}
		return associatedEditors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#getAllOpenedEditors()
	 */
	public List<IEditor> getAllOpenedEditors() {
		Collection<JComponent> components = storage.getComponents(group);
		List<IEditor> openedEditors = new ArrayList<IEditor>(components.size());
		for (JComponent c : components) {
			openedEditors.add((IEditor) c);
		}
		return openedEditors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#getOpenedEditorsThatHave(java.lang.String)
	 */
	@Override
	public List<IEditor> getOpenedEditorsThatHave(String projectName) {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null.");
		}
		List<IEditor> editorsHavingSpecifiedProject = new ArrayList<IEditor>();
		for (IEditor e : getAllOpenedEditors()) {
			String editorProjectName = e.getProjectName();
			if (editorProjectName == null) {
				continue;
			}
			if (ModelUtil.projectNamesAreEqual(editorProjectName, projectName)) {
				editorsHavingSpecifiedProject.add(e);
			}
		}
		return editorsHavingSpecifiedProject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#getSelectedEditor()
	 */
	@Override
	public IEditor getSelectedEditor() {
		return (IEditor) storage.getSelectedComponent(group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#getTitle(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier)
	 */
	public String getTitle(IComponentIdentifier<?> identifier) {
		return storage.getTitleFor(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#setTitle(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor,
	 *      java.lang.String)
	 */
	public void setTitle(IEditor editor, String title) {
		if (editor == null) {
			throw new NullPointerException("Editor cant be null");
		}
		IComponentIdentifier<?> id = getIdentifierFor(editor);
		setTitle(id, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager#setTitle(hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier,
	 *      java.lang.String)
	 */
	public void setTitle(IComponentIdentifier<?> identifier, String title) {
		storage.setTitle(identifier, title);
	}

	/**
	 * Returns an identifier for an opened <code>editor</code> or
	 * <code>null</code> if an <code>editor</code> is not stored.
	 * 
	 * @param editor
	 *            an editor for whom to return identifier
	 * @return an identifier for a specified editor
	 * @throws NullPointerException
	 *             if <code>editor</code> is <code>null</code>
	 */
	private IComponentIdentifier<?> getIdentifierFor(IEditor editor) {
		return storage.getIdentifierFor((JComponent) editor);
	}

	/**
	 * Returns a component placement out of specified identifier.
	 * 
	 * @param identifier
	 *            an identifier for whom to determine placement
	 * @return a component placement
	 */
	private ComponentPlacement getPlacement(IComponentIdentifier<?> identifier) {
		String id = identifier.getComponentType();
		UserPreferences pref = UserPreferences.instance();
		String name = UserFileConstants.SYSTEM_COMPONENT_PLACEMENT_FOR;
		String property = pref.get(name + id, null);
		if (property == null) {
			name = UserFileConstants.SYSTEM_DEFAULT_EDITOR_PLACEMENT;
			property = pref.get(name, null);
			if (property == null) {
				property = ComponentPlacement.CENTER.name();
			}
		}
		return ComponentPlacement.valueOf(property);
	}

	/**
	 * Returns a new instance of specified editor.
	 * 
	 * @param identifier
	 *            an identifier of an editor
	 * @return a new editor instance
	 * @throws NullPointerException
	 *             if <code>id</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if can't find editor for given <code>id</code>
	 * @throws IllegalStateException
	 *             if editor can't be instantiated
	 */
	private IEditor getNewEditorInstance(IComponentIdentifier<?> identifier) {
		if (identifier == null) {
			throw new NullPointerException("Identifier can not be null.");
		}
		EditorProperties ep = conf.getEditorProperties(identifier);
		if (ep == null) {
			throw new IllegalArgumentException(
					"Can not find editor for given identifier.");
		}
		IEditor editor = null;
		try {
			editor = (IEditor) Class.forName(ep.getClazz()).newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Cant instantiate editor", e);
		}
		return editor;
	}

	/**
	 * An alias method for
	 * {@link ISystemContainer#echoStatusText(String, MessageType)} method.
	 * 
	 * @param text
	 *            a text to echo
	 * @param type
	 *            a message type
	 */
	private void echoStatusText(String text, MessageType type) {
		SystemLog.instance().addSystemMessage(text, type);
	}

	private class EditorModifiedListener implements EditorListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see hr.fer.zemris.vhdllab.applets.main.event.EditorListener#modified(java.lang.String,
		 *      java.lang.String, boolean)
		 */
		@Override
		public void modified(String projectName, String fileName, boolean flag) {
			resetEditorTitle(projectName, fileName, flag);
		}
	}

}
