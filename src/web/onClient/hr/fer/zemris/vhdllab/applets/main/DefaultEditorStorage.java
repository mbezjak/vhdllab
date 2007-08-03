package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentStorage;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.utilities.ModelUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;

/**
 * This is a default implementation of {@link IEditorStorage}.
 * 
 * @author Miro Bezjak
 */
public class DefaultEditorStorage implements IEditorStorage {

	/**
	 * An extended component storage.
	 */
	private IComponentStorage storage;
	/**
	 * An editor group.
	 */
	private ComponentGroup group;

	/**
	 * Constructs an editor storage as an extension to {@link IComponentStorage}.
	 * 
	 * @param storage
	 *            a component storage to extend
	 * @throws NullPointerException
	 *             is <code>storage</code> is <code>null</code>
	 */
	public DefaultEditorStorage(IComponentStorage storage) {
		if (storage == null) {
			throw new NullPointerException("Component storage cant be null.");
		}
		this.group = ComponentGroup.EDITOR;
		this.storage = storage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#add(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	@Override
	public boolean add(IEditor editor) {
		String identifier = createEditorIdentifierFor(editor);
		String title = createTitleFor(editor);
		return add(identifier, title, editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#add(java.lang.String,
	 *      java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	@Override
	public boolean add(String identifier, String title, IEditor editor) {
		return storage.add(identifier, group, title, getComponentFor(editor));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#close(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	@Override
	public IEditor close(IEditor editor) {
		return close(createEditorIdentifierFor(editor));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#close(java.lang.String)
	 */
	@Override
	public IEditor close(String identifier) {
		return (IEditor) storage.remove(identifier, group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#move(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentPlacement)
	 */
	@Override
	public void move(IEditor editor, ComponentPlacement placement) {
		move(createEditorIdentifierFor(editor), placement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#move(java.lang.String,
	 *      hr.fer.zemris.vhdllab.applets.main.ComponentPlacement)
	 */
	@Override
	public void move(String identifier, ComponentPlacement placement) {
		storage.moveComponent(identifier, group, placement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#getOpenedEditor(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public IEditor getOpenedEditor(String projectName, String fileName) {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		FileIdentifier identifier = new FileIdentifier(projectName, fileName);
		return getOpenedEditor(createEditorIdentifierFor(identifier));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#getOpenedEditor(java.lang.String)
	 */
	@Override
	public IEditor getOpenedEditor(String identifier) {
		return (IEditor) storage.getComponent(identifier, group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#getAllOpenedEditors()
	 */
	@Override
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
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#getOpenedEditorsThatHave(java.lang.String)
	 */
	@Override
	public List<IEditor> getOpenedEditorsThatHave(String projectName) {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null.");
		}
		List<IEditor> editorsHavingSpecifiedProject = new ArrayList<IEditor>();
		for (IEditor e : getAllOpenedEditors()) {
			String editorProjectName = e.getProjectName();
			if (ModelUtil.projectNamesAreEqual(editorProjectName, projectName)) {
				editorsHavingSpecifiedProject.add(e);
			}
		}
		return editorsHavingSpecifiedProject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#setSelectedEditor(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	@Override
	public void setSelectedEditor(IEditor editor) {
		setSelectedEditor(createEditorIdentifierFor(editor));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#setSelectedEditor(java.lang.String)
	 */
	@Override
	public void setSelectedEditor(String identifier) {
		storage.setSelectedComponent(identifier, group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#getSelectedEditor()
	 */
	@Override
	public IEditor getSelectedEditor() {
		return (IEditor) storage.getSelectedComponent(group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#isEditorOpened(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	@Override
	public boolean isEditorOpened(IEditor editor) {
		return isEditorOpened(createEditorIdentifierFor(editor));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#isEditorOpened(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isEditorOpened(String projectName, String fileName) {
		if (projectName == null) {
			throw new NullPointerException("Project name cant be null");
		}
		if (fileName == null) {
			throw new NullPointerException("File name cant be null");
		}
		FileIdentifier identifier = new FileIdentifier(projectName, fileName);
		return isEditorOpened(createEditorIdentifierFor(identifier));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#isEditorOpened(java.lang.String)
	 */
	@Override
	public boolean isEditorOpened(String identifier) {
		return storage.contains(identifier, group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#setTitle(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor,
	 *      java.lang.String)
	 */
	@Override
	public void setTitle(IEditor editor, String title) {
		setTitle(createEditorIdentifierFor(editor), title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#setTitle(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void setTitle(String identifier, String title) {
		storage.setTitle(identifier, group, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#setToolTipText(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor,
	 *      java.lang.String)
	 */
	@Override
	public void setToolTipText(IEditor editor, String tooltip) {
		setToolTipText(createEditorIdentifierFor(editor), tooltip);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#setToolTipText(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void setToolTipText(String identifier, String tooltip) {
		storage.setToolTipText(identifier, group, tooltip);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#getEditorCount()
	 */
	@Override
	public int getEditorCount() {
		return storage.getComponentCountFor(group);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return getEditorCount() == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#createTitleFor(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	@Override
	public String createTitleFor(IEditor editor) {
		if (editor == null) {
			throw new NullPointerException("Editor cant be null");
		}
		String projectName = editor.getProjectName();
		String fileName = editor.getFileName();
		if (projectName == null) {
			throw new IllegalArgumentException(
					"Editor must have a valid project name, was: "
							+ projectName);
		}
		if (fileName == null) {
			throw new IllegalArgumentException(
					"Editor must have a valid file name, was: " + fileName);
		}
		FileIdentifier identifier = new FileIdentifier(projectName, fileName);
		return createTitleFor(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#createTitleFor(hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier)
	 */
	@Override
	public String createTitleFor(FileIdentifier identifier) {
		if (identifier == null) {
			throw new NullPointerException("File identifier cant be null");
		}
		String projectName = identifier.getProjectName();
		String fileName = identifier.getFileName();
		return createTitleFor(projectName, fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#createTitleFor(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String createTitleFor(String projectName, String fileName) {
		if (projectName == null) {
			throw new IllegalArgumentException("Project name cant be null");
		}
		if (fileName == null) {
			throw new IllegalArgumentException("File name cant be null");
		}
		return projectName + "/" + fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#createEditorIdentifierFor(hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor)
	 */
	@Override
	public String createEditorIdentifierFor(IEditor editor) {
		return createTitleFor(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorStorage#createEditorIdentifierFor(hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier)
	 */
	@Override
	public String createEditorIdentifierFor(FileIdentifier identifier) {
		return createTitleFor(identifier);
	}

	/**
	 * Converts an editor to a component.
	 * 
	 * @param editor
	 *            an editor to convert
	 * @return editor as a component
	 */
	private JComponent getComponentFor(IEditor editor) {
		return (JComponent) editor;
	}

}
