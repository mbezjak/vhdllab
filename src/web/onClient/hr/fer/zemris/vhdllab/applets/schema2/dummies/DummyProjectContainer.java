package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.IStatusBar;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageEnum;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IComponentProvider;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.i18n.CachedResourceBundles;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ResourceBundle;









public class DummyProjectContainer implements ISystemContainer {

	public boolean compile(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean createNewFileInstance(String type) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean createNewProjectInstance() {
		// TODO Auto-generated method stub
		return false;
	}

	public void deleteFile(String projectName, String fileName) throws UniformAppletException {
		// TODO Auto-generated method stub
		
	}

	public void deleteProject(String projectName) throws UniformAppletException {
		// TODO Auto-generated method stub
		
	}

	public void echoStatusText(String text, MessageEnum type) {
		// TODO Auto-generated method stub
		
	}

	public boolean existsFile(String projectName, String fileName) throws UniformAppletException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean existsProject(String projectName) throws UniformAppletException {
		// TODO Auto-generated method stub
		return false;
	}

	public Hierarchy extractHierarchy(String projectName) throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getAllCircuits(String projectName) throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getAllProjects() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getAllTestbenches(String projectName) throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	public CircuitInterface getCircuitInterfaceFor(String projectName, String fileName) throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	public IEditor getEditor(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFileType(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPredefinedFileContent(String fileName) throws UniformAppletException {
		if (!fileName.equals("predefined.xml")) throw new RuntimeException("Dummy only supports 'predefined.xml'.");
		
		InputStream stream = this.getClass().getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder("");
		String s;
		
		try {
			while ((s = reader.readLine()) != null) {
				sb.append(s);
				sb.append('\n');
			}
		} catch (IOException e) {
			return "";
		}
		
		return sb.toString();
	}

	public IUserPreferences getPreferences() {
		// TODO Auto-generated method stub
		return null;
	}

	public ResourceBundle getResourceBundle(String baseName) {
		return CachedResourceBundles.getBundle(baseName + "_en");
	}

	public FileIdentifier getSelectedFile() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSelectedProject() {
		// TODO Auto-generated method stub
		return null;
	}

	public IStatusBar getStatusBar() {
		// TODO Auto-generated method stub
		return null;
	}

	public IView getView(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isCircuit(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCompilable(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCorrectEntityName(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCorrectProjectName(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSimulatable(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSimulation(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isTestbench(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	public void openEditor(String projectName, String fileName, boolean isSavable, boolean isReadOnly) {
		// TODO Auto-generated method stub
		
	}

	public void resetEditorTitle(boolean contentChanged, String projectName, String fileName) {
		// TODO Auto-generated method stub
		
	}

	public boolean simulate(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	public void viewVHDLCode(String projectName, String fileName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean compile(IEditor editor) {
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
	public IComponentProvider getComponentProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getFilesFor(String projectName)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileIdentifier getLastCompilationHistoryTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileIdentifier getLastSimulationHistoryTarget() {
		// TODO Auto-generated method stub
		return null;
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
	public boolean simulate(IEditor editor) {
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
	public void openProjectExplorer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IView openView(String type) {
		// TODO Auto-generated method stub
		return null;
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
	public String getProperty(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProperty(String name, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshWorkspace() {
		// TODO Auto-generated method stub
		
	}
	
}













