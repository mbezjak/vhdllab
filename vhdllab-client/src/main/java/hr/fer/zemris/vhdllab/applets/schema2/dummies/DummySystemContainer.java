package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentProvider;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

import java.util.ArrayList;
import java.util.List;









public class DummySystemContainer implements ISystemContainer {
	
	private IResourceManager resourceManager;
	
	public DummySystemContainer() {
		resourceManager = new DummyResourceManager();
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getResourceManagement()
	 */
	@Override
	public IResourceManager getResourceManager() {
		return resourceManager;
	}

	@Override
	public boolean compile(FileIdentifier file) {
		return false;
	}

	@Override
	public boolean compile(Caseless projectName, Caseless fileName) {
		return false;
	}

	@Override
	public boolean compileLastHistoryResult() {
		return false;
	}

	@Override
	public boolean compileWithDialog() {
		return false;
	}

	@Override
	public boolean createNewFileInstance(FileType type) {
		return false;
	}

	@Override
	public boolean createNewProjectInstance() {
		return false;
	}

	@Override
	public IComponentProvider getComponentProvider() {
		return null;
	}

	@Override
	public FileIdentifier getSelectedFile() {
		return null;
	}

	@Override
	public Caseless getSelectedProject() {
		return null;
	}

	@Override
	public boolean simulate(FileIdentifier file) {
		return false;
	}

	@Override
	public boolean simulate(Caseless projectName, Caseless fileName) {
		return false;
	}

	@Override
	public boolean simulateLastHistoryResult() {
		return false;
	}

	@Override
	public boolean simulateWithDialog() {
		return false;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getEditorManager()
	 */
	@Override
	public IEditorManager getEditorManager() {
		return null;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#showSaveDialog(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public List<IEditor> showSaveDialog(String title, String message,
			List<IEditor> editorsToBeSaved) {
		return new ArrayList<IEditor>();
	}

    @Override
    public IProjectExplorer getProjectExplorer() {
        // TODO Auto-generated method stub
        return null;
    }

}













