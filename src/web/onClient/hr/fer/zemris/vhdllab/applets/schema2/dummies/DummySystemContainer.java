package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import hr.fer.zemris.vhdllab.applets.main.component.statusbar.IStatusBar;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentProvider;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean compile(IEditor editor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean compile(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean compileLastHistoryResult() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean compileWithDialog() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createNewFileInstance(String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createNewProjectInstance() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IComponentProvider getComponentProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileIdentifier getSelectedFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSelectedProject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStatusBar getStatusBar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean simulate(FileIdentifier file) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean simulate(IEditor editor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean simulate(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean simulateLastHistoryResult() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean simulateWithDialog() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getEditorManager()
	 */
	@Override
	public IEditorManager getEditorManager() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#showSaveDialog(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public List<IEditor> showSaveDialog(String title, String message,
			List<IEditor> editorsToBeSaved) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getViewManager()
	 */
	@Override
	public IViewManager getViewManager() {
		// TODO Auto-generated method stub
		return null;
	}

}












