package hr.fer.zemris.vhdllab.applets.main.event;

import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

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
	public void beforeResourceCompilation(Caseless projectName, Caseless fileName)
			throws ResourceVetoException {
	}

	@Override
	public void beforeResourceCreation(Caseless projectName, Caseless fileName,
			FileType type) throws ResourceVetoException {
	}

	@Override
	public void beforeResourceSimulation(Caseless projectName, Caseless fileName)
			throws ResourceVetoException {
	}

	@Override
	public void projectCreated(Caseless projectName) {
	}

	@Override
	public void resourceCompiled(Caseless projectName, Caseless fileName,
			CompilationResult result) {
	}

	@Override
	public void resourceCreated(Caseless projectName, Caseless fileName, FileType type) {
	}

	@Override
	public void resourceDeleted(Caseless projectName, Caseless fileName) {
	}
	
	@Override
	public void resourceSaved(Caseless projectName, Caseless fileName) {
	}

	@Override
	public void resourceSimulated(Caseless projectName, Caseless fileName,
			SimulationResult result) {
	}

}
