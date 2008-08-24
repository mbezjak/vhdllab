package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import hr.fer.zemris.vhdllab.applets.main.component.statusbar.IStatusBar;
import hr.fer.zemris.vhdllab.applets.main.componentIdentifier.IComponentIdentifier;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentProvider;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditorManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IViewManager;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;

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
	public boolean compile(IEditor editor) {
		return false;
	}

	@Override
	public boolean compile(String projectName, String fileName) {
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
	public boolean createNewFileInstance(String type) {
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
	public String getSelectedProject() {
		return null;
	}

	@Override
	public IStatusBar getStatusBar() {
		return null;
	}

	@Override
	public boolean simulate(FileIdentifier file) {
		return false;
	}

	@Override
	public boolean simulate(IEditor editor) {
		return false;
	}

	@Override
	public boolean simulate(String projectName, String fileName) {
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

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getViewManager()
	 */
	@Override
	public IViewManager getViewManager() {
		return new IViewManager() {

			@Override
			public void closeAllButThisView(IView viewToKeepOpened) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void closeAllViews() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void closeView(IView view) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void closeViews(List<IView> viewsToClose) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public List<IView> findAllViewsAssociatedWith(
					Object instanceModifier) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<IView> getAllOpenedViews() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IComponentIdentifier<?> getIdentifierFor(IView view) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IView getOpenedView(IComponentIdentifier<?> identifier) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IView getSelectedView() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getTitle(IComponentIdentifier<?> identifier) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isViewOpened(IComponentIdentifier<?> identifier) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isViewOpened(IView view) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void openProjectExplorer() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public IView openView(IComponentIdentifier<?> identifier) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setTitle(IComponentIdentifier<?> identifier,
					String title) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setTitle(IView view, String title) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}

}













