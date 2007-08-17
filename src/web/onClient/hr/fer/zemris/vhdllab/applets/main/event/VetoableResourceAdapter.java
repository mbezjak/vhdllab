package hr.fer.zemris.vhdllab.applets.main.event;

import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;

/**
 * An abstract adapter class for vetoable resource listener. The methods in this
 * class are empty. This class exists as convenience for creating listener
 * objects.
 * <p>
 * Extend this class to create a <code>VetoableResource</code> listener and
 * override the methods for the events of interest. (If you implement the
 * <code>VetoableResourceListener</code> interface, you have to define all of
 * the methods in it. This abstract class defines null methods for them all, so
 * you can only have to define methods for events you care about.)
 * </p>
 * 
 * @author Miro Bezjak
 */
public abstract class VetoableResourceAdapter implements
		VetoableResourceListener {

	@Override
	public void beforeProjectCreation(String projectName)
			throws ResourceVetoException {
	}

	@Override
	public void beforeProjectDeletion(String projectName)
			throws ResourceVetoException {
	}

	@Override
	public void beforeResourceCompilation(String projectName, String fileName)
			throws ResourceVetoException {
	}

	@Override
	public void beforeResourceCreation(String projectName, String fileName,
			String type) throws ResourceVetoException {
	}

	@Override
	public void beforeResourceDeletion(String projectName, String fileName)
			throws ResourceVetoException {
	}

	@Override
	public void beforeResourceSimulation(String projectName, String fileName)
			throws ResourceVetoException {
	}

	@Override
	public void projectCreated(String projectName) {
	}

	@Override
	public void projectDeleted(String projectName) {
	}

	@Override
	public void resourceCompiled(String projectName, String fileName,
			CompilationResult result) {
	}

	@Override
	public void resourceCreated(String projectName, String fileName, String type) {
	}

	@Override
	public void resourceDeleted(String projectName, String fileName) {
	}
	
	@Override
	public void resourceSaved(String projectName, String fileName) {
	}

	@Override
	public void resourceSimulated(String projectName, String fileName,
			SimulationResult result) {
	}

}
