package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import hr.fer.zemris.vhdllab.applets.main.component.statusbar.IStatusBar;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageType;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentProvider;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemLog;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;

import java.util.ResourceBundle;









public class DummySystemContainer implements ISystemContainer {
	
	private IResourceManagement resourceManagement;
	
	public DummySystemContainer() {
		resourceManagement = new DummyResourceManagement();
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getResourceManagement()
	 */
	@Override
	public IResourceManagement getResourceManagement() {
		return resourceManagement;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer#getResourceBundle(java.lang.String)
	 */
	@Override
	public ResourceBundle getResourceBundle(String baseName) {
		return resourceManagement.getResourceBundle(baseName);
	}

	@Override
	public void closeAllButThisEditor(IEditor editorToKeepOpened,
			boolean showDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeAllEditors(boolean showDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeEditor(IEditor editor, boolean showDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeView(IView view) {
		// TODO Auto-generated method stub
		
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
	public void echoStatusText(String text, MessageType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IComponentProvider getComponentProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEditor getEditor(FileIdentifier resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEditor getEditor(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUserPreferences getPreferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProperty(String name) {
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
	public ISystemLog getSystemLog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IView getView(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void openEditor(String projectName, String fileName,
			boolean savable, boolean readOnly) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openEditor(String projectName, String fileName, String content,
			String type, boolean savable, boolean readOnly) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openPreferences() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openProjectExplorer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IView openView(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshWorkspace() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetEditorTitle(boolean contentChanged, String projectName,
			String fileName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveAllEditors() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveEditor(IEditor editor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProperty(String name, String data) {
		// TODO Auto-generated method stub
		
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

	@Override
	public void viewVHDLCode(IEditor editor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IEditor viewVHDLCode(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

}













