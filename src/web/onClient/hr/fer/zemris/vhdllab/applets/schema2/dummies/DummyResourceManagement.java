package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ResourceBundle;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceListener;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement;
import hr.fer.zemris.vhdllab.i18n.CachedResourceBundles;
import hr.fer.zemris.vhdllab.preferences.IUserPreferences;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Hierarchy;

public class DummyResourceManagement implements IResourceManagement {

	@Override
	public void addVetoableResourceListener(VetoableResourceListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CompilationResult compile(String projectName, String fileName)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createNewProject(String projectName)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createNewResource(String projectName, String fileName,
			String type) throws UniformAppletException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createNewResource(String projectName, String fileName,
			String type, String data) throws UniformAppletException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteFile(String projectName, String fileName)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProject(String projectName) throws UniformAppletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean existsFile(String projectName, String fileName)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existsProject(String projectName)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Hierarchy extractHierarchy(String projectName)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllCircuits(String projectName)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllProjects() throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllTestbenches(String projectName)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CircuitInterface getCircuitInterfaceFor(String projectName,
			String fileName) throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileType(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getFilesFor(String projectName)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#getPredefinedFileContent(java.lang.String)
	 */
	@Override
	public String getPredefinedFileContent(String fileName)
			throws UniformAppletException {
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

	@Override
	public IUserPreferences getPreferences() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManagement#getResourceBundle(java.lang.String)
	 */
	@Override
	public ResourceBundle getResourceBundle(String baseName) {
		return CachedResourceBundles.getBundle(baseName + "_en");
	}

	@Override
	public VetoableResourceListener[] getVetoableResourceListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCircuit(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCompilable(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCorrectEntityName(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCorrectProjectName(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSimulatable(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSimulation(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTestbench(String projectName, String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAllVetoableResourceListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeVetoableResourceListener(VetoableResourceListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SimulationResult simulate(String projectName, String fileName)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateVHDL(String projectName, String fileName)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileContent(String projectName, String fileName)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveFile(String projectName, String fileName, String content)
			throws UniformAppletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCorrectFileName(String name) {
		// TODO Auto-generated method stub
		return false;
	}

}
