package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ResourceBundle;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.IStatusBar;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageEnum;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.i18n.CachedResourceBundles;
import hr.fer.zemris.vhdllab.preferences.PropertyListener;
import hr.fer.zemris.vhdllab.preferences.UserPreferences;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;









public class DummyProjectContainer implements ProjectContainer {

	public void compile(String projectName, String fileName) {
		// TODO Auto-generated method stub
		
	}

	public void createNewFileInstance(String type) {
		// TODO Auto-generated method stub
		
	}

	public void createNewProjectInstance() {
		// TODO Auto-generated method stub
		
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

	public IEditor getEditor(String projectName, String fileName) throws UniformAppletException {
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

	public UserPreferences getPreferences() throws UniformAppletException {
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

	public IView getView(String type) throws UniformAppletException {
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

	public void openEditor(String projectName, String fileName, boolean isSavable, boolean isReadOnly) throws UniformAppletException {
		// TODO Auto-generated method stub
		
	}

	public void resetEditorTitle(boolean contentChanged, String projectName, String fileName) {
		// TODO Auto-generated method stub
		
	}

	public void simulate(String projectName, String fileName) {
		// TODO Auto-generated method stub
		
	}

	public void viewVHDLCode(String projectName, String fileName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getProperty(String name) throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveProperty(String name, String data)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPropertyListener(PropertyListener l, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePropertyListener(PropertyListener l, String name) {
		// TODO Auto-generated method stub
		
	}
	
}













