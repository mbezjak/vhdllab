/**
 * 
 */
package hr.fer.zemris.vhdllab.service.dependency.simulation;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.service.dependency.IDependency;

import java.util.Collections;
import java.util.List;

/**
 * @author Miro Bezjak
 *
 */
public class SimulationDependency implements IDependency {
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.service.dependency.IDependency#extractDependencies(hr.fer.zemris.vhdllab.model.File, hr.fer.zemris.vhdllab.service.VHDLLabManager)
	 */
	@Override
	public List<File> extractDependencies(File f, VHDLLabManager labman)
			throws ServiceException {
		return Collections.emptyList();
	}

}
