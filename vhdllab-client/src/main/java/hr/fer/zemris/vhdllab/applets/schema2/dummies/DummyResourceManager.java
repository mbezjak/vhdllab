package hr.fer.zemris.vhdllab.applets.schema2.dummies;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.event.VetoableResourceListener;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

import java.util.ArrayList;
import java.util.List;

public class DummyResourceManager implements IResourceManager {

	@Override
	public void addVetoableResourceListener(VetoableResourceListener l) {
		
	}

	@Override
	public CompilationResult compile(Caseless projectName, Caseless fileName)
			throws UniformAppletException {
		return null;
	}

	@Override
	public boolean createNewResource(Caseless projectName, Caseless fileName,
			FileType type, String data) throws UniformAppletException {
		return false;
	}

	@Override
	public void deleteFile(Caseless projectName, Caseless fileName)
			throws UniformAppletException {
		
	}

	@Override
	public void deleteProject(Caseless projectName) throws UniformAppletException {
		
	}

	@Override
	public boolean existsFile(Caseless projectName, Caseless fileName)
			throws UniformAppletException {
		return false;
	}

	@Override
	public Hierarchy extractHierarchy(Caseless projectName)
			throws UniformAppletException {
		return null;
	}

	@Override
	public List<Caseless> getAllCircuits(Caseless projectName)
			throws UniformAppletException {
		return new ArrayList<Caseless>();
	}

	@Override
	public List<Caseless> getAllProjects() throws UniformAppletException {
		return new ArrayList<Caseless>();
	}

	@Override
	public List<Caseless> getAllTestbenches(Caseless projectName)
			throws UniformAppletException {
		return null;
	}

	@Override
	public CircuitInterface getCircuitInterfaceFor(Caseless projectName,
	        Caseless fileName) throws UniformAppletException {
		return null;
	}

	@Override
	public FileType getFileType(Caseless projectName, Caseless fileName) {
		return null;
	}

	@Override
	public boolean isCircuit(Caseless projectName, Caseless fileName) {
		return false;
	}

	@Override
	public boolean isCompilable(Caseless projectName, Caseless fileName) {
		return false;
	}

	@Override
	public boolean isSimulatable(Caseless projectName, Caseless fileName) {
		return false;
	}

	@Override
	public boolean isTestbench(Caseless projectName, Caseless fileName) {
		return false;
	}

	@Override
	public void removeVetoableResourceListener(VetoableResourceListener l) {
		
	}

	@Override
	public SimulationResult simulate(Caseless projectName, Caseless fileName)
			throws UniformAppletException {
		return null;
	}

	@Override
	public VHDLGenerationResult generateVHDL(Caseless projectName, Caseless fileName)
			throws UniformAppletException {
		return null;
	}

	@Override
	public boolean isCorrectFileName(Caseless name) {
		return false;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IResourceManager#saveErrorMessage(java.lang.String)
	 */
	@Override
	public void saveErrorMessage(String content) throws UniformAppletException {
		
	}

}
