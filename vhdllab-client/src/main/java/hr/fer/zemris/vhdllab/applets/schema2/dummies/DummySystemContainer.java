package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

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
	public void createNewProjectInstance() {
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

    @Override
    public IProjectExplorer getProjectExplorer() {
        return null;
    }

}













