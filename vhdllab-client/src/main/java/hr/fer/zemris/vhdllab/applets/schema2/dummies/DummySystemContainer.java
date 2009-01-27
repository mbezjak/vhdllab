package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.platform.manager.workspace.model.FileIdentifier;

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
	public boolean createNewFileInstance(FileType type) {
		return false;
	}

	@Override
	public void createNewProjectInstance() {
	}

    @Override
    public IProjectExplorer getProjectExplorer() {
        return null;
    }

    @Override
    public FileIdentifier showCompilationRunDialog() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FileIdentifier showSimulationRunDialog() {
        // TODO Auto-generated method stub
        return null;
    }

}













